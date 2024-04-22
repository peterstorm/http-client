package dk.oister.domain.errors;

public record Conflict<E>(String errorMessage, E errorBody) implements HttpError {}