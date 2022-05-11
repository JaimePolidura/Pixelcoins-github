package es.serversurvival.bolsa.other._shared.ofertasmercadoserver.mysql;

import es.jaimetruman.delete.Delete;
import es.jaimetruman.insert.Insert;
import es.jaimetruman.select.Select;
import es.jaimetruman.select.SelectOptionInitial;
import es.jaimetruman.update.Update;
import es.jaimetruman.update.UpdateOptionInitial;
import es.serversurvival.bolsa.other._shared.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival._shared.mysql.MySQLRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static es.serversurvival._shared.utils.Funciones.*;

public final class OfertasMercadoServer extends MySQLRepository {
    public static final OfertasMercadoServer INSTANCE = new OfertasMercadoServer();
    private final SelectOptionInitial select;
    private final UpdateOptionInitial update;

    private OfertasMercadoServer() {
        this.select = Select.from("ofertasbolsaserver");
        this.update = Update.table("ofertasbolsaserver");
    }

    public void nueva (String jugador, String empresa, double precio, int cantidad, TipoOfertante tipoOfertante, double precioApertura) {
        String fecha = dateFormater.format(new Date());

        String insertQuery = Insert.table("ofertasbolsaserver")
                .fields("jugador", "empresa", "precio", "cantidad", "fecha", "tipo_ofertante", "precio_apertura")
                .values(jugador, empresa, precio, cantidad, fecha, tipoOfertante.toString(), precioApertura);

        executeUpdate(insertQuery);
    }

    public List<OfertaMercadoServer> getAll () {
        return buildListFromQuery(select);
    }

    public List<OfertaMercadoServer> getOfertasEmpresa (String empresa) {
        return buildListFromQuery(select.where("empresa").equal(empresa));
    }

    public List<OfertaMercadoServer> getOfertasJugador (String jugador) {
        return buildListFromQuery(select.where("jugador").equal(jugador));
    }

    public List<OfertaMercadoServer> getOfertasEmpresa (String empresa, Predicate<? super OfertaMercadoServer> condition) {
        List<OfertaMercadoServer> ofertas = buildListFromQuery(select.where("empresa").equal(empresa));

        return ofertas.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public OfertaMercadoServer get (int id) {
        return (OfertaMercadoServer) buildObjectFromQuery(select.where("id").equal(id));
    }

    public void setCantidad (int id, int cantidad) {
        executeUpdate(update.set("cantidad", cantidad).where("id").equal(id));
    }

    public void cambiarNombreJugador (String antiguoNombre, String nuevoNombre) {
        executeUpdate(update.set("jugador", nuevoNombre).where("jugador").equal(antiguoNombre).and("tipo_ofertante").equal("JUGADOR"));
    }

    public void borrar (int id) {
        executeUpdate(Delete.from("ofertasbolsaserver").where("id").equal(id));
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
        List<OfertaMercadoServer> ofertas = getOfertasEmpresa(empresa, oferta -> oferta.getTipo_ofertante() == TipoOfertante.EMPRESA);

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
