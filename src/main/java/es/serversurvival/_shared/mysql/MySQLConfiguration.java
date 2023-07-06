package es.serversurvival._shared.mysql;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.dependencyinjector.dependencies.annotations.Configuration;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.connection.pool.ConnectionPool;
import es.serversurvival._shared.ConfigurationVariables;
import lombok.AllArgsConstructor;

import static es.serversurvival._shared.ConfigurationVariables.*;

@Configuration
@AllArgsConstructor
public final class MySQLConfiguration extends DatabaseConfiguration {
    private final ObjectMapper objectMapper;

    @Override
    public String url() {
        return String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s&useSSL=false&allowPublicKeyRetrieval=true",
                DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD);
    }

    @Override
    public ObjectMapper objectMapper() {
        return objectMapper;
    }

    @Override
    public boolean showQueries() {
        return true;
    }
}
