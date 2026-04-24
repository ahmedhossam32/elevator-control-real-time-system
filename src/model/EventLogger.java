package model;

import view.ElevatorPhase1View;
import view.ElevatorPhase2View;

public class EventLogger {

    private final ElevatorPhase1View phase1Gui;
    private final ElevatorPhase2View phase2Gui;

    public EventLogger(ElevatorPhase1View phase1Gui, ElevatorPhase2View phase2Gui) {
        this.phase1Gui = phase1Gui;
        this.phase2Gui = phase2Gui;
    }

    public void log(String message) {
        System.out.println("[EVENT LOG]: " + message);

        if (phase1Gui != null) {
            phase1Gui.setStatus(message);
        }

        if (phase2Gui != null) {
            phase2Gui.getJTextField2().setText(message);
        }
    }
}
