package dk.oister.domain.errors;

public sealed interface HttpError<E> permits ConflictErrror, NotAuthorized{

}
