package dk.oister.domain.errors;

public record Forbidden<E>(String errorMessage, E errorBody) implements HttpError {}
