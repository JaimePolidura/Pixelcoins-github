package es.serversurvival.nfs.tienda.vertienda.usecases;

import es.serversurvival.legacy.main.Pixelcoin;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.eventos.tienda.ItemCompradoEvento;
import es.serversurvival.legacy.mySQL.tablasObjetos.Jugador;
import es.serversurvival.legacy.mySQL.tablasObjetos.Oferta;
import es.serversurvival.nfs.jugadores.mySQL.Jugadores;
import org.bukkit.inventory.ItemStack;

public final class ComprarOfertaUseCase implements AllMySQLTablesInstances {
    public static final ComprarOfertaUseCase INSTANCE = new ComprarOfertaUseCase();

    private ComprarOfertaUseCase () {}

    public void realizarVenta(String comprador, int id) {
        Oferta ofertaAComprar = ofertasMySQL.getOferta(id);
        Jugador jugadorComprador = jugadoresMySQL.getJugador(comprador);

        int cantidad = ofertaAComprar.getCantidad();
        String vendedor = ofertaAComprar.getJugador();
        String objeto = ofertaAComprar.getObjeto();
        double precio = ofertaAComprar.getPrecio();

        ItemStack itemAComprar = ofertasMySQL.getItemOferta(ofertasMySQL.getOferta(id));
        itemAComprar.setAmount(1);

        if (cantidad == 1)
            ofertasMySQL.borrarOferta(id);
        else
            ofertasMySQL.setCantidad(id, cantidad - 1);

        Jugadores.INSTANCE.realizarTransferenciaConEstadisticas(comprador, vendedor, precio);

        Pixelcoin.publish(new ItemCompradoEvento(vendedor, comprador, objeto, cantidad, precio));
    }
}
