package dk.oister.domain.errors;

public record UnknownError(String errorMessage) implements HttpError{

}
