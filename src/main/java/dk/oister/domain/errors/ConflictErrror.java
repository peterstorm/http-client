package dk.oister.domain.errors;

public record ConflictErrror<E>(String errorMessage, E errorBody) implements HttpError<E> {}