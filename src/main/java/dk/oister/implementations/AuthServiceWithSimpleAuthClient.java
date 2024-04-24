package dk.oister.implementations;

import java.util.Map;

import dk.oister.domain.authentication.AuthToken;
import dk.oister.domain.errors.HttpError;
import dk.oister.interfaces.AuthClient;
import dk.oister.interfaces.AuthService;
import dk.oister.util.Either;

public class AuthServiceWithSimpleAuthClient<T, E> implements AuthService {

    private final AuthToken authToken;
    private final AuthClient<T> authClient;
    private final T credentials;

    public AuthServiceWithSimpleAuthClient(
        String baseUrl,
        String method,
        Map<String, String> headers,
        T credentials,
        Class<E> error
    ) {
        this.authToken = new AuthToken("renewMe");
        this.authClient = new SimpleAuthClient<>(baseUrl, method, headers, error);
        this.credentials = credentials;
    }

    @Override
    public Either<HttpError, AuthToken> renewAuthToken() throws Exception {
        return authClient.renewAuthToken(credentials);

    }

    @Override
    public AuthToken retrieveAuthToken() {
        return this.authToken;
    }

}
