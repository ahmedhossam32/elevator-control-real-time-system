package esper;

import model.*;

public class Main {

    public static void main(String[] args) {

        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");

        Config.registerEvents();

        final ElevatorControlSystem controlSystem = new ElevatorControlSystem();

        // CALL
        Config.createStatement(
                "select sourceFloor from CallElevatorEvent"
        ).setSubscriber(new Object() {
            public void update(int sourceFloor) {
                controlSystem.handleCallElevator(sourceFloor);
            }
        });

        // ARRIVAL
        Config.createStatement(
                "select floor from FloorReachedEvent"
        ).setSubscriber(new Object() {
            public void update(int floor) {
                controlSystem.arriveAtFloor(floor);
            }
        });
        
        // EMERGENCY
        Config.createStatement(
                "select currentFloor from EmergencyEvent"
        ).setSubscriber(new Object() {
            public void update(int currentFloor) {
                controlSystem.handleEmergency(currentFloor);
            }
        });

        // ACCESS REQUEST
        Config.createStatement(
                "select tokenId, sourceFloor, destinationFloor, timestamp from AccessRequestEvent"
        ).setSubscriber(new Object() {
            public void update(String tokenId, int sourceFloor, int destinationFloor, long timestamp) {
                controlSystem.handleAccessRequest(tokenId, sourceFloor, destinationFloor, timestamp);
            }
        });

        // AUTH
        Config.createStatement(
                "select result, tokenId, destinationFloor, timestamp from AuthEvent"
        ).setSubscriber(new Object() {
            public void update(events.AuthEvent.AuthResult result, String tokenId, int destinationFloor, long timestamp) {
                if (result == events.AuthEvent.AuthResult.SUCCESS) {
                    controlSystem.handleAuthorizedRequest(tokenId, destinationFloor);
                } else {
                    controlSystem.handleFailedAuth(destinationFloor);
                }
            }
        });
        
        // FLOOR SENSOR
        Config.createStatement(
                "select currentFloor from FloorSensorEvent"
        ).setSubscriber(new Object() {
            public void update(int currentFloor) {
                controlSystem.handleFloorSensorUpdate(currentFloor);
            }
        });

        /*
 DOOR OBSTRUCTION
Config.createStatement(
        "select state, floor from DoorEvent"
).setSubscriber(new Object() {
    public void update(events.DoorEvent.DoorState state, int floor) {
        if (state == events.DoorEvent.DoorState.OBSTRUCTION) {
            controlSystem.handleDoorObstruction(floor);
        }
    }
});
*/
        
    }
}