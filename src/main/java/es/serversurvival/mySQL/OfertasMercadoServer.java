package es.serversurvival.mySQL;

import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.mySQL.enums.TipoOfertante;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.mySQL.tablasObjetos.OfertaMercadoServer;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static es.serversurvival.mySQL.enums.TipoOfertante.*;
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

    public List<OfertaMercadoServer> getOfertasEmpresa (String empresa) {
        return buildListFromQuery("SELECT * FROM ofertasbolsaserver WHERE empresa = '"+empresa+"'");
    }

    public List<OfertaMercadoServer> getOfertasJugador (String jugador) {
        return buildListFromQuery("SELECT * FROM ofertasbolsaserver WHERE jugador = '"+jugador+"'");
    }

    public List<OfertaMercadoServer> getOfertasEmpresa (String empresa, Predicate<? super OfertaMercadoServer> condition) {
        List<OfertaMercadoServer> ofertas = buildListFromQuery("SELECT * FROM ofertasbolsaserver WHERE empresa = '"+empresa+"'");

        return ofertas.stream()
                .filter(condition)
                .collect(Collectors.toList());
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
        if(this.get(id).getCantidad() > cantidad )
            this.setCantidad(id, cantidad);
        else
            this.borrar(id);
    }

    public int getAccionesTotales (String empresa) {
        int accionesNoEnCirculacion = getSumaTotalListInteger(posicionesAbiertasMySQL.getPosicionesAccionesServer(empresa), PosicionAbierta::getCantidad);
        int accionesEnCirculacion = getSumaTotalListInteger(getOfertasEmpresa(empresa), OfertaMercadoServer::getCantidad);

        return accionesNoEnCirculacion + accionesEnCirculacion;
    }

    public int getAccionesDeEmpresa (String empresa) {
        List<OfertaMercadoServer> ofertas = getOfertasEmpresa(empresa, oferta -> oferta.getTipo_ofertante() == EMPRESA);

        return getSumaTotalListInteger(ofertas, OfertaMercadoServer::getCantidad);
    }

    public Map<String, Integer> getAccionistasEmpresaServer (String empresa) {
        List<PosicionAbierta> posicionesAcciones = posicionesAbiertasMySQL.getPosicionesAccionesServer(empresa);
        List<OfertaMercadoServer> ofertasAcciones = getOfertasEmpresa(empresa);

        int accionesTotales = getAccionesTotales(empresa);

        Map<String, Integer> accionistasPeso = new HashMap<>();

        posicionesAcciones.forEach(posicion -> {
            accionistasPeso.put(posicion.getJugador(), (int) rentabilidad(accionesTotales, posicion.getCantidad()));
        });
        ofertasAcciones.forEach(oferta -> {
            if(accionistasPeso.get(oferta.getJugador()) == null){
                accionistasPeso.put(oferta.getJugador(), (int) rentabilidad(accionesTotales, oferta.getCantidad()));
            }else{
                int acciones = accionistasPeso.get(oferta.getJugador());

                accionistasPeso.put(oferta.getJugador(), (int) rentabilidad(accionesTotales, acciones + oferta.getCantidad()));
            }
        });

        return sortMapByValueDecre(accionistasPeso);
    }

    /**
     * Hay que hacer que el jugador puede ejercer un precio de venta
     */
    public void venderOfertaDesdeBolsaCartera (Player player, PosicionAbierta posicionAVender, double precio) {
        nueva(player.getName(), posicionAVender.getNombre_activo(), precio, posicionAVender.getCantidad(), JUGADOR, posicionAVender.getPrecio_apertura());
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

    public int getAccionesTotalesParaPagarDividendo (String empresa) {
        List<PosicionAbierta> posicionesAccion = posicionesAbiertasMySQL.getPosicionesAccionesServer(empresa);
        List<OfertaMercadoServer> ofertasAccion =  ofertasMercadoServerMySQL.getOfertasEmpresa(empresa, OfertaMercadoServer::esTipoOfertanteJugador);

        return getSumaTotalListInteger(posicionesAccion, PosicionAbierta::getCantidad) +
                getSumaTotalListInteger(ofertasAccion, OfertaMercadoServer::getCantidad);
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
