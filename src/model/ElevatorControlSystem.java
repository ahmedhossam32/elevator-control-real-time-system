package model;

import esper.Config;
import events.*;
import view.ElevatorPhase1View;

import java.util.UUID;

public class ElevatorControlSystem {

    private final AccessControlService accessControlService;
    private final RequestQueue requestQueue;
    private final Motor motor;
    private final Door door;
    private ElevatorPhase1View gui;
    private view.ElevatorPhase2View phase2Gui;
    private final EventLogger eventLogger;
    private EmergencySensor emergencySensor;
    private FloorSensor floorSensor;
    private KeyReaderSensor keyReaderSensor;
    private boolean emergencyActive = false;
    private boolean waitingForPassenger = false;
    private int pickupFloor = -1;

    public ElevatorControlSystem() {

        gui = new ElevatorPhase1View();
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        gui.setStatus("Waiting...");
        gui.setCurrentFloor(0);
        gui.setDoorState("CLOSED");

        
        phase2Gui = new view.ElevatorPhase2View();
        phase2Gui.setLocationRelativeTo(null);
        eventLogger = new EventLogger(gui, phase2Gui);
        eventLogger.log("Waiting...");

        connectButtons();

        accessControlService = new AccessControlService();
        requestQueue = new RequestQueue(this);
        requestQueue.start();
        
        keyReaderSensor = new KeyReaderSensor();
       

        motor = new Motor(this);
        motor.start();

        floorSensor = new FloorSensor(motor);
        floorSensor.start();

        emergencySensor = new EmergencySensor(motor);
        emergencySensor.start();

        door = new Door(this);

        DoorSensor doorSensor = new DoorSensor(door);
        door.setDoorSensor(doorSensor);
        doorSensor.start();
    }

    public ElevatorPhase1View getGUI() {
        return gui;
    }

    public EventLogger getEventLogger() {
        return eventLogger;
    }

