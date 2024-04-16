

import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.reflect.TypeToken;

import dk.oister.HttpClient;
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

        HttpClient client = new HttpClient
            .Builder("dummyapi.online", null)
            .withSimpleAuth("awefawefawfwf")
            .build();

        try {
            ArrayList<Todo> result = client.get("api/todos", Collections.emptyMap(), Collections.emptyMap(), listType);
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
