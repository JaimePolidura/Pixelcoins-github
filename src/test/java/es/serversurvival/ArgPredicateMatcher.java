package es.serversurvival;

import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.function.Predicate;

@AllArgsConstructor
public final class ArgPredicateMatcher<T> implements ArgumentMatcher<T> {
    private final Predicate<T> predicate;

    @Override
    public boolean matches(T t) {
        return predicate.test(t);
    }

    public static <T> ArgPredicateMatcher<T> of(Predicate<T> matcher){
        return new ArgPredicateMatcher<>(matcher);
    }
}
