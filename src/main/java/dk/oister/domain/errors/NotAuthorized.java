package dk.oister.domain.errors;

public record NotAuthorized<E>(String errorMessage, E errorBody) implements HttpError<E> {}
