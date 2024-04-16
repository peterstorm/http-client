package dk.oister;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public interface HttpClientInterface {

    public <T> T get(
        String method,
        Map<String, String> headers,
        Map<String, String> params, 
        Type type
    ) throws IOException, Exception;

    public <T, U> U post(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        T data, 
        Type postType, 
        Type returnType
    ) throws IOException, Exception;

    public <T, U> U put(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        T data, 
        Type postType, 
        Type returnType
    ) throws Exception;

    public <U> U putNoBody(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        Type returnType
    ) throws Exception;

    public <T> T delete(
        String method, 
        Map<String, String> headers, 
        Map<String, String> params, 
        Type type
    ) throws Exception;

}
