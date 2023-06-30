package es.serversurvival._shared.utils;

import es.dependencyinjector.dependencies.annotations.Configuration;
import es.dependencyinjector.providers.Provider;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public final class CommonObjectsProvider {
    @Provider
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Provider
    public Executor executor() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
}
