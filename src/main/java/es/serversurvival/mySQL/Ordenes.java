package es.serversurvival.mySQL;

import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.mySQL.enums.TipoOperacion;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.mySQL.tablasObjetos.Orden;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import javafx.util.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static es.serversurvival.util.Funciones.*;

public final class Ordenes extends MySQL{
    public final static Ordenes INSTANCE = new Ordenes();

    private Ordenes () {}

    public void nuevaOrden (String jugador, String nombre_activo, int cantidad, TipoOperacion tipo_operacion, TipoPosicion tipo_posicion) {
        executeUpdate("INSERT INTO ordenes (jugador, nombre_activo, cantidad, tipo_operacion, tipo_posicion) VALUES ('"+jugador+"', '"+nombre_activo+"', '"+cantidad+"', '"+tipo_operacion.toString()+"', '"+tipo_posicion+"')");
    }

    public List<Orden> getAllOrdenes () {
        return buildListFromQuery("SELECT * FROM ordenes");
    }

    public List<Orden> getOrdenes (String jugador) {
        return buildListFromQuery("SELECT * FROM ordenes WHERE jugador = '"+jugador+"'");
    }

    public Orden getOrdenTicker (String owner, String ticker) {
        return (Orden) buildObjectFromQuery("SELECT * FROM ordenes WHERE jugador = '"+owner+"' AND nombre_activo = '"+ticker+"'");
    }

    public void borrarOrden(int id) {
        executeUpdate("DELETE FROM ordenes WHERE id = '"+id+"'");
    }

    public void abrirOrdenCompraLargo(Player player, String ticker, int cantidad) {
        nuevaOrden(player.getName(), ticker, cantidad, TipoOperacion.COMPRA, TipoPosicion.LARGO);

        player.sendMessage(ChatColor.GOLD + "Se ha abierto una orden. Cuando el mercado este abierto se ejecutara. " + ChatColor.AQUA + "/bolsa ordenes");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public void abrirOrdenVentaLargo(Player player, String id, int cantidad) {
        if(getOrdenTicker(player.getName(), id) != null){
            player.sendMessage(ChatColor.DARK_RED + "Ya tienes esa orden alistada. /bolsa ordenes");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        nuevaOrden(player.getName(), id, cantidad, TipoOperacion.VENTA, TipoPosicion.LARGO);

        player.sendMessage(ChatColor.GOLD + "Se ha abierto una orden. Cuando el mercado este abierto se ejecutara. " + ChatColor.AQUA + "/bolsa ordenes");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public void abrirOrdenVentaCorto (Player player, String ticker, int cantidad){
        nuevaOrden(player.getName(), ticker, cantidad, TipoOperacion.VENTA, TipoPosicion.CORTO);

        player.sendMessage(ChatColor.GOLD + "Se ha abierto una orden. Cuando el mercado este abierto se ejecutara. " + ChatColor.AQUA + "/bolsa ordenes");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public void abrirOrdenCompraCorto (Player player, String id, int cantidad){
        nuevaOrden(player.getName(), id, cantidad, TipoOperacion.COMPRA, TipoPosicion.CORTO);

        player.sendMessage(ChatColor.GOLD + "Se ha abierto una orden. Cuando el mercado este abierto se ejecutara. " + ChatColor.AQUA + "/bolsa ordenes");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public void cancelarOrden (int id, Player player) {
        borrarOrden(id);

        player.sendMessage(ChatColor.RED + "Has cancelado la orden");
    }

    public void ejecutarOrdenes () {
        if(false){
            return;
        }

        MySQL.conectar();
        List<Orden> todasLasOrdenes = this.getAllOrdenes();

        POOL.submit(() -> {
            todasLasOrdenes.forEach(orden -> {
                if(orden.getTipo_operacion() == TipoOperacion.COMPRA && orden.getTipo_posicion() == TipoPosicion.LARGO){
                    ejecutarOrdenCompraLargo(orden);
                }else if (orden.getTipo_operacion() == TipoOperacion.VENTA && orden.getTipo_posicion() == TipoPosicion.LARGO){
                    ejecutarOrdenVentaLargo(orden);
                }else if (orden.getTipo_operacion() == TipoOperacion.VENTA && orden.getTipo_posicion() == TipoPosicion.CORTO) {
                    ejecutarOrdenVentaCorto(orden);
                }else if (orden.getTipo_operacion() == TipoOperacion.COMPRA && orden.getTipo_posicion() == TipoPosicion.CORTO){
                    ejecutarOrdenCompraCorto(orden);
                }
            });
        });
    }

    private void ejecutarOrdenVentaLargo(Orden orden) {
        MySQL.conectar();

        int cantidad = orden.getCantidad();
        String jugador = orden.getJugador();
        int id = Integer.parseInt(orden.getNombre_activo());
        PosicionAbierta posicionAbierta = posicionesAbiertasMySQL.getPosicionAbierta(id);

        transaccionesMySQL.venderPosicion(posicionAbierta, cantidad, jugador);
        borrarOrden(orden.getId());
    }

    private void ejecutarOrdenCompraLargo(Orden orden) {
        MySQL.conectar();

        String ticker = orden.getNombre_activo();
        int cantidad = orden.getCantidad();
        String jugador = orden.getJugador();

        Pair<String, Double> pairNombrePrecio = llamadasApiMySQL.getPairNombreValorPrecio(ticker).get();

        double precio = pairNombrePrecio.getValue();
        String nombreValor = pairNombrePrecio.getKey();
        double pixelcoinsJugador = jugadoresMySQL.getJugador(jugador).getPixelcoins();

        if(cantidad * precio >= pixelcoinsJugador){
            cantidad = (int) (pixelcoinsJugador / precio);
        }

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

    private void ejecutarOrdenVentaCorto (Orden orden) {
        MySQL.conectar();

        Pair<String, Double> pairNombrePrecio = llamadasApiMySQL.getPairNombreValorPrecio(orden.getNombre_activo()).get();
        double precio = pairNombrePrecio.getValue();
        String nombreValor = pairNombrePrecio.getKey();

        transaccionesMySQL.venderEnCortoBolsa(orden.getJugador(), orden.getNombre_activo(), nombreValor, orden.getCantidad(), precio);
        borrarOrden(orden.getId());
    }

    private void ejecutarOrdenCompraCorto (Orden orden) {
        PosicionAbierta posicionAbierta = posicionesAbiertasMySQL.getPosicionAbierta(Integer.parseInt(orden.getNombre_activo()));

        transaccionesMySQL.comprarPosicionCorto(posicionAbierta, orden.getCantidad(), orden.getJugador());
        borrarOrden(orden.getId());
    }

    @Override
    protected Orden buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Orden(rs.getInt("id"),
                rs.getString("jugador"),
                rs.getString("nombre_activo"),
                rs.getInt("cantidad"),
                TipoOperacion.valueOf(rs.getString("tipo_operacion")),
                TipoPosicion.valueOf(rs.getString("tipo_posicion")));
    }
}