    public Motor getMotor() {  
        return motor;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    private void connectButtons() {

        gui.getFloor1Btn().addActionListener(e -> sendCallRequest(1));
        gui.getFloor2Btn().addActionListener(e -> sendCallRequest(2));
        gui.getFloor3Btn().addActionListener(e -> sendCallRequest(3));
        gui.getFloor4Btn().addActionListener(e -> sendCallRequest(4));
        gui.getFloor5Btn().addActionListener(e -> sendCallRequest(5));
        gui.getFloor6Btn().addActionListener(e -> sendCallRequest(6));
        
        phase2Gui.getJButton1().addActionListener(e -> sendAccessRequest(1));
        phase2Gui.getJButton2().addActionListener(e -> sendAccessRequest(2));
        phase2Gui.getJButton3().addActionListener(e -> sendAccessRequest(3));
        phase2Gui.getJButton4().addActionListener(e -> sendAccessRequest(4));
        phase2Gui.getJButton5().addActionListener(e -> sendAccessRequest(5));
        phase2Gui.getJButton6().addActionListener(e -> sendAccessRequest(6));

        phase2Gui.getEmergency().addActionListener(e -> {
            emergencySensor.triggerEmergency();
        });

        phase2Gui.getAccessButton().addActionListener(e -> {
            eventLogger.log("Token Entered. Select Floor.");
        });
    }

    private void sendAccessRequest(int destFloor) {
    String token = phase2Gui.getJTextField4().getText();
    int currentFloor = motor.getCurrentFloor();
    keyReaderSensor.simulateAccessRequest(token, currentFloor, destFloor);
    }

    private void sendCallRequest(int floor) {
    Config.sendEvent(new CallElevatorEvent(floor));
}
    
    public void handleAccessRequest(String tokenId, int sourceFloor, int destinationFloor, long timestamp) {

        if (emergencyActive) {
            eventLogger.log("Locked due to emergency");
            return;
        }

        eventLogger.log("Access request received");

        boolean allowed = accessControlService.authenticate(tokenId);

        AuthEvent.AuthResult result = allowed ? AuthEvent.AuthResult.SUCCESS : AuthEvent.AuthResult.FAILURE;
        Config.sendEvent(new AuthEvent(result, tokenId, destinationFloor, timestamp));
    }

    public void handleAuthorizedRequest(String tokenId, int destinationFloor) {

        if (emergencyActive) return;

        eventLogger.log("Access Granted");

        ElevatorRequest request = new ElevatorRequest(
                UUID.randomUUID().toString(),
                motor.getCurrentFloor(),
                destinationFloor,
                System.currentTimeMillis(),
                tokenId
        );

        requestQueue.addRequest(request);
    }

    public void handleFailedAuth(int destinationFloor) {
        eventLogger.log("Access Denied");
        highlightAccessDeniedFloor(destinationFloor);
    }

    public void processRequest(ElevatorRequest request) {

        if (emergencyActive) return;

        eventLogger.log("Moving to floor " + request.getDestinationFloor());
        motor.moveTo(request.getDestinationFloor());
    }

    public void arriveAtFloor(int floor) {

        if (emergencyActive) return;

        eventLogger.log("Elevator arrived at floor " + floor);

        door.openDoor(floor);

        if (waitingForPassenger && floor == pickupFloor) {

            waitingForPassenger = false;

            int result = javax.swing.JOptionPane.showConfirmDialog(
                    gui,
                    "Elevator has arrived at floor " + floor + ".\nDo you want to enter?",
                    "Enter Elevator",
                    javax.swing.JOptionPane.YES_NO_OPTION);

            if (result == javax.swing.JOptionPane.YES_OPTION) {

                gui.setVisible(false);
                phase2Gui.setVisible(true);

            } else {
                eventLogger.log("Waiting for passenger...");
                gui.enableButtons();
            }

        } else {
            if (phase2Gui != null && phase2Gui.isVisible()) {

                int exitResult = javax.swing.JOptionPane.showConfirmDialog(
                        phase2Gui,
                        "Arrived at floor " + floor + ".\nDo you want to exit?",
                        "Destination Reached",
                        javax.swing.JOptionPane.YES_NO_OPTION);

                if (exitResult == javax.swing.JOptionPane.YES_OPTION) {

                    phase2Gui.setVisible(false);
                    gui.setVisible(true);
                    gui.enableButtons();
                    gui.setCurrentFloor(floor);

                    eventLogger.log("Passenger exited at floor " + floor);
                }
            }
        }
    }

    public void onDoorClosed() {

        if (emergencyActive) return;

        gui.setDoorState("CLOSED");
        eventLogger.log("Door closed");

        requestQueue.onRequestCompleted();
    }

    public void handleEmergency(int floor) {

        emergencyActive = true;

        eventLogger.log("EMERGENCY TRIGGERED!");

        motor.emergencyStop();
        requestQueue.clearAll();
        door.emergencyOpen(floor);
    }


    public void handleCallElevator(int sourceFloor) {

        if (emergencyActive) return;

        waitingForPassenger = true;
        pickupFloor = sourceFloor;

        eventLogger.log("Call registered from floor " + sourceFloor);
        gui.disableButtons();

        motor.moveTo(sourceFloor);
    }

    public void handleFloorSensorUpdate(int currentFloor) {

        gui.setCurrentFloor(currentFloor);
        eventLogger.log("Current floor: " + currentFloor);
        highlightCurrentFloor(currentFloor);
    }


    public void handleDoorObstruction(int floor) {

        if (emergencyActive) return;

        eventLogger.log("Door obstruction detected!");
        door.handleObstructionFromSensor(floor);
    }

    
    private void resetAllFloorButtonColors() {
        if (phase2Gui != null) {
            phase2Gui.getJButton1().setBackground(new java.awt.Color(153, 255, 153));
            phase2Gui.getJButton2().setBackground(new java.awt.Color(153, 255, 153));
            phase2Gui.getJButton3().setBackground(new java.awt.Color(153, 255, 153));
            phase2Gui.getJButton4().setBackground(new java.awt.Color(153, 255, 153));
            phase2Gui.getJButton5().setBackground(new java.awt.Color(153, 255, 153));
            phase2Gui.getJButton6().setBackground(new java.awt.Color(153, 255, 153));
        }
    }
    
    private void highlightCurrentFloor(int floor) {
        resetAllFloorButtonColors();
        if (phase2Gui != null) {
            switch (floor) {
                case 1:
                    phase2Gui.getJButton1().setBackground(java.awt.Color.YELLOW);
                    break;
                case 2:
                    phase2Gui.getJButton2().setBackground(java.awt.Color.YELLOW);
                    break;
                case 3:
                    phase2Gui.getJButton3().setBackground(java.awt.Color.YELLOW);
                    break;
                case 4:
                    phase2Gui.getJButton4().setBackground(java.awt.Color.YELLOW);
                    break;
                case 5:
                    phase2Gui.getJButton5().setBackground(java.awt.Color.YELLOW);
                    break;
                case 6:
                    phase2Gui.getJButton6().setBackground(java.awt.Color.YELLOW);
                    break;
            }
        }
    }
    
    private void highlightAccessDeniedFloor(int floor) {
        resetAllFloorButtonColors();
        if (phase2Gui != null) {
            switch (floor) {
                case 1:
                    phase2Gui.getJButton1().setBackground(java.awt.Color.RED);
                    break;
                case 2:
                    phase2Gui.getJButton2().setBackground(java.awt.Color.RED);
                    break;
                case 3:
                    phase2Gui.getJButton3().setBackground(java.awt.Color.RED);
                    break;
                case 4:
                    phase2Gui.getJButton4().setBackground(java.awt.Color.RED);
                    break;
                case 5:
                    phase2Gui.getJButton5().setBackground(java.awt.Color.RED);
                    break;
                case 6:
                    phase2Gui.getJButton6().setBackground(java.awt.Color.RED);
                    break;
            }
        }
    }
}