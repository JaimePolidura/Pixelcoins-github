package es.serversurvival.pixelcoins._shared.usecases;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.connection.transactions.DatabaseTransacionExecutor;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.mysql.TransactionExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public final class UseCaseBus {
    private final TransactionExecutor transactionExecutor;
    private final DependenciesRepository dependencies;

    private final Map<Class<?>, UseCaseHandler<?>> useCasesByParametrosCache;

    public UseCaseBus(TransactionExecutor transactionExecutor, DependenciesRepository dependencies) {
        this.transactionExecutor = transactionExecutor;
        this.useCasesByParametrosCache = new ConcurrentHashMap<>();
        this.dependencies = dependencies;
    }

    public <T extends ParametrosUseCase> void handle(T parametros) {
        UseCaseHandler<T> useCase = getUseCase(parametros);

        transactionExecutor.execute(DatabaseTransacionExecutor.ExceptionHandlingMethod.ONLY_RETHROW, () ->  {
            useCase.handle(parametros);
        });
    }

    private <T extends ParametrosUseCase> UseCaseHandler<T> getUseCase(T parametros) {
        if(useCasesByParametrosCache.containsKey(parametros.getClass())){
            return (UseCaseHandler<T>) useCasesByParametrosCache.get(parametros.getClass());
        }

        return dependencies.filterByImplementsInterfaceWithGeneric(UseCaseHandler.class, parametros.getClass())
                .map(useCase -> {
                    useCasesByParametrosCache.put(parametros.getClass(), useCase);
                    return useCase;
                })
                .orElseThrow(() -> new ResourceNotFound("Caso de uso no registrado, hablar con Jaime"));
    }

}
