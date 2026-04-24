package events;

public class CallElevatorEvent {

    private final int sourceFloor;

    public CallElevatorEvent(int sourceFloor) {
        this.sourceFloor = sourceFloor;
    }

    public int getSourceFloor() {
        return sourceFloor;
    }
}