package es.serversurvival._shared.mysql;

import es.dependencyinjector.dependencies.annotations.Configuration;
import es.dependencyinjector.providers.Provider;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.connection.ConnectionManager;
import es.jaime.javaddd.domain.database.TransactionManager;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public final class MySQLProviders {
    private final DatabaseConfiguration databaseConfiguration;

    @Provider
    public ConnectionManager connectionManager() {
        return new ConnectionManager(databaseConfiguration);
    }
}
