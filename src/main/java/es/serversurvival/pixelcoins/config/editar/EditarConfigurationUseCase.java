package es.serversurvival.pixelcoins.config.editar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationEntry;
import es.serversurvival.pixelcoins.config._shared.application.ConfigurationService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class EditarConfigurationUseCase implements UseCaseHandler<EditarConfigurationParametros> {
    private final ConfigurationService configurationService;
    private final Validador validador;
    private final EventBus eventBus;

    @Override
    public void handle(EditarConfigurationParametros params) {
        validador.noNull(params.getNuevoValor(), "No puede ser null");
        ConfigurationEntry oldConfigEntry = configurationService.getByKey(params.getKey());

        configurationService.save(oldConfigEntry.editar(params.getNuevoValor()));

        eventBus.publish(new ConfigurationEditada(params.getJugadorId(), params.getKey(), oldConfigEntry.getValue(),
                params.getNuevoValor()));
    }
}
