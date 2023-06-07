package es.serversurvival._shared.mysql;

import es.dependencyinjector.dependencies.annotations.Configuration;
import es.dependencyinjector.providers.Provider;

@Configuration
public final class MySQLProviders {
    @Provider
    public MySQLConfiguration mySQLConfigurationProviders() {
        return new MySQLConfiguration();
    }
}
