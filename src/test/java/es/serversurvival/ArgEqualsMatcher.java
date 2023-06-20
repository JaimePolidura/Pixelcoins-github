package es.serversurvival;

import org.mockito.ArgumentMatcher;

public final class ArgEqualsMatcher<T> implements ArgumentMatcher<T> {
    private final T toCheck;

    public static <T> ArgEqualsMatcher<T> of(T toCheck){
        return new ArgEqualsMatcher<>(toCheck);
    }

    public ArgEqualsMatcher(T toCheck) {
        this.toCheck = toCheck;
    }

    @Override
    public boolean matches(T other) {
        return toCheck.equals(other);
    }
}
