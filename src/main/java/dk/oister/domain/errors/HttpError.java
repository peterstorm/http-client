package dk.oister.domain.errors;

public sealed interface HttpError
    permits
        Conflict,
        Unauthorized,
        BadRequest,
        Forbidden,
        NotFound,
        InternalServerError,
        ServiceUnavailable,
        UnhandledError,
        UnknownError,
        RetryError
    {}
