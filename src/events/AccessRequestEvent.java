package events;

import java.util.Date;

public class AccessRequestEvent {

    private final String tokenId;
    private final int sourceFloor;
    private final int destinationFloor;
    private final long timestamp;

    public AccessRequestEvent(String tokenId, int sourceFloor, int destinationFloor) {
        this.tokenId = tokenId;
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
        this.timestamp = new Date().getTime();
    }

    public String getTokenId() {
        return tokenId;
    }

    public int getSourceFloor() {
        return sourceFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public long getTimestamp() {
        return timestamp;
    }
}