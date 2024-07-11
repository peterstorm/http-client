package dk.oister.implementations;

import dk.oister.domain.authentication.AuthToken;
import dk.oister.domain.errors.Forbidden;
import dk.oister.domain.errors.HttpError;
import dk.oister.domain.errors.RetryError;
import dk.oister.domain.errors.Unauthorized;
import dk.oister.interfaces.AuthService;
import dk.oister.interfaces.AuthTokens;
import dk.oister.interfaces.ThrowingLambda;
import dk.oister.util.Either;
import dk.oister.util.Left;
import dk.oister.util.Right;

public class AuthTokensWithRetry implements AuthTokens {

    private AuthService authService;

    public AuthTokensWithRetry(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public <T> Either<HttpError, T> withAuthToken(
        ThrowingLambda<AuthToken, Either<HttpError, T>> function
    ){
        Either<HttpError, T> either = authService
                .retrieveAuthToken()
                .flatMap(token -> function.apply(token));

        return switch (either) {
            case Left(HttpError error) -> switch (error) {
                case Unauthorized(String message, var unused) ->
                    authService
                        .renewAuthToken()
                        .flatMap(token -> retry(function.apply(token), 0, message));
                case Forbidden(String message, var unused) ->
                    authService
                        .renewAuthToken()
                        .flatMap(token -> retry(function.apply(token), 0, message));
                default -> Either.left(error);
            };
            case Right(T value) -> Either.pure(value);

        };
    }

    private <T> Either<HttpError, T> retry(
            Either<HttpError, T> either,
            Integer retries,
            String originalError
    ){
        if (retries > 3) {
            return Either.left(new RetryError("Too many retries: " + originalError));
        } else {
            return switch (either) {
                case Left(HttpError error) -> switch (error) {
                    case Unauthorized(String message,  var unused) ->
                        retry(either, retries + 1, error.toString());
                    case Forbidden(String message, var unused) ->
                        retry(either, retries + 1, error.toString());
                    default -> Either.left(error);
                };
                case Right(T value) -> Either.pure(value);
            };
        }
    }

}
