package events;

public class FloorSensorEvent {

    private final int currentFloor;

    public FloorSensorEvent(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }
}