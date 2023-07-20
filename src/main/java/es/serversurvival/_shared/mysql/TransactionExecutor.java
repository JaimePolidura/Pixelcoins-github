package es.serversurvival._shared.mysql;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.connection.transactions.DatabaseTransacionExecutor;
import es.jaime.connection.transactions.DatabaseTransactionManager;

@Service
public final class TransactionExecutor {
    private final DatabaseTransacionExecutor databaseTransacionExecutor;

    public TransactionExecutor(DatabaseTransactionManager transactionManager) {
        this.databaseTransacionExecutor = new DatabaseTransacionExecutor(transactionManager);
    }

    public void execute(DatabaseTransacionExecutor.ExceptionHandlingMethod exceptionHandlingMethod, Runnable runnable) {
        this.databaseTransacionExecutor.execute(exceptionHandlingMethod, runnable);
    }
}
