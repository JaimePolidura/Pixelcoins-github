package es.serversurvival.pixelcoins._shared.usecases;

public interface UseCaseHandler<T extends ParametrosUseCase> {
    void handle(T parametros);
}
