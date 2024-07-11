package dk.oister.implementations;

import dk.oister.domain.authentication.AuthToken;
import dk.oister.domain.errors.HttpError;
import dk.oister.interfaces.AuthService;
import dk.oister.util.Either;

public class SimpleAuthService implements AuthService {

    private String apiKey;

    public SimpleAuthService(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Either<HttpError, AuthToken> renewAuthToken() {
        return Either.pure(new AuthToken(this.apiKey));
    }

    @Override
    public Either<HttpError, AuthToken> retrieveAuthToken() {
        return Either.pure(new AuthToken(this.apiKey));
    }

}
