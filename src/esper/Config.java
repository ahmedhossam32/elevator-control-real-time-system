package esper;

import com.espertech.esper.client.*;
import events.*;

public class Config {

    private static final EPServiceProvider engine = EPServiceProviderManager.getDefaultProvider();

    public static void registerEvents() {

        engine.getEPAdministrator().getConfiguration().addEventType(AccessRequestEvent.class);
        engine.getEPAdministrator().getConfiguration().addEventType(AuthEvent.class);
        engine.getEPAdministrator().getConfiguration().addEventType(MotorEvent.class);
        engine.getEPAdministrator().getConfiguration().addEventType(FloorSensorEvent.class);
        engine.getEPAdministrator().getConfiguration().addEventType(FloorReachedEvent.class); // 🔥 ADDED
        engine.getEPAdministrator().getConfiguration().addEventType(DoorEvent.class);
        engine.getEPAdministrator().getConfiguration().addEventType(EmergencyEvent.class);
        engine.getEPAdministrator().getConfiguration().addEventType(CallElevatorEvent.class);

        System.out.println("Events Registered");
    }

    public static EPStatement createStatement(String epl) {
        return engine.getEPAdministrator().createEPL(epl);
    }

    public static void sendEvent(Object event) {
        engine.getEPRuntime().sendEvent(event);
    }
}