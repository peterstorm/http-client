package dk.oister.implementations;

import java.util.Map;

import dk.oister.domain.authentication.AuthToken;
import dk.oister.domain.errors.HttpError;
import dk.oister.interfaces.AuthClient;
import dk.oister.interfaces.AuthService;
import dk.oister.util.Either;
import dk.oister.util.Left;
import dk.oister.util.Right;

public class AuthServiceWithSimpleAuthClient<T, E> implements AuthService {

  private AuthToken authToken;
  private final AuthClient<T> authClient;
  private final T credentials;

  public AuthServiceWithSimpleAuthClient(
      String baseUrl,
      String method,
      Map<String, String> headers,
      T credentials,
      Class<E> error
  ){
      this.authToken = new AuthToken("renewMe");
      this.authClient = new SimpleAuthClient<>(baseUrl, method, headers, error);
      this.credentials = credentials;
  }

  @Override
  public Either<HttpError, AuthToken> renewAuthToken() {
      return switch (authClient.renewAuthToken(credentials)) {
          case Left(HttpError error) -> Either.left(error);
          case Right(AuthToken token) -> setAndReturnAuthToken(token);
      };
  }

  @Override
  public Either<HttpError, AuthToken> retrieveAuthToken() {
      return Either.pure(this.authToken);
  }

  private Either<HttpError, AuthToken> setAndReturnAuthToken(AuthToken token) {
      this.authToken = token;
      return Either.pure(token);
  }

}
