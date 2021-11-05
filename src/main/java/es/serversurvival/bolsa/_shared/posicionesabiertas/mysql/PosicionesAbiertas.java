package es.serversurvival.bolsa._shared.posicionesabiertas.mysql;

import es.jaimetruman.delete.Delete;
import es.jaimetruman.insert.Insert;
import es.jaimetruman.select.Select;
import es.jaimetruman.select.SelectOptionInitial;
import es.jaimetruman.update.Update;
import es.jaimetruman.update.UpdateOptionInitial;
import es.serversurvival.bolsa._shared.llamadasapi.mysql.LlamadasApi;
import es.serversurvival.bolsa._shared.llamadasapi.mysql.LlamadaApi;
import es.serversurvival.bolsa._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.shared.mysql.MySQL;
import es.serversurvival.shared.utils.Funciones;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static es.serversurvival.shared.utils.Funciones.*;
import static java.lang.Math.abs;

/**
 * 405 -> 246
 */
public final class PosicionesAbiertas extends MySQL {
    public final static PosicionesAbiertas INSTANCE = new PosicionesAbiertas();
    private final SelectOptionInitial select;
    private final UpdateOptionInitial update;

    public static final double PORCENTAJE_CORTO = 5;

    private PosicionesAbiertas () {
        this.select = Select.from("posicionesabiertas");
        this.update = Update.table("posicionesabiertas");
    }

    public void nuevaPosicion(String jugador, TipoActivo tipoAcivo, String nombreActivo, int cantidad, double precioApertura, TipoPosicion tipoPosicion) {
        String fecha = dateFormater.format(new Date());
        String insertQuery = Insert.table("posicionesabiertas")
                .fields("jugador", "tipo_activo", "nombre_activo", "cantidad", "precio_apertura", "fecha_apertura", "tipo_posicion")
                .values(jugador, tipoAcivo, nombreActivo, cantidad, precioApertura, fecha, tipoPosicion.toString());

        executeUpdate(insertQuery);
    }

    public PosicionAbierta getPosicionAbierta(int id){
        return (PosicionAbierta) buildObjectFromQuery(select.where("id").equal(id));
    }

    public PosicionAbierta getPosicionAbierta (int id, String jugador) {
        return (PosicionAbierta) buildObjectFromQuery(select.where("id").equal(id).and("jugador").equal(jugador));
    }

    public PosicionAbierta getPosicionAbierta (int id, String jugador, TipoPosicion tipoPosicion) {
        return (PosicionAbierta) buildObjectFromQuery(select.where("id").equal(id).and("jugador").equal(jugador).and("tipo_posicion").equal(tipoPosicion.toString()));
    }

    public List<PosicionAbierta> getPosicionesAbiertasJugador(String jugador){
        return buildListFromQuery(select.where("jugador").equal(jugador));
    }

    public List<PosicionAbierta> getPosicionesAbiertasJugadorCondicion (String jugador, Predicate<? super PosicionAbierta> condicion) {
        return getPosicionesAbiertasJugador(jugador).stream()
                .filter(condicion)
                .collect(Collectors.toList());
    }

    public List<PosicionAbierta> getTodasPosicionesAbiertas(){
        return buildListFromQuery(select);
    }

    public List<PosicionAbierta> getTodasPosicionesAbiertasCondicion(Predicate<? super PosicionAbierta> condicion){
        return getTodasPosicionesAbiertas().stream()
                .filter(condicion)
                .collect(Collectors.toList());
    }

    public List<PosicionAbierta> getPosicionesAbiertasTipoActivo (String tipoActivo) {
        return buildListFromQuery(select.where("tipo_activo").equal(tipoActivo));
    }

    public void borrarPosicionAbierta(int id) {
        executeUpdate(Delete.from("posicionesabiertas").where("id").equal(id));
    }

    public boolean existeTicker(String nombre){
        return !isEmptyFromQuery(select.where("nombre_activo").equal(nombre));
    }

    public void setCantidad(int id, int cantidad) {
        executeUpdate(update.set("cantidad", cantidad).where("id").equal(id));
    }

    public void setPrecioApertura (int id, double precio) {
        executeUpdate(update.set("precio_apertura", precio).where("id").equal(id));
    }

    public void setJugador (String jugador, String nuevoJugador) {
        executeUpdate("UPDATE posicionesabiertas SET jugador = '"+nuevoJugador+"' WHERE jugador = '"+jugador+"'");
    }

    public List<PosicionAbierta> getPosicionAbierta (String owner, int cantidad, String ticker) {
        return buildListFromQuery(select.where("cantidad").equal(cantidad).and("jugador").equal(owner).and("nombre_activo").equal(ticker));
    }

    public List<PosicionAbierta> getPosicionesAccionesServer(String empresa) {
        return buildListFromQuery(select.where("nombre_activo").equal(empresa).and("tipo_activo").equal("ACCIONES_SERVER"));
    }

    public List<PosicionAbierta> getPosicionesServerJugador (String jugador) {
        return buildListFromQuery(select.where("jugador").equal(jugador).and("tipo_activo").equal("ACCIONES_SERVER"));
    }

    public Map<String, List<PosicionAbierta>> getAllPosicionesAbiertasMap (Predicate<? super PosicionAbierta> condition) {
        List<PosicionAbierta> posicionAbiertas = this.getTodasPosicionesAbiertas().stream()
                .filter(condition)
                .collect(Collectors.toList());

        return Funciones.mergeMapList(posicionAbiertas, PosicionAbierta::getJugador);
    }

    public static String getNombreSimbolo (String ticker) {
        String nombreSimbolo = MateriasPrimas.getNombreValor(ticker);

        if(nombreSimbolo.equalsIgnoreCase(ticker)){
            nombreSimbolo = Criptomonedas.getNombreValor(ticker);
        }

        return nombreSimbolo;
    }

    public double getAllPixeloinsEnAcciones (String jugador) {
        List<PosicionAbierta> posLargas = getPosicionesAbiertasJugadorCondicion(jugador, PosicionAbierta::noEsTipoAccionServerYLargo);
        List<PosicionAbierta> posCortas = getPosicionesAbiertasJugadorCondicion(jugador, PosicionAbierta::esCorto);

        Map<String, LlamadaApi> llamadasApiMap = llamadasApiMySQL.getMapOfAllLlamadasApi();

        double pixelcoinsEnLargos =
                getSumaTotalListDouble(posLargas, pos -> llamadasApiMap.get(pos.getNombre_activo()).getPrecio() * pos.getCantidad());
        double pixelcoinsEnCortos =
                getSumaTotalListDouble(posCortas, pos -> (pos.getPrecio_apertura() - llamadasApiMap.get(pos.getNombre_activo()).getPrecio()) * pos.getCantidad());

        return pixelcoinsEnLargos + pixelcoinsEnCortos;
    }

    public Map<PosicionAbierta, Integer> getPosicionesAbiertasConPesoJugador(String jugador, double totalInverito) {
        List<PosicionAbierta> posicionAbiertasJugador = getPosicionesAbiertasJugadorCondicion(jugador, PosicionAbierta::noEsTipoAccionServer);

        Map<PosicionAbierta, Integer> posicionesAbiertasConPeso = new HashMap<>();

        posicionAbiertasJugador.forEach( (posicion) -> {
            posicionesAbiertasConPeso.put(posicion, (int)
                    rentabilidad(totalInverito, posicion.getCantidad() * llamadasApiMySQL.getLlamadaAPI(posicion.getNombre_activo()).getPrecio()));
        });

        return posicionesAbiertasConPeso;
    }

    public Map<PosicionAbierta, Double> calcularTopPosicionesAbiertas (String jugador) {
        List<PosicionAbierta> posicionAbiertas = AllMySQLTablesInstances.posicionesAbiertasMySQL.getPosicionesAbiertasJugadorCondicion
                (jugador, PosicionAbierta::noEsTipoAccionServerYLargo);

        Map<PosicionAbierta, Double> posicionAbiertasConRentabilidad = new HashMap<>();

        for (PosicionAbierta posicion : posicionAbiertas) {
            double precioInicial = posicion.getPrecio_apertura();
            double precioActual = LlamadasApi.INSTANCE.getLlamadaAPI(posicion.getNombre_activo()).getPrecio();
            double rentabildad;

            if(posicion.getTipo_posicion() == TipoPosicion.LARGO){
                rentabildad = redondeoDecimales(diferenciaPorcntual(precioInicial, precioActual), 2);
            }else{
                rentabildad = abs(redondeoDecimales(diferenciaPorcntual(precioActual, precioInicial), 2));
            }

            posicionAbiertasConRentabilidad.put(posicion, rentabildad);
        }

        return sortMapByValueDecre(posicionAbiertasConRentabilidad);
    }

    @Override
    protected PosicionAbierta buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new PosicionAbierta(rs.getInt("id"),
                rs.getString("jugador"),
                TipoActivo.valueOf(rs.getString("tipo_activo")),
                rs.getString("nombre_activo"),
                rs.getInt("cantidad"),
                rs.getDouble("precio_apertura"),
                rs.getString("fecha_apertura"),
                TipoPosicion.valueOf(rs.getString("tipo_posicion")));
    }
}
