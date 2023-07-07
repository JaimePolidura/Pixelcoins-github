package es.serversurvival.pixelcoins.lootbox.propiedad.comprar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.lootbox.propiedad._shared.application.LootboxEnPropiedadValidator;
import es.serversurvival.pixelcoins.lootbox.propiedad._shared.application.LootboxEnPropiedadService;
import es.serversurvival.pixelcoins.lootbox.propiedad._shared.domain.LootboxEnPropiedad;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesSaver;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.domain.Transaccion;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class ComprarLootboxUseCase implements UseCaseHandler<ComprarLootboxParametros> {
    private final LootboxEnPropiedadService lootboxEnPropiedadService;
    private final LootboxEnPropiedadValidator validator;
    private final TransaccionesSaver transaccionesSaver;
    private final EventBus eventBus;

    @Override
    public void handle(ComprarLootboxParametros parametros) {
        validator.tienePixelcoins(parametros.getJugadorId(), parametros.getLootboxTier());

        double precio = parametros.getLootboxTier().getPrecio();
        UUID lootBoxEnPropiedadId = UUID.randomUUID();

        lootboxEnPropiedadService.save(LootboxEnPropiedad.builder()
                .lootboxEnPropiedadId(lootBoxEnPropiedadId)
                .jugadorId(parametros.getJugadorId())
                .tier(parametros.getLootboxTier())
                .build());
        transaccionesSaver.save(Transaccion.builder()
                .pagadorId(parametros.getJugadorId())
                .pixelcoins(precio)
                .tipo(TipoTransaccion.LOOTBOX_COMPRAR)
                .objeto(lootBoxEnPropiedadId)
                .build());

        eventBus.publish(new LootBoxComprada(parametros.getJugadorId(), parametros.getLootboxTier()));
    }
}
