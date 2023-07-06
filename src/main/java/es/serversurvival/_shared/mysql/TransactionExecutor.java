package es.serversurvival._shared.mysql;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.database.TransactionManager;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public final class TransactionExecutor {
    private final TransactionManager transactionManager;

    public void execute(Runnable runnable) {
        try{
            transactionManager.start();
            runnable.run();
            transactionManager.commit();
        }catch (Exception e) {
            transactionManager.rollback();
            throw new RuntimeException(e);
        }
    }
}
