package es.serversurvival.v1;

import org.mockito.ArgumentMatcher;

public final class MockitoArgEqualsMatcher<T> implements ArgumentMatcher<T> {
    private final T toCheck;

    public static <T> MockitoArgEqualsMatcher<T> of(T toCheck){
        return new MockitoArgEqualsMatcher<>(toCheck);
    }

    public MockitoArgEqualsMatcher(T toCheck) {
        this.toCheck = toCheck;
    }

    @Override
    public boolean matches(T other) {
        return toCheck.equals(other);
    }
}
