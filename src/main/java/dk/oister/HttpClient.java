package dk.oister;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import dk.oister.implementations.SimpleAuthService;
import dk.oister.implementations.SimpleAuthTokens;
import dk.oister.interfaces.AuthService;
import dk.oister.interfaces.AuthTokens;
import dk.oister.interfaces.HttpClientInterface;
import dk.oister.utils.OptionalTypeAdapter;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpClient implements HttpClientInterface {

    private Type errorBody;
    private final Gson gson;
    private final OkHttpClient client;
    private final String baseUrl;
    private final String urlScheme;
    private final Optional<AuthTokens> authTokens;
    private final Optional<AuthService> authService;

    private HttpClient(Builder builder) {
        this.errorBody = builder.errorBody;
        this.baseUrl = builder.baseUrl;
        this.urlScheme = builder.urlScheme;
        this.client = builder.client;
        this.gson = builder.gson;
        this.authTokens = builder.authTokens;
        this.authService = builder.authService;
    }

    @Override
    public <T> T get(String method, Map<String, String> headers, Map<String, String> params, Type type) throws Exception {
        Headers headersFromMap = Headers.of(headers);
        Request.Builder request = new Request
            .Builder()
            .headers(headersFromMap)
            .url(buildUrl(method, params))
            .get();
        authTokens
            .ifPresentOrElse(
                auth -> auth.withAuthToken(token -> request.addHeader("Authorization", "Bearer " + token.token())), 
                null
            );
        return runRequest(request, type);
    }

    @Override
    public <T, U> U post(String method, Map<String, String> headers, Map<String, String> params, T data, Type postType,
            Type returnType) throws IOException, Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'post'");
    }

    @Override
    public <T, U> U put(String method, Map<String, String> headers, Map<String, String> params, T data, Type postType,
            Type returnType) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    @Override
    public <U> U putNoBody(String method, Map<String, String> headers, Map<String, String> params, Type returnType)
            throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'putNoBody'");
    }

    @Override
    public <T> T delete(String method, Map<String, String> headers, Map<String, String> params, Type type)
            throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
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

    private Response checkResponse(Response response, Request request) {
        int responseCode = response.code();
        if (responseCode == 200) {
            return response;
        } else if (responseCode == 201) {
            return response;
        } else {
            return null;
        }
    }

    private <T> T runRequest(Request.Builder requestBuilder, Type type) throws Exception {
        Request request = requestBuilder
                .build();
        System.out.println(request.toString());
        Response response = client
                .newCall(request)
                .execute();

        ResponseBody body = checkResponse(response, request).body();
        JsonReader reader = new JsonReader(new InputStreamReader(body.byteStream()));

        return gson.fromJson(reader, type);

    }

    public static final class Builder {
        String baseUrl;
        String urlScheme;
        Type errorBody;
        OkHttpClient client;
        Gson gson;
        Optional<AuthTokens> authTokens;
        Optional<AuthService> authService;

        public Builder(String baseUrl, Type errorBody) {
            this.errorBody = errorBody;
            this.baseUrl = baseUrl;
            this.urlScheme = "https";
            this.client = new OkHttpClient();
            this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY)
                .create();
            this.authService = Optional.empty();
            this.authTokens = Optional.empty();
        }

        public Builder withSimpleAuth(String apiKey) {
            AuthService authService = new SimpleAuthService(apiKey);
            AuthTokens authTokens = new SimpleAuthTokens(authService);
            this.authService = Optional.of(authService);
            this.authTokens = Optional.of(authTokens);
            return this;
        }

        public Builder withHttpScheme() {
            this.urlScheme = "http";
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this);
        }
    }


}
