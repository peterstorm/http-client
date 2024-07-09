package dk.oister.interfaces;

import dk.oister.domain.authentication.AuthToken;
import dk.oister.domain.errors.HttpError;
import dk.oister.util.Either;

public interface AuthService {

    public Either<HttpError, AuthToken> renewAuthToken();

    public AuthToken retrieveAuthToken();

}
