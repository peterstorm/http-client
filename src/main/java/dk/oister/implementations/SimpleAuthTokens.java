package dk.oister.implementations;

import dk.oister.domain.authentication.AuthToken;
import dk.oister.domain.errors.HttpError;
import dk.oister.interfaces.AuthService;
import dk.oister.interfaces.AuthTokens;
import dk.oister.interfaces.ThrowingLambda;
import dk.oister.util.Either;

public class SimpleAuthTokens implements AuthTokens {

    private AuthService authService;

    public SimpleAuthTokens(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public <T>  Either<HttpError, T> withAuthToken(ThrowingLambda<AuthToken, Either<HttpError, T>> function) {
      return authService
        .retrieveAuthToken()
        .flatMap(token -> function .apply(token));
    }

}
