package model;

import esper.Config;
import events.DoorEvent;

public class Door {

    private final ElevatorControlSystem controlSystem;
    private DoorSensor doorSensor;

    public Door(ElevatorControlSystem controlSystem) {
        this.controlSystem = controlSystem;
    }

    public void setDoorSensor(DoorSensor doorSensor) {
        this.doorSensor = doorSensor;
    }

    public int getCurrentFloor() {
        return controlSystem.getMotor().getCurrentFloor();
    }

    public void openDoor(int floor) {

        controlSystem.getGUI().setDoorState("OPEN");
        controlSystem.getEventLogger().log("Door opened at floor " + floor);

        Config.sendEvent(new DoorEvent(DoorEvent.DoorState.OPEN, floor));

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                closeDoor(floor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void closeDoor(int floor) {

        new Thread(() -> {

            if (doorSensor != null) {
                doorSensor.setDoorClosing(true);
            }

            controlSystem.getGUI().setDoorState("CLOSING");
            controlSystem.getEventLogger().log("Door closing...");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}

            Config.sendEvent(new DoorEvent(DoorEvent.DoorState.CLOSED, floor));

            if (doorSensor != null) {
                doorSensor.setDoorClosing(false);
            }
            controlSystem.onDoorClosed();

        }).start();
    }

    public void handleObstructionFromSensor(int floor) {

        controlSystem.getGUI().setDoorState("OBSTRUCTION - OPENING");
        controlSystem.getEventLogger().log("Obstruction detected! Reopening door...");

        Config.sendEvent(new DoorEvent(DoorEvent.DoorState.OPEN, floor));

        new Thread(() -> {
            try {
                Thread.sleep(2000);

                if (doorSensor != null) {
                    doorSensor.clearObstruction();  
                }

                controlSystem.getEventLogger().log("Attempting to close door again...");
                closeDoor(floor);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public void emergencyOpen(int floor) {

        controlSystem.getGUI().setDoorState("OPEN (EMERGENCY)");
        controlSystem.getEventLogger().log("EMERGENCY DOOR OPEN");

        Config.sendEvent(new DoorEvent(DoorEvent.DoorState.OPEN, floor));
    }
}