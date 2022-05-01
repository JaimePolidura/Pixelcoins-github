package es.serversurvival._shared;

import java.util.HashMap;
import java.util.Map;

public final class DependecyContainer {
    private final Map<Class<?>, Object> dependencies;

    public DependecyContainer(){
        this.dependencies = new HashMap<>();
    }

    public <T> void add(Class<T> depencyClass, T instanceClass){
        this.dependencies.put(depencyClass, instanceClass);
    }

    public <T> T get(Class<T> depencyClass){
        return (T) this.dependencies.get(depencyClass);
    }
}
