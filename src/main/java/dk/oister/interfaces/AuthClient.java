package dk.oister.interfaces;

import dk.oister.domain.authentication.AuthToken;
import dk.oister.domain.errors.HttpError;
import dk.oister.util.Either;

public interface AuthClient<T> {

    public Either<HttpError, AuthToken> renewAuthToken(T credentials) throws Exception;

}
