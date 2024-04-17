package dk.oister.domain.errors;

public record NotFound<E>(String errorMessage, E errorBody) implements HttpError {}
