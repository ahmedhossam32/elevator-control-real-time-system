package events;

public class MotorEvent {

    public enum Direction {
        UP,
        DOWN,
        STOP
    }

    private final Direction direction;
    private final int targetFloor;

    public MotorEvent(Direction direction, int targetFloor) {
        this.direction = direction;
        this.targetFloor = targetFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getTargetFloor() {
        return targetFloor;
    }
}