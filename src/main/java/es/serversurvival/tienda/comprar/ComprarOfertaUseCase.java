package es.serversurvival.tienda.comprar;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival._shared.utils.ItemsUtils;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.tienda._shared.application.TiendaService;
import es.serversurvival.tienda._shared.domain.TiendaObjeto;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public final class ComprarOfertaUseCase implements AllMySQLTablesInstances {
    private final TiendaService tiendaService;
    private final JugadoresService jugadoresService;

    public ComprarOfertaUseCase () {
        this.tiendaService = DependecyContainer.get(TiendaService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public ItemStack realizarVenta(String comprador, UUID tiendaObjetoId) {
        TiendaObjeto tiendaObjetoAComprar = this.ensureObjetoTiendaExists(tiendaObjetoId);
        Jugador jugadorComprador = this.ensureJugadorExists(comprador);
        this.ensureCompradorHasEnoughPixelcoins(tiendaObjetoAComprar, jugadorComprador);
        Jugador jugadorVendedor = this.ensureJugadorExists(tiendaObjetoAComprar.getJugador());

        int cantidad = tiendaObjetoAComprar.getCantidad();
        String vendedor = tiendaObjetoAComprar.getJugador();
        String objeto = tiendaObjetoAComprar.getObjeto();
        double precio = tiendaObjetoAComprar.getPrecio();

        ItemStack itemAComprar = ItemsUtils.getItemStakcByTiendaObjeto(tiendaObjetoAComprar);
        itemAComprar.setAmount(1);

        jugadoresService.realizarTransferenciaConEstadisticas(jugadorComprador, jugadorVendedor, tiendaObjetoAComprar.getPrecio());

        if(tiendaObjetoAComprar.getCantidad() == 1)
            this.tiendaService.deleteById(tiendaObjetoId);
        else
            this.tiendaService.save(tiendaObjetoAComprar.decrementCantidadByOne());

        Pixelcoin.publish(new ObjetoTiendaComprado(vendedor, comprador, objeto, cantidad, precio));

        return itemAComprar;
    }

    private void ensureCompradorHasEnoughPixelcoins(TiendaObjeto itemTienda, Jugador comprador){
        if(itemTienda.getPrecio() > comprador.getPixelcoins())
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins para realizar la compra");
    }

    private TiendaObjeto ensureObjetoTiendaExists(UUID tiendaObjetoId){
        return this.tiendaService.getById(tiendaObjetoId);
    }

    private Jugador ensureJugadorExists(String jugadorName){
        return this.jugadoresService.getByNombre(jugadorName);
    }
}
