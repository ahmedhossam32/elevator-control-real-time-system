package model;

import java.util.HashSet;
import java.util.Set;

public class AccessControlService {

    private final Set<String> validTokens;

    public AccessControlService() {
        validTokens = new HashSet<>();
        validTokens.add("999");
    }
    
    public boolean authenticate(String tokenId) {
        return validTokens.contains(tokenId);
    }
}