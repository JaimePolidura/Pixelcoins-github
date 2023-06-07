package es.serversurvival._shared.utils;

import es.dependencyinjector.dependencies.annotations.Configuration;
import es.dependencyinjector.providers.Provider;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;

@Configuration
public final class CommonObjectsProvider {
    @Provider
    public ExecutorService executorService() {
        return Funciones.POOL;
    }

    @Provider
    public DateTimeFormatter dateFormatter() {
        return Funciones.DATE_FORMATER;
    }

    @Provider
    public DecimalFormat decimalFormat() {
        return Funciones.FORMATEA;
    }
}
