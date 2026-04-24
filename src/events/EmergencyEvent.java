package events;

public class EmergencyEvent {

    private final int currentFloor;
    private final long timestamp;

    public EmergencyEvent(int currentFloor, long timestamp) {
        this.currentFloor = currentFloor;
        this.timestamp = timestamp;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public long getTimestamp() {
        return timestamp;
    }
}