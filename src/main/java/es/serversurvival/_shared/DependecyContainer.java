package es.serversurvival._shared;

import java.util.HashMap;
import java.util.Map;

public final class DependecyContainer {
    private static final Map<Class<?>, Object> dependencies;

    static {
        dependencies = new HashMap<>();
    }

    public static <T> void add(Class<T> depencyClass, T instanceClass){
        dependencies.put(depencyClass, instanceClass);
    }

    public static <T> void addAll(Map<Class<?>, Object> dependenciesToAdd){
        dependencies.putAll(dependenciesToAdd);
    }

    public static <T> T get(Class<T> depencyClass){
        return (T) dependencies.get(depencyClass);
    }
}
