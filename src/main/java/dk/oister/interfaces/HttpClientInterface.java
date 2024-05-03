package dk.oister.interfaces;

import java.lang.reflect.Type;
import java.util.Map;

import dk.oister.domain.errors.HttpError;
import dk.oister.util.Either;

public interface HttpClientInterface {

    public <T> Either<HttpError, T> get(
        String method,
        Map<String, String> headers,
        Map<String, String> params, 
        Type type
    );

    public <T, U> Either<HttpError, U> post(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        T data, 
        Type postType, 
        Type returnType
    );

    public <T, U> Either<HttpError, U> put(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        T data, 
        Type postType, 
        Type returnType
    );

    public <U> Either<HttpError, U> putNoBody(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        Type returnType
    );

    public <T> Either<HttpError, T> delete(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        Type type
    );

}
