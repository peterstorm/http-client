package dk.oister.domain.errors;

public record ServiceUnavailable<E>(String errorMessage, E errorBody) implements HttpError {}
