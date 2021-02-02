package es.serversurvival.mySQL;

import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.mySQL.enums.TipoOfertante;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.mySQL.tablasObjetos.OfertaMercadoServer;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static es.serversurvival.util.Funciones.*;
import static org.bukkit.ChatColor.*;

public final class OfertasMercadoServer extends MySQL{
    private OfertasMercadoServer() {}
    public static final OfertasMercadoServer INSTANCE = new OfertasMercadoServer();

    public void nueva (String jugador, String empresa, double precio, int cantidad, TipoOfertante tipoOfertante, double precioApertura) {
        String fecha = dateFormater.format(new Date());

        executeUpdate("INSERT INTO ofertasbolsaserver (jugador, empresa, precio, cantidad, fecha, tipo_ofertante, precio_apertura) VALUES ('"+jugador+"', '"+empresa+"', '"+precio+"', '"+cantidad+"', '"+fecha+"', '"+tipoOfertante.toString()+"', '"+precioApertura+"')");
    }

    public List<OfertaMercadoServer> getAll () {
        return buildListFromQuery("SELECT * FROM ofertasbolsaserver");
    }

    public OfertaMercadoServer get (int id) {
        return (OfertaMercadoServer) buildObjectFromQuery("SELECT * FROM ofertasbolsaserver WHERE id = '"+id+"'");
    }

    public void setCantidad (int id, int cantidad) {
        executeUpdate("UPDATE ofertasbolsaserver SET cantidad = '"+cantidad+"' WHERE id = '"+id+"'");
    }

    public void borrar (int id) {
        executeUpdate("DELETE FROM ofertasbolsaserver WHERE id = '"+id+"'");
    }

    public void setCantidadOBorrar (int id, int cantidad) {
        if(this.get(id).getCantidad() > cantidad){
            this.setCantidad(id, cantidad);
        }else{
            this.borrar(id);
        }
    }

    /**
     * Hay que hacer que el jugador puede ejercer un precio de venta
     */
    public void venderOfertaDesdeBolsaCartera (Player player, PosicionAbierta posicionAVender) {
        nueva(player.getName(), posicionAVender.getNombre_activo(), posicionAVender.getPrecio_apertura(), posicionAVender.getCantidad(), TipoOfertante.JUGADOR, posicionAVender.getPrecio_apertura());
        posicionesAbiertasMySQL.borrarPosicionAbierta(posicionAVender.getId());

        enviarMensajeYSonido(player, GOLD + "Al ser un accion de una empresa del servidor de minecraft. Se ha puesta la oferta de venta en el mercado de acciones. Para consultar el mercado: " + AQUA + "/empresas mercado",
                Sound.ENTITY_PLAYER_LEVELUP);
        Bukkit.broadcastMessage(GOLD + player.getName() + " ha subido acciones de la empresa del servidor: " + posicionAVender.getNombre_activo() + AQUA + " /empresas mercado");
    }

    public void cancelarOferta (Player player, int id) {
        OfertaMercadoServer oferta = this.get(id);
        this.borrar(id);
        posicionesAbiertasMySQL.nuevaPosicion(player.getName(), TipoActivo.ACCIONES_SERVER, oferta.getEmpresa(), oferta.getCantidad(), oferta.getPrecio_apertura(), TipoPosicion.LARGO);

        enviarMensajeYSonido(player, GOLD + "Has cancelado tu oferta en el mercado. Ahora vuelves a tener esas acciones en tu cartera: " + AQUA + "/bolsa cartera",
                Sound.ENTITY_PLAYER_LEVELUP);
    }

    @Override
    protected OfertaMercadoServer buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new OfertaMercadoServer(
                rs.getInt("id"),
                rs.getString("jugador"),
                rs.getString("empresa"),
                rs.getDouble("precio"),
                rs.getInt("cantidad"),
                rs.getString("fecha"),
                TipoOfertante.valueOf(rs.getString("tipo_ofertante")),
                rs.getDouble("precio_apertura")
        );
    }
}
