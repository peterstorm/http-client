package dk.oister.interfaces;

import dk.oister.domain.authentication.AuthToken;
import dk.oister.domain.errors.HttpError;
import dk.oister.util.Either;
import java.util.function.Function;

public interface AuthTokens {

    public <T> Either<HttpError, T> withAuthToken(Function<AuthToken, Either<HttpError, T>> function);

}
