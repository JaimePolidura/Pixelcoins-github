package es.serversurvival._shared.mysql;

import es.dependencyinjector.annotations.Configuration;
import es.jaime.configuration.DatabaseConfiguration;

import static java.lang.String.*;

@Configuration
public final class MySQLConfiguration extends DatabaseConfiguration {
    private static final String DB_NAME = "pixelcoins4";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final String DB_HOST = "localhost";
    private static final int DB_PORT = 3306;

    @Override
    protected String url() {
        return format("jdbc:mysql://%s:%s/%s?user=%s&password=%s&useSSL=false&allowPublicKeyRetrieval=true",
                DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD);
    }
}
