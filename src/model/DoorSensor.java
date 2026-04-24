package model;

import esper.Config;
import events.DoorEvent;

public class DoorSensor extends Thread {

    private volatile boolean running = true;
    private final Door door;
    private volatile boolean doorClosing = false;
    private volatile boolean obstructionDetected = false;

    public DoorSensor(Door door) {
        this.door = door;
        setName("DoorSensor");
    }

    public void stopSensor() {
        running = false;
    }

    public void setDoorClosing(boolean closing) {
        doorClosing = closing;
    }

    
    @Override
    public void run() {
        while (running) {
           try {
                /*
                
                if (doorClosing && !obstructionDetected) {

                    boolean randomObstruction = Math.random() < 0.2;

                    if (randomObstruction) {

                        obstructionDetected = true;

                        System.out.println("DoorSensor: Obstruction detected!");

                        Config.sendEvent(
                                new DoorEvent(
                                        DoorEvent.DoorState.OBSTRUCTION,
                                        door.getCurrentFloor()
                                )
                        );
                    }
                }
*/
                
                Thread.sleep(100);

            } catch (InterruptedException e) {
                e.printStackTrace();
                running = false;
            }
        }
                
}


    public boolean hasObstruction() {
        return obstructionDetected;
    }

    public void clearObstruction() {
        obstructionDetected = false;
    }
}