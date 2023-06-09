package es.serversurvival.pixelcoins._shared.usecases;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.database.TransactionManager;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public final class UseCaseBus {
    private final DependenciesRepository dependencies;
    private final TransactionManager transactionManager;

    private final Map<Class<?>, UseCaseHandler<?>> useCasesByParametrosCache;

    public UseCaseBus(DependenciesRepository dependencies, TransactionManager transactionManager) {
        this.useCasesByParametrosCache = new ConcurrentHashMap<>();
        this.transactionManager = transactionManager;
        this.dependencies = dependencies;
    }

    private <T extends ParametrosUseCase> void handle(T parametros) {
        UseCaseHandler<T> useCase =  getUseCase(parametros);

        try {
            transactionManager.start();
            useCase.handle(parametros);
            transactionManager.commit();
        } catch (Exception e) {
            transactionManager.rollback();
            throw new RuntimeException(e);
        }
    }

    private <T extends ParametrosUseCase> UseCaseHandler<T> getUseCase(T parametros) {
        if(useCasesByParametrosCache.containsKey(parametros.getClass())){
            return (UseCaseHandler<T>) useCasesByParametrosCache.get(parametros.getClass());
        }

        return (UseCaseHandler<T>) dependencies.filterByImplementsInterfaceWithGeneric(UseCaseHandler.class, parametros.getClass())
                .map(useCase -> useCasesByParametrosCache.put(parametros.getClass(), useCase))
                .orElseThrow(() -> new ResourceNotFound("Caso de uso no registrado, hablar con Jaime"));
    }
}
