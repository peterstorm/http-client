

import java.util.Collections;
import java.util.List;

import dk.oister.HttpClient;
import dk.oister.domain.errors.HttpError;
import dk.oister.implementations.AuthServiceWithSimpleAuthClient;
import dk.oister.util.Either;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        record Todo(int id, String todo, Boolean completed, int userId) {};
        record Todos(List<Todo> todos) {};
        record Error(String message) {};
        record Credentials(String username, String password, int expiresIn) {};
        Credentials credentials = new Credentials("kminchelle", "0lelplR", 30);

        HttpClient<Error> client = new HttpClient
            .Builder<Error>("dummyjson.com", Error.class)
            .build();

        AuthServiceWithSimpleAuthClient<Credentials, Error> authService =
            new AuthServiceWithSimpleAuthClient<>(
                "dummyjson.com",
                "auth/login", 
                Collections.emptyMap(), 
                credentials,
                Error.class
            );

        try {
            Either<HttpError, List<Todo>> result = 
                client.get(
                    "todos", 
                    Collections.emptyMap(), 
                    Collections.emptyMap(), 
                    Todos.class
                );
            System.out.println(authService.renewAuthToken());
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
