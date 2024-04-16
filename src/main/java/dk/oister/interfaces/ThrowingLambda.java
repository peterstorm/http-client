package dk.oister.interfaces;

@FunctionalInterface
public interface ThrowingLambda<T, U> {
    U apply(T t);
}
