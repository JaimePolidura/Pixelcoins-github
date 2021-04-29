package es.serversurvival.bolsa.ordenespremarket.mysql;

import es.serversurvival.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionesAbiertas;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.mysql.MySQL;
import javafx.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static es.serversurvival.utils.Funciones.*;
import static es.serversurvival.utils.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.DARK_RED;

public final class OrdenesPreMarket extends MySQL {
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

    @Override
    protected OrdenPreMarket buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new OrdenPreMarket(rs.getInt("id"),
                rs.getString("jugador"),
                rs.getString("nombre_activo"),
                rs.getInt("cantidad"),
                AccionOrden.valueOf(rs.getString("accion_orden")));
    }
}
