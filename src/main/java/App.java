

import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.reflect.TypeToken;

import dk.oister.HttpClient;
import dk.oister.domain.errors.HttpError;
import dk.oister.util.Either;

import java.lang.reflect.Type;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        record Todo(int id, String title, Boolean completed, String priority) {};
        Type listType = new TypeToken<ArrayList<Todo>>(){}.getType();
        record Error(String message) {};

        HttpClient<Error> client = new HttpClient
            .Builder<Error>("dummyapi.online", Error.class)
            .build();

        try {
            Either<HttpError, ArrayList<Todo>> result = client.get("api/todos/awefawef", Collections.emptyMap(), Collections.emptyMap(), listType);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
