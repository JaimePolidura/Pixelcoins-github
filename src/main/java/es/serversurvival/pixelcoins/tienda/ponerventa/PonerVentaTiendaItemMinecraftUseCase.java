package es.serversurvival.pixelcoins.tienda.ponerventa;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.tienda._shared.TiendaItemMinecraftValidator;
import es.serversurvival.pixelcoins.tienda._shared.OfertaTiendaItemMinecraft;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PonerVentaTiendaItemMinecraftUseCase implements UseCaseHandler<PonerVentaTiendaItemMinecraftParametros> {
    private final TiendaItemMinecraftValidator validator;
    private final OfertasService ofertasService;
    private final EventBus eventBus;

    @Override
    public void handle(PonerVentaTiendaItemMinecraftParametros parametros) {
        validator.itemNoTieneMarcaDeAgua(parametros.getItem());
        validator.itemNoBaneado(parametros.getItem());
        validator.precio(parametros.getPrecio());

        OfertaTiendaItemMinecraft build = OfertaTiendaItemMinecraft.builder()
                .vendedorId(parametros.getJugadorId())
                .precio(parametros.getPrecio())
                .cantidad(parametros.getItem().getAmount())
                .item(parametros.getItem())
                .build();

        ofertasService.save(build);

        eventBus.publish(new TiendaItemMinecrafPuestoEnVenta(parametros.getItem(), parametros.getPrecio(), parametros.getJugadorId()));
    }
}
