package es.serversurvival._shared.providers;

import es.dependencyinjector.dependencies.annotations.Configuration;
import es.dependencyinjector.providers.Provider;
import es.serversurvival._shared.mysql.MySQLConfiguration;

@Configuration
public final class MySQLProviders {
    @Provider
    public MySQLConfiguration mySQLConfigurationProviders() {
        return new MySQLConfiguration();
    }
}
