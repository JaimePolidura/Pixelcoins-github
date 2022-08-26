package es.serversurvival.tienda.comprar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaimetruman.annotations.UseCase;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival._shared.utils.ItemsUtils;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.tienda._shared.application.TiendaService;
import es.serversurvival.tienda._shared.domain.TiendaObjeto;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@RequiredArgsConstructor
@UseCase
public final class ComprarTiendaObjetoUseCase {
    public static final ComprarTiendaObjetoUseCase INSTANCE = new ComprarTiendaObjetoUseCase();

    private final TiendaService tiendaService;
    private final JugadoresService jugadoresService;
    private final EventBus eventBus;

    public ComprarTiendaObjetoUseCase() {
        this.tiendaService = DependecyContainer.get(TiendaService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public ItemStack realizarVenta(String comprador, UUID tiendaObjetoId) {
        TiendaObjeto tiendaObjetoAComprar = this.ensureObjetoTiendaExists(tiendaObjetoId);
        Jugador jugadorComprador = jugadoresService.getByNombre(comprador);
        this.ensureCompradorHasEnoughPixelcoins(tiendaObjetoAComprar, jugadorComprador);
        Jugador jugadorVendedor = jugadoresService.getByNombre(tiendaObjetoAComprar.getJugador());
        this.ensureNotHisSelf(jugadorComprador, jugadorVendedor);

        int cantidad = tiendaObjetoAComprar.getCantidad();
        String vendedor = tiendaObjetoAComprar.getJugador();
        String objeto = tiendaObjetoAComprar.getObjeto();
        double precio = tiendaObjetoAComprar.getPrecio();

        ItemStack itemAComprar = ItemsUtils.getItemStakcByTiendaObjeto(tiendaObjetoAComprar);
        itemAComprar.setAmount(1);

        jugadoresService.realizarTransferencia(jugadorComprador, jugadorVendedor, tiendaObjetoAComprar.getPrecio());

        if(tiendaObjetoAComprar.getCantidad() == 1)
            this.tiendaService.deleteById(tiendaObjetoId);
        else
            this.tiendaService.save(tiendaObjetoAComprar.decrementCantidadByOne());

        this.eventBus.publish(new ObjetoTiendaComprado(vendedor, comprador, objeto, cantidad, precio));

        return itemAComprar;
    }

    private void ensureNotHisSelf(Jugador jugadorComprador, Jugador jugadorVendedor) {
        if(jugadorComprador.getNombre().equalsIgnoreCase(jugadorVendedor.getNombre()))
            throw new CannotBeYourself("No te puedes autocomprar un objeto en la tienda");
    }

    private void ensureCompradorHasEnoughPixelcoins(TiendaObjeto itemTienda, Jugador comprador){
        if(itemTienda.getPrecio() > comprador.getPixelcoins())
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins para realizar la compra");
    }

    private TiendaObjeto ensureObjetoTiendaExists(UUID tiendaObjetoId){
        return this.tiendaService.getById(tiendaObjetoId);
    }
}
