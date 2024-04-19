package dk.oister.interfaces;

import java.io.IOException;
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
    ) throws IOException, Exception;

    public <T, U> Either<HttpError, U> post(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        T data, 
        Type postType, 
        Type returnType
    ) throws IOException, Exception;

    public <T, U> Either<HttpError, U> put(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        T data, 
        Type postType, 
        Type returnType
    ) throws Exception;

    public <U> Either<HttpError, U> putNoBody(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        Type returnType
    ) throws Exception;

    public <T> Either<HttpError, T> delete(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        Type type
    ) throws Exception;

}
