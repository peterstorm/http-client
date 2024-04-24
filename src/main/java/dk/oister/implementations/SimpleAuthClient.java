package dk.oister.implementations;

import java.util.Collections;
import java.util.Map;

import dk.oister.HttpClient;
import dk.oister.domain.authentication.AuthToken;
import dk.oister.domain.errors.HttpError;
import dk.oister.interfaces.AuthClient;
import dk.oister.util.Either;

public class SimpleAuthClient<T, E> implements AuthClient<T> {

    private final HttpClient<E> client;
    private final String method;
    private final Map<String, String> headers;

    public SimpleAuthClient(
        String baseUrl,
        String method,
        Map<String, String> headers,
        Class<E> error
    ) {
        this.client = new HttpClient.Builder<E>(baseUrl, error).build();
        this.method = method;
        this.headers = headers;
    }

    @Override
    public Either<HttpError, AuthToken> renewAuthToken(T credentials) throws Exception {
        return client
            .post(
                this.method, 
                headers,
                Collections.emptyMap(), 
                credentials, 
                credentials.getClass(), 
                AuthToken.class
            );
    }


}
