package model;

import esper.Config;
import events.FloorSensorEvent;

public class FloorSensor extends Thread {

    private volatile boolean running = true;
    private final Motor motor;
    private int lastFloor = -1;

    public FloorSensor(Motor motor) {
        this.motor = motor;
        setName("FloorSensor");
    }

    public void stopSensor() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                int currentFloor = motor.getCurrentFloor();
                
                if (currentFloor != lastFloor) {
                    System.out.println("FloorSensor: Floor changed from " + lastFloor + " to " + currentFloor);
                    
                    Config.sendEvent(new FloorSensorEvent(currentFloor));
                    lastFloor = currentFloor;
                }

                Thread.sleep(500); 

            } catch (InterruptedException e) {
                e.printStackTrace();
                running = false;
            }
        }
    }
}
