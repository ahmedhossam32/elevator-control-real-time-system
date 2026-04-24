package model;

import esper.Config;
import events.AccessRequestEvent;

public class KeyReaderSensor {

    public void simulateAccessRequest(String tokenId, int sourceFloor, int destinationFloor) {
        Config.sendEvent(
                new AccessRequestEvent(tokenId, sourceFloor, destinationFloor)
        );
    }
}