package es.serversurvival.mySQL;

import es.serversurvival.mySQL.enums.AccionOrden;
import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.mySQL.tablasObjetos.OrdenPreMarket;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import javafx.util.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static es.serversurvival.util.Funciones.*;
import static es.serversurvival.util.Funciones.enviarMensajeYSonido;

public final class OrdenesPreMarket extends MySQL{
    public final static OrdenesPreMarket INSTANCE = new OrdenesPreMarket();

    private OrdenesPreMarket() {}

    public void nuevaOrden (String jugador, String nombre_activo, int cantidad, AccionOrden accion_orden) {
        executeUpdate("INSERT INTO ordenespremarket (jugador, nombre_activo, cantidad, accion_orden) VALUES ('"+jugador+"', '"+nombre_activo+"', '"+cantidad+"', '"+accion_orden.toString()+"')");
    }

    public List<OrdenPreMarket> getAllOrdenes () {
        return buildListFromQuery("SELECT * FROM ordenespremarket");
    }

    public List<OrdenPreMarket> getOrdenes (String jugador) {
        return buildListFromQuery("SELECT * FROM ordenespremarket WHERE jugador = '"+jugador+"'");
    }

    public OrdenPreMarket getOrdenTicker (String owner, String ticker) {
        return (OrdenPreMarket) buildObjectFromQuery("SELECT * FROM ordenespremarket WHERE jugador = '"+owner+"' AND nombre_activo = '"+ticker+"'");
    }

    public void borrarOrden(int id) {
        executeUpdate("DELETE FROM ordenespremarket WHERE id = '"+id+"'");
    }

    public void abrirOrdenCompraLargo(Player player, String ticker, int cantidad) {
        nuevaOrden(player.getName(), ticker, cantidad, AccionOrden.LARGO_COMPRA);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Se ha abierto una orden. Cuando el mercado este abierto se ejecutara. " +
                ChatColor.AQUA + "/bolsa ordenes", Sound.ENTITY_PLAYER_LEVELUP);
    }

    public void abrirOrdenVentaLargo(Player player, String id, int cantidad) {
        if(getOrdenTicker(player.getName(), id) != null){
            enviarMensajeYSonido(player,ChatColor.DARK_RED + "Ya tienes esa orden alistada. /bolsa ordenes", Sound.ENTITY_VILLAGER_NO);
            return;
        }

        nuevaOrden(player.getName(), id, cantidad, AccionOrden.LARGO_VENTA);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Se ha abierto una orden. Cuando el mercado este abierto se ejecutara. " +
                ChatColor.AQUA + "/bolsa ordenes", Sound.ENTITY_PLAYER_LEVELUP);
    }

    public void abrirOrdenVentaCorto (Player player, String ticker, int cantidad){
        nuevaOrden(player.getName(), ticker, cantidad, AccionOrden.CORTO_VENTA);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Se ha abierto una orden. Cuando el mercado este abierto se ejecutara. " +
                ChatColor.AQUA + "/bolsa ordenes", Sound.ENTITY_PLAYER_LEVELUP);
    }

    public void abrirOrdenCompraCorto (Player player, String id, int cantidad){
        nuevaOrden(player.getName(), id, cantidad, AccionOrden.CORTO_COMPRA);

        enviarMensajeYSonido(player,ChatColor.GOLD + "Se ha abierto una orden. Cuando el mercado este abierto se ejecutara. " +
                ChatColor.AQUA + "/bolsa ordenes", Sound.ENTITY_PLAYER_LEVELUP);
    }

    public void cancelarOrden (int id, Player player) {
        borrarOrden(id);

        player.sendMessage(ChatColor.RED + "Has cancelado la orden");
    }

    public void ejecutarOrdenes () {
        if(mercadoNoEstaAbierto()){
            return;
        }

        MySQL.conectar();
        List<OrdenPreMarket> todasLasOrdenes = this.getAllOrdenes();

        POOL.submit(() -> {
            todasLasOrdenes.forEach(orden -> {
                if(orden.getAccion_orden() == AccionOrden.LARGO_COMPRA){
                    ejecutarOrdenCompraLargo(orden);

                }else if (orden.getAccion_orden() == AccionOrden.LARGO_VENTA){
                    ejecutarOrdenVentaLargo(orden);

                }else if (orden.getAccion_orden() == AccionOrden.CORTO_VENTA) {
                    ejecutarOrdenVentaCorto(orden);

                }else if (orden.getAccion_orden() == AccionOrden.CORTO_COMPRA){
                    ejecutarOrdenCompraCorto(orden);
                }
            });
        });
    }

    private void ejecutarOrdenVentaLargo(OrdenPreMarket orden) {
        MySQL.conectar();

        int cantidad = orden.getCantidad();
        String jugador = orden.getJugador();
        int id = Integer.parseInt(orden.getNombre_activo());
        PosicionAbierta posicionAbierta = posicionesAbiertasMySQL.getPosicionAbierta(id);

        transaccionesMySQL.venderPosicion(posicionAbierta, cantidad, jugador);
        borrarOrden(orden.getId());
    }

    private void ejecutarOrdenCompraLargo(OrdenPreMarket orden) {
        MySQL.conectar();

        String ticker = orden.getNombre_activo();
        int cantidad = orden.getCantidad();
        String jugador = orden.getJugador();

        Pair<String, Double> pairNombrePrecio = llamadasApiMySQL.getPairNombreValorPrecio(ticker).get();

        double precio = pairNombrePrecio.getValue();
        String nombreValor = pairNombrePrecio.getKey();
        double pixelcoinsJugador = jugadoresMySQL.getJugador(jugador).getPixelcoins();

        if(cantidad * precio >= pixelcoinsJugador)
            cantidad = (int) (pixelcoinsJugador / precio);

        if(cantidad == 0) {
            mensajesMySQL.nuevoMensaje("", jugador, "No se ha podido ejecutar la orden de " + orden.getNombre_activo() + " por que no tenias las suficintes pixelcoins");
            llamadasApiMySQL.borrarLlamadaSiNoEsUsada(ticker);
            return;
        }

        transaccionesMySQL.comprarUnidadBolsa(TipoActivo.ACCIONES, ticker, nombreValor, "acciones", precio, cantidad, jugador);
        mensajesMySQL.nuevoMensaje("", jugador, "Se ha ejecutado la orden de: " + orden.getNombre_activo() + " -" + cantidad * precio + " PC");
        borrarOrden(orden.getId());

        MySQL.desconectar();
    }

    private void ejecutarOrdenVentaCorto (OrdenPreMarket orden) {
        MySQL.conectar();

        Pair<String, Double> pairNombrePrecio = llamadasApiMySQL.getPairNombreValorPrecio(orden.getNombre_activo()).get();
        double precio = pairNombrePrecio.getValue();
        String nombreValor = pairNombrePrecio.getKey();

        transaccionesMySQL.venderEnCortoBolsa(orden.getJugador(), orden.getNombre_activo(), nombreValor, orden.getCantidad(), precio);
        borrarOrden(orden.getId());
    }

    private void ejecutarOrdenCompraCorto (OrdenPreMarket orden) {
        PosicionAbierta posicionAbierta = posicionesAbiertasMySQL.getPosicionAbierta(Integer.parseInt(orden.getNombre_activo()));

        transaccionesMySQL.comprarPosicionCorto(posicionAbierta, orden.getCantidad(), orden.getJugador());
        borrarOrden(orden.getId());
    }

    @Override
    protected OrdenPreMarket buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new OrdenPreMarket(rs.getInt("id"),
                rs.getString("jugador"),
                rs.getString("nombre_activo"),
                rs.getInt("cantidad"),
                AccionOrden.valueOf(rs.getString("accion_orden")));
    }
}
