package events;

public class DoorEvent {

    public enum DoorState {
        OPEN,
        CLOSED,
        OBSTRUCTION
    }

    private final DoorState state;
    private final int floor;

    public DoorEvent(DoorState state, int floor) {
        this.state = state;
        this.floor = floor;
    }

    public DoorState getState() {
        return state;
    }

    public int getFloor() {
        return floor;
    }
}