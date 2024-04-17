package dk.oister.domain.errors;

public sealed interface HttpError
    permits
        ConflictErrror,
        Unauthorized,
        BadRequest,
        Forbidden,
        NotFound,
        InternalServerError,
        ServiceUnavailable
    {}
