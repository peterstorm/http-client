import java.lang.Runtime.Version;
import java.util.Collections;
import java.util.List;

import dk.oister.HttpClient;
import dk.oister.domain.errors.HttpError;
import dk.oister.util.Either;

public class App
{
    public static void main( String[] args )
    {
        record Todo(int id, String todo, Boolean completed, int userId) {};
        record Todos(List<Todo> todos) {};
        record Error(String message) {};
        record Credentials(String username, String password, int expiresIn) {};
        record User(int id, String username, String email) {};
        Credentials credentials = new Credentials("emilys", "emilyspass", 30);

        HttpClient<Error> noAuthclient = new HttpClient
            .Builder<Credentials, Error>("dummyjson.com", Error.class)
            .withRetryAuth(
                "dummyjson.com",
                "auth/login",
                Collections.emptyMap(),
                credentials,
                Error.class
            )
            .build();

        Either<HttpError, List<Todo>> result = 
            noAuthclient.get(
                "todos",
                Collections.emptyMap(),
                Collections.emptyMap(),
                Todos.class
            );
        Either<HttpError, User> auth = 
            noAuthclient.get(
                "auth/me",
                Collections.emptyMap(),
                Collections.emptyMap(),
                User.class
            );
        System.out.println(result);
        System.out.println(auth);
        Version javaVersion = Runtime.version();
        System.out.println(javaVersion);
    }
}
