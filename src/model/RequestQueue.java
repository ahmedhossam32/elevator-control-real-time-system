package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestQueue extends Thread {

    private final BlockingQueue<ElevatorRequest> requests;
    private final ElevatorControlSystem controlSystem;

    private final Object completionLock = new Object();
    private volatile boolean running = true;
    private volatile boolean waitingForCompletion = false;

    public RequestQueue(ElevatorControlSystem controlSystem) {
        this.controlSystem = controlSystem;
        this.requests = new LinkedBlockingQueue<>();
        setName("RequestQueue");
    }

    public void addRequest(ElevatorRequest request) {
        requests.offer(request);
    }

    public void onRequestCompleted() {
        synchronized (completionLock) {
            waitingForCompletion = false;
            completionLock.notifyAll();
        }
    }

    public void cancelCurrentRequest() {
        synchronized (completionLock) {
            waitingForCompletion = false;
            completionLock.notifyAll();
        }
    }

    public void shutdownQueue() {
        running = false;
        interrupt();
        synchronized (completionLock) {
            completionLock.notifyAll();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                ElevatorRequest nextRequest = requests.take();

                waitingForCompletion = true;
                controlSystem.processRequest(nextRequest);

                synchronized (completionLock) {
                    while (waitingForCompletion && running) {
                        completionLock.wait();
                    }
                }

            } catch (InterruptedException e) {
                if (!running) {
                    break;
                }
            }
        }
    }

    public synchronized boolean isEmpty() {
        return requests.isEmpty();
    }

    public synchronized void clearAll() {
        requests.clear();
        System.out.println("🧹 Queue cleared due to emergency");
    }

    public synchronized int size() {
        return requests.size();
    }
}