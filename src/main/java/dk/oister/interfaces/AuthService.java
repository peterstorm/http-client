package dk.oister.interfaces;

import dk.oister.domain.authentication.AuthToken;

public interface AuthService {

    public AuthToken renewAuthToken();

    public AuthToken retrieveAuthToken();

}
