package events;

public class FloorReachedEvent {

    private final int floor;

    public FloorReachedEvent(int floor) {
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }
}