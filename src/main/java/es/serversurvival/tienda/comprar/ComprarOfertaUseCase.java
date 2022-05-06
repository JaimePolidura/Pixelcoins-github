package es.serversurvival.tienda.comprar;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival.jugadores._shared.mySQL.MySQLJugadoresRepository;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.tienda._shared.newformat.application.TiendaService;
import es.serversurvival.tienda._shared.newformat.domain.TiendaObjeto;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public final class ComprarOfertaUseCase implements AllMySQLTablesInstances {
    private final TiendaService tiendaService;

    private ComprarOfertaUseCase () {
        this.tiendaService = DependecyContainer.get(TiendaService.class);
    }

    public ItemStack realizarVenta(String comprador, UUID tiendaObjetoId) {
        TiendaObjeto ofertaAComprar = ofertasMySQL.getOferta(tiendaObjetoId);
        Jugador jugadorComprador = jugadoresMySQL.getJugador(comprador);

        int cantidad = ofertaAComprar.getCantidad();
        String vendedor = ofertaAComprar.getJugador();
        String objeto = ofertaAComprar.getObjeto();
        double precio = ofertaAComprar.getPrecio();

        ItemStack itemAComprar = ofertasMySQL.getItemOferta(ofertaAComprar);
        itemAComprar.setAmount(1);

        if (cantidad == 1)
            ofertasMySQL.borrarOferta(tiendaObjetoId);
        else
            ofertasMySQL.setCantidad(tiendaObjetoId, cantidad - 1);

        //TODO
        MySQLJugadoresRepository.INSTANCE.realizarTransferenciaConEstadisticas(comprador, vendedor, precio);

        Pixelcoin.publish(new ItemCompradoEvento(vendedor, comprador, objeto, cantidad, precio));

        return itemAComprar;
    }
}
