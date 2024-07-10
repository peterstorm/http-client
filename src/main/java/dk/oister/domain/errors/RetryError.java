package dk.oister.domain.errors;

public record RetryError(String errorMessage) implements HttpError{

}
