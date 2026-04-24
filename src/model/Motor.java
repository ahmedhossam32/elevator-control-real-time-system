package model;

import esper.Config;
import events.*;

public class Motor extends Thread {

    private int currentFloor = 0;
    private int targetFloor = 0;
    private MotorEvent.Direction direction = MotorEvent.Direction.STOP;

    private volatile boolean running = false;

    private ElevatorControlSystem controlSystem;

    public Motor(ElevatorControlSystem controlSystem) {
        this.controlSystem = controlSystem;
    }
    public int getCurrentFloor() {
        return currentFloor;
    }

    public synchronized void moveTo(int targetFloor) {

        this.targetFloor = targetFloor;

        if (targetFloor > currentFloor) direction = MotorEvent.Direction.UP;
        else if (targetFloor < currentFloor) direction = MotorEvent.Direction.DOWN;
        else direction = MotorEvent.Direction.STOP;

        running = true;

        controlSystem.getEventLogger().log(
                "Elevator moving " + direction + " to floor " + targetFloor
        );

        Config.sendEvent(new MotorEvent(direction, targetFloor));
    }

    public synchronized void emergencyStop() {
        running = false;
        direction = MotorEvent.Direction.STOP;
    }

   @Override
   public void run() {

    while (true) {

        if (running) {

            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            if (direction == MotorEvent.Direction.UP) currentFloor++;
            else if (direction == MotorEvent.Direction.DOWN) currentFloor--;

           
            if (currentFloor == targetFloor) {

                running = false;
                direction = MotorEvent.Direction.STOP;

                Config.sendEvent(new FloorReachedEvent(currentFloor));
            }

        } else {
            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }
    }
}
}