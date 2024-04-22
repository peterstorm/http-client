package dk.oister;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import dk.oister.domain.errors.BadRequest;
import dk.oister.domain.errors.Conflict;
import dk.oister.domain.errors.Forbidden;
import dk.oister.domain.errors.HttpError;
import dk.oister.domain.errors.InternalServerError;
import dk.oister.domain.errors.NotFound;
import dk.oister.domain.errors.ServiceUnavailable;
import dk.oister.domain.errors.Unauthorized;
import dk.oister.implementations.SimpleAuthService;
import dk.oister.implementations.SimpleAuthTokens;
import dk.oister.interfaces.AuthService;
import dk.oister.interfaces.AuthTokens;
import dk.oister.interfaces.HttpClientInterface;
import dk.oister.util.Either;
import dk.oister.util.Left;
import dk.oister.util.Right;
import dk.oister.utils.OptionalTypeAdapter;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpClient<E> implements HttpClientInterface {

    private Class<E> errorBody;
    private final Gson gson;
    private final OkHttpClient client;
    private final String baseUrl;
    private final String urlScheme;
    private final String authScheme;
    private final Optional<AuthTokens> authTokens;

    private HttpClient(Builder<E> builder) {
        this.errorBody = builder.errorBody;
        this.baseUrl = builder.baseUrl;
        this.urlScheme = builder.urlScheme;
        this.authScheme = builder.authScheme;
        this.client = builder.client;
        this.gson = builder.gson;
        this.authTokens = builder.authTokens;
    }

    @Override
    public <T> Either<HttpError, T> get(
        String method, 
        Map<String, String> headers,
        Map<String, String> params, 
        Type type
    ) throws Exception {

        Headers headersFromMap = Headers.of(headers);
        Request.Builder request = new Request
            .Builder()
            .headers(headersFromMap)
            .url(buildUrl(method, params))
            .get();
        
        return runRequest(
            addAuthTokenIfPresent(request, authTokens, authScheme), 
            type
        );
    }

    @Override
    public <T, U> Either<HttpError, U> post(
        String method, 
        Map<String, 
        String> headers, 
        Map<String, String> params, 
        T data, 
        Type postType,
        Type returnType
    ) throws Exception {

        Headers headersFromMap = Headers.of(headers);
        String body = gson.toJson(data, postType);
        Request.Builder request = new Request
            .Builder()
            .headers(headersFromMap)
            .url(buildUrl(method, params))
            .post(RequestBody.create(body, MediaType.parse("application/json")));
        
        return runRequest(
            addAuthTokenIfPresent(request, authTokens, authScheme), 
            returnType
        );
    }

    @Override
    public <T, U> Either<HttpError, U> put(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        T data, 
        Type postType,
        Type returnType
    ) throws Exception {

        Headers headersFromMap = Headers.of(headers);
        String body = gson.toJson(data, postType);
        Request.Builder request = new Request
            .Builder()
            .headers(headersFromMap)
            .url(buildUrl(method, params))
            .put(RequestBody.create(body, MediaType.parse("application/json")));

        return runRequest(
            addAuthTokenIfPresent(request, authTokens, authScheme), 
            returnType
        );
    }

    @Override
    public <U> Either<HttpError, U> putNoBody(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        Type returnType
    ) throws Exception {

        Headers headersFromMap = Headers.of(headers);
        String body = gson.toJson("");
        Request.Builder request = new Request.Builder()
            .headers(headersFromMap)
            .addHeader("Content-Length", "0")
            .url(buildUrl(method, params))
            .put(RequestBody.create(body, MediaType.parse("application/json")));

        return runRequest(
            addAuthTokenIfPresent(request, authTokens, authScheme), 
            returnType
        );
    }

    @Override
    public <T> Either<HttpError, T> delete(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        Type type
    ) throws Exception {

        Headers headersFromMap = Headers.of(headers);
        Request.Builder request = new Request
            .Builder()
            .headers(headersFromMap)
            .url(buildUrl(method, params))
            .delete();
        return runRequest(
            addAuthTokenIfPresent(request, authTokens, authScheme), 
            type
        );
    }

    private Request.Builder addAuthTokenIfPresent(
        Request.Builder request,
        Optional<AuthTokens> authTokens,
        String authScheme
    ) {
        Request.Builder req = request;
        authTokens
            .ifPresent(
                auth -> auth.withAuthToken(token -> req.addHeader("Authorization", authScheme + " " + token.token()))
            );
        return req;
    }

    private HttpUrl buildUrl(String method, Map<String, String> params) {
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder();
        urlBuilder.scheme(this.urlScheme);
        urlBuilder.host(this.baseUrl);
        urlBuilder.addPathSegments(method);
        params.forEach((k, v) -> urlBuilder.addQueryParameter(k, v));
        HttpUrl url = urlBuilder.build();
        return url;
    }

    private Either<HttpError, JsonReader> checkResponse (Response response, Request request) {
        int responseCode = response.code();
        JsonReader reader = new JsonReader(new InputStreamReader(response.body().byteStream()));
        if (responseCode == 200) {
            return Either.pure(reader);
        } else if (responseCode == 201) {
            return Either.pure(reader);
        } else {
            E error = gson.fromJson(reader, errorBody);
            String errorMessage = "Request {{ " +  request.toString() + " }} failed with " + response.code() + " and ";
            if (responseCode == 400) {
                return Either.left(new BadRequest<>(errorMessage, error));
            } else if (responseCode == 401) {
                return Either.left(new Unauthorized<>(errorMessage, error));
            } else if (responseCode == 403) {
                return Either.left(new Forbidden<>(errorMessage, error));
            } else if (responseCode == 404) {
                return Either.left(new NotFound<>(errorMessage, error));
            } else if (responseCode == 409) {
                return Either.left(new Conflict<>(errorMessage, error));
            } else if (responseCode == 500) {
                return Either.left(new InternalServerError<>(errorMessage, error));
            } else if (responseCode == 503) {
                return Either.left(new ServiceUnavailable<>(errorMessage, error));
            }
            System.err.println("UNHANDLED HTTP ERROR, ADD IT IN THE LIBRARY" + responseCode);
            return null;
        }
    }

    private <T> Either<HttpError, T> runRequest(Request.Builder requestBuilder, Type type) throws Exception {
        Request request = requestBuilder
                .build();
        System.out.println(request);
        Response response = client
                .newCall(request)
                .execute();
        
        return checkResponse(response, request)
            .map(reader -> gson.fromJson(reader, type));

    }

    public static final class Builder<E> {
        String baseUrl;
        String urlScheme;
        String authScheme;
        Class<E> errorBody;
        OkHttpClient client;
        Gson gson;
        Optional<AuthTokens> authTokens;

        public Builder(String baseUrl, Class<E> errorBody) {
            this.errorBody = errorBody;
            this.baseUrl = baseUrl;
            this.urlScheme = "https";
            this.authScheme = "Bearer";
            this.client = new OkHttpClient();
            this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY)
                .create();
            this.authTokens = Optional.empty();
        }

        public Builder<E> withSimpleAuth(String apiKey) {
            AuthService authService = new SimpleAuthService(apiKey);
            AuthTokens authTokens = new SimpleAuthTokens(authService);
            this.authTokens = Optional.of(authTokens);
            return this;
        }

        public Builder<E> withCustomAuth(AuthTokens authTokens) {
            this.authTokens = Optional.of(authTokens);
            return this;
        }

        public Builder<E> withCustomAuthScheme(String authScheme) {
            this.authScheme = authScheme;
            return this;
        }

        public Builder<E> withHttpScheme() {
            this.urlScheme = "http";
            return this;
        }

        public HttpClient<E> build() {
            return new HttpClient<E>(this);
        }
    }


}
