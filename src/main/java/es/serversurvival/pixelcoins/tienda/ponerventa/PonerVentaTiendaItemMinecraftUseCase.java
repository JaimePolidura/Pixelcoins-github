package es.serversurvival.pixelcoins.tienda.ponerventa;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.tienda._shared.TiendaItemMinecraftValidator;
import es.serversurvival.pixelcoins.tienda._shared.OfertaTiendaItemMinecraft;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PonerVentaTiendaItemMinecraftUseCase {
    private final TiendaItemMinecraftValidator itemTiendaValidator;
    private final OfertasService ofertasService;
    private final EventBus eventBus;

    public void ponerVenta(PonerVentaTiendaItemMinecraftParametros parametros) {
        itemTiendaValidator.itemNoBaneado(parametros.getItem());
        itemTiendaValidator.precio(parametros.getPrecio());

        ofertasService.save(OfertaTiendaItemMinecraft.builder()
                .vendedorId(parametros.getJugadorId())
                .precio(parametros.getPrecio())
                .cantidad(parametros.getItem().getAmount())
                .objeto(parametros.getItem().getType().toString())
                .item(parametros.getItem())
                .tipoOferta(TipoOferta.TIENDA_ITEM_MINECRAFT)
                .build());

        eventBus.publish(new TiendaItemMinecrafPuestoEnVenta(parametros.getItem(), parametros.getPrecio(), parametros.getJugadorId()));
    }
}
