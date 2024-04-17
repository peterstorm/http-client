package dk.oister.domain.errors;

public record Unauthorized<E>(String errorMessage, E errorBody) implements HttpError {}
