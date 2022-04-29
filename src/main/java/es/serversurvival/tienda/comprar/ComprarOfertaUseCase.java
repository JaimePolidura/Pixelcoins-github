package es.serversurvival.tienda.comprar;

import es.serversurvival.Pixelcoin;
import es.serversurvival.jugadores._shared.mySQL.Jugador;
import es.serversurvival.jugadores._shared.mySQL.JugadoresRepository;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.tienda._shared.mySQL.ofertas.Oferta;
import org.bukkit.inventory.ItemStack;

public final class ComprarOfertaUseCase implements AllMySQLTablesInstances {
    public static final ComprarOfertaUseCase INSTANCE = new ComprarOfertaUseCase();

    private ComprarOfertaUseCase () {}

    public ItemStack realizarVenta(String comprador, int id) {
        Oferta ofertaAComprar = ofertasMySQL.getOferta(id);
        Jugador jugadorComprador = jugadoresMySQL.getJugador(comprador);

        int cantidad = ofertaAComprar.getCantidad();
        String vendedor = ofertaAComprar.getJugador();
        String objeto = ofertaAComprar.getObjeto();
        double precio = ofertaAComprar.getPrecio();

        ItemStack itemAComprar = ofertasMySQL.getItemOferta(ofertaAComprar);
        itemAComprar.setAmount(1);

        if (cantidad == 1)
            ofertasMySQL.borrarOferta(id);
        else
            ofertasMySQL.setCantidad(id, cantidad - 1);

        //TODO
        JugadoresRepository.INSTANCE.realizarTransferenciaConEstadisticas(comprador, vendedor, precio);

        Pixelcoin.publish(new ItemCompradoEvento(vendedor, comprador, objeto, cantidad, precio));

        return itemAComprar;
    }
}
