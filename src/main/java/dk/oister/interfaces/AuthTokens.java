package dk.oister.interfaces;

import dk.oister.domain.authentication.AuthToken;
import dk.oister.domain.errors.HttpError;
import dk.oister.util.Either;

public interface AuthTokens {

    public <T> Either<HttpError, T> withAuthToken(ThrowingLambda<AuthToken, Either<HttpError, T>> function);

}
