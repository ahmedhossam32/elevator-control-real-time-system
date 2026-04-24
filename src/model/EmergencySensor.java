package model;

import esper.Config;
import events.EmergencyEvent;

public class EmergencySensor extends Thread {

    private volatile boolean running = true;
    private volatile boolean triggerEmergency = false;

    private final Motor motor;

    public EmergencySensor(Motor motor) {
        this.motor = motor;
    }

    public void triggerEmergency() {
        triggerEmergency = true;
    }

    public void stopSensor() {
        running = false;
    }

    @Override
    public void run() {

        while (running) {

            try {

                if (triggerEmergency) {

                    int currentFloor = motor.getCurrentFloor();

                    System.out.println("🚨 Emergency triggered!");

                    Config.sendEvent(
                            new EmergencyEvent(currentFloor, System.currentTimeMillis())
                    );

                    triggerEmergency = false; 
                }

                Thread.sleep(200);

            } catch (InterruptedException e) {
                running = false;
            }
        }
    }
}