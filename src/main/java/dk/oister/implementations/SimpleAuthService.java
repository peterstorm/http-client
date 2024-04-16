package dk.oister.implementations;

import dk.oister.domain.authentication.AuthToken;
import dk.oister.interfaces.AuthService;

public class SimpleAuthService implements AuthService {

    private String apiKey;

    public SimpleAuthService(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public AuthToken renewAuthToken() {
        return new AuthToken(this.apiKey);
    }

    @Override
    public AuthToken retrieveAuthToken() {
        return new AuthToken(this.apiKey);
    }

}
