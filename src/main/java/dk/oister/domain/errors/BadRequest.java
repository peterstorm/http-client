package dk.oister.domain.errors;

public record BadRequest<E>(String errorMessage, E errorBody) implements HttpError {

}
