package dk.oister.implementations;

import dk.oister.domain.authentication.AuthToken;
import dk.oister.interfaces.AuthService;
import dk.oister.interfaces.AuthTokens;
import dk.oister.interfaces.ThrowingLambda;

public class SimpleAuthTokens implements AuthTokens {

    private AuthService authService;

    public SimpleAuthTokens(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public <T> T withAuthToken(ThrowingLambda<AuthToken, T> function) {
        AuthToken authToken = authService.retrieveAuthToken();
        return function.apply(authToken);
    }

}
