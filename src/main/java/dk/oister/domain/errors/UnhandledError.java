package dk.oister.domain.errors;

public record UnhandledError<E>(String errorMessage, E errorBody) implements HttpError {}
