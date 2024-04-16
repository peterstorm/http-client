package dk.oister.interfaces;

import dk.oister.domain.authentication.AuthToken;

public interface AuthTokens {
    
    public <T> T withAuthToken(ThrowingLambda<AuthToken, T> function);

}
