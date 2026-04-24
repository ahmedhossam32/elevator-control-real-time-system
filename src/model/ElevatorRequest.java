package model;

public class ElevatorRequest {

    private final String requestId;
    private final int sourceFloor;
    private final int destinationFloor;
    private final long requestTime;
    private final String tokenId;
    private String status;

    public ElevatorRequest(String requestId, int sourceFloor, int destinationFloor, long requestTime, String tokenId) {
        this.requestId = requestId;
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
        this.requestTime = requestTime;
        this.tokenId = tokenId;
        this.status = "PENDING";
    }

    public String getRequestId() {
        return requestId;
    }

    public int getSourceFloor() {
        return sourceFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}