package es.serversurvival.pixelcoins.lootbox.abrir;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.lootbox._shared.items.application.LootboxItemSeleccionador;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxItemSeleccionadaoResultado;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.application.LootboxEnPropiedadValidator;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.application.LootboxEnPropiedadService;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.domain.LootboxEnPropiedad;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class AbrirLootboxUseCase implements UseCaseHandler<AbrirLootboxParametros> {
    private final LootboxEnPropiedadService lootboxEnPropiedadService;
    private final LootboxItemSeleccionador lootboxItemSeleccionador;
    private final LootboxEnPropiedadValidator validator;
    private final EventBus eventBus;

    @Override
    public void handle(AbrirLootboxParametros parametros) {
        validator.tieneEnPropiedadLaLootbox(parametros.getJugadorId(), parametros.getLootboxEnPropiedadId());
        validator.lootboxPendienteDeAbrir(parametros.getLootboxEnPropiedadId());

        LootboxEnPropiedad lootboxAbrir = lootboxEnPropiedadService.getById(parametros.getLootboxEnPropiedadId());
        LootboxItemSeleccionadaoResultado resultado = lootboxItemSeleccionador.seleccionarAleatorio(lootboxAbrir.getTier());

        lootboxEnPropiedadService.save(lootboxAbrir.abrir(resultado));

        eventBus.publish(new LootboxItemAbierto(resultado, parametros.getJugadorId()));
    }
}
