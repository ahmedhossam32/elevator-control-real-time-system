package events;

public class AuthEvent {

    public enum AuthResult {
        SUCCESS,
        FAILURE
    }

    private final AuthResult result;
    private final String tokenId;
    private final int destinationFloor;
    private final long timestamp;

    public AuthEvent(AuthResult result, String tokenId, int destinationFloor, long timestamp) {
        this.result = result;
        this.tokenId = tokenId;
        this.destinationFloor = destinationFloor;
        this.timestamp = timestamp;
    }

    public AuthResult getResult() {
        return result;
    }

    public String getTokenId() {
        return tokenId;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public long getTimestamp() {
        return timestamp;
    }
}