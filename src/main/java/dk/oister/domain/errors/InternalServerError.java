package dk.oister.domain.errors;

public record InternalServerError<E>(String errorMessage, E errorBody) implements HttpError {}
