package es.serversurvival._shared.mysql;

import es.dependencyinjector.dependencies.annotations.Configuration;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.javaddd.domain.database.TransactionManager;
import es.jaime.transacions.DatabaseTransacionManager;

@Configuration
public final class MySQLTransactionManager implements TransactionManager {
    private final DatabaseTransacionManager databaseTransacionManager;

    public MySQLTransactionManager(DatabaseConfiguration databaseConfiguration) {
        this.databaseTransacionManager = new DatabaseTransacionManager(databaseConfiguration.getConnectionManager());
    }

    @Override
    public void start() {
        databaseTransacionManager.start();
    }

    @Override
    public void commit() {
        databaseTransacionManager.commit();
    }

    @Override
    public void rollback() {
        databaseTransacionManager.rollback();
    }
}
