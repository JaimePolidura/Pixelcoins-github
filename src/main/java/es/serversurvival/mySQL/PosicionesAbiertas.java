package es.serversurvival.mySQL;

import es.serversurvival.main.Pixelcoin;
import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.apiHttp.IEXCloud_API;
import es.serversurvival.mySQL.tablasObjetos.LlamadaApi;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import es.serversurvival.util.Funciones;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static es.serversurvival.util.Funciones.*;

/**
 * 405 -> 246
 */
public final class PosicionesAbiertas extends MySQL {
    public final static PosicionesAbiertas INSTANCE = new PosicionesAbiertas();

    public static final double PORCENTAJE_CORTO = 5;

    private PosicionesAbiertas () {}

    public void nuevaPosicion(String jugador, TipoActivo tipoAcivo, String nombreActivo, int cantidad, double precioApertura, TipoPosicion tipoPosicion) {
        String fecha = dateFormater.format(new Date());

        executeUpdate("INSERT INTO posicionesabiertas (jugador, tipo_activo, nombre_activo, cantidad, precio_apertura, fecha_apertura, tipo_posicion) VALUES ('" + jugador + "','"+tipoAcivo.toString()+"','" + nombreActivo + "','" + cantidad + "','" + precioApertura + "', '" + fecha + "','"+tipoPosicion.toString()+"')");
    }

    public PosicionAbierta getPosicionAbierta(int id){
        return (PosicionAbierta) buildObjectFromQuery(String.format("SELECT * FROM posicionesabiertas WHERE id = '%d'", id));
    }

    public PosicionAbierta getPosicionAbierta (int id, String jugador) {
        return (PosicionAbierta) buildObjectFromQuery(String.format("SELECT * FROM posicionesabiertas WHERE id = '"+id+"' AND jugador = '%s'", jugador));
    }

    public PosicionAbierta getPosicionAbierta (int id, String jugador, TipoPosicion tipoPosicion) {
        return (PosicionAbierta) buildObjectFromQuery("SELECT * FROM posicionesabiertas WHERE id = '"+id+"' AND jugador = '"+jugador+"' AND tipo_posicion = '"+tipoPosicion.toString()+"'");
    }

    public List<PosicionAbierta> getPosicionesAbiertasJugador(String jugador){
        return buildListFromQuery(String.format("SELECT * FROM posicionesabiertas WHERE jugador = '%s'", jugador));
    }

    public List<PosicionAbierta> getPosicionesAbiertasJugadorCondicion (String jugador, Predicate<? super PosicionAbierta> condicion) {
        return getPosicionesAbiertasJugador(jugador).stream()
                .filter(condicion)
                .collect(Collectors.toList());
    }

    public List<PosicionAbierta> getTodasPosicionesAbiertas(){
        return buildListFromQuery("SELECT * FROM posicionesabiertas");
    }

    public List<PosicionAbierta> getTodasPosicionesAbiertasCondicion(Predicate<? super PosicionAbierta> condicion){
        return getTodasPosicionesAbiertas().stream()
                .filter(condicion)
                .collect(Collectors.toList());
    }

    public List<PosicionAbierta> getPosicionesAbiertasTipoActivo (String tipoActivo) {
        return buildListFromQuery(String.format("SELECT * FROM posicionesabiertas WHERE tipo_activo = '%s'", tipoActivo));
    }

    public void borrarPosicionAbierta(int id) {
        executeUpdate(String.format("DELETE FROM posicionesabiertas WHERE id = '%d'", id));
    }

    public boolean existeTicker(String nombre){
        return !isEmptyFromQuery(String.format("SELECT * FROM posicionesabiertas WHERE nombre_activo = '%s'", nombre));
    }

    public void setCantidad(int id, int cantidad) {
        executeUpdate(String.format("UPDATE posicionesabiertas SET cantidad = '%d' WHERE id = '%d'", cantidad, id));
    }

    public void setPrecioApertura (int id, double precio) {
        executeUpdate(String.format("UPDATE posicionesabiertas SET precio_apertura = '%d' WHERE id = '%d'", precio, id));
    }

    public void setJugador (String jugador, String nuevoJugador) {
        executeUpdate("UPDATE posicionesabiertas SET jugador = '"+nuevoJugador+"' WHERE jugador = '"+jugador+"'");
    }

    public List<PosicionAbierta> getPosicionAbierta (String owner, int cantidad, String ticker) {
        return buildListFromQuery("SELECT * FROM posicionesabiertas WHERE cantidad = '"+cantidad+"' AND jugador = '"+owner+"' AND nombre_activo = '"+ticker+"'");
    }

    public double getAllPixeloinsEnAcciones (String jugador) {
        List<PosicionAbierta> posLargas = getPosicionesAbiertasJugadorCondicion(jugador, PosicionAbierta::noEsTipoAccionServerYLargo);
        List<PosicionAbierta> posCortas = getPosicionesAbiertasJugadorCondicion(jugador, PosicionAbierta::esCorto);

        Map<String, LlamadaApi> llamadasApiMap = llamadasApiMySQL.getMapOfAllLlamadasApi();

        double pixelcoinsEnLargos = getSumaTotalListDouble(posLargas, pos -> llamadasApiMap.get(pos.getNombre_activo()).getPrecio() * pos.getCantidad());
        double pixelcoinsEnCortos = getSumaTotalListDouble(posCortas, pos -> (pos.getPrecio_apertura() - llamadasApiMap.get(pos.getNombre_activo()).getPrecio()) * pos.getCantidad());

        return pixelcoinsEnLargos + pixelcoinsEnCortos;
    }

    public Map<String, List<PosicionAbierta>> getAllPosicionesAbiertasMap (Predicate<? super PosicionAbierta> condition) {
        List<PosicionAbierta> posicionAbiertas = this.getTodasPosicionesAbiertas().stream()
                .filter(condition)
                .collect(Collectors.toList());

        Map<String, List<PosicionAbierta>> mapPosicionesAbiertas = new HashMap<>();

        posicionAbiertas.forEach(posicion -> {
            if(mapPosicionesAbiertas.get(posicion.getJugador()) == null){
                mapPosicionesAbiertas.put(posicion.getJugador(), Funciones.listOf(posicion));
            }else{
                List<PosicionAbierta> posicionAbiertasList = mapPosicionesAbiertas.get(posicion.getJugador());
                posicionAbiertasList.add(posicion);

                mapPosicionesAbiertas.replace(posicion.getJugador(), posicionAbiertasList);
            }
        });

        return mapPosicionesAbiertas;
    }

    public static String getNombreSimbolo(String simbolo){
        String toReturn = "";

        switch (simbolo){
            case "BTCUSD":
                toReturn = "Bitcoin";
                break;
            case "LTCUSD":
                toReturn = "Litecoin";
                break;
            case "ETHUSD":
                toReturn = "Etherium";
                break;
            case "DJFUELUSGULF":
                toReturn = "Queroseno";
                break;
            case "DCOILBRENTEU":
                toReturn =  "Petroleo";
                break;
            case "DHHNGSP":
                toReturn =  "Gas natural";
                break;
            case "GASDESW":
                toReturn = "Diesel";
                break;
            default:
                toReturn = simbolo;
        }

        return toReturn;
    }

    public Map<PosicionAbierta, Integer> getPosicionesAbiertasConPesoJugador(String jugador, double totalInverito) {
        List<PosicionAbierta> posicionAbiertasJugador = getPosicionesAbiertasJugadorCondicion(jugador, PosicionAbierta::noEsTipoAccionServer);
        Map<PosicionAbierta, Integer> posicionesAbiertasConPeso = new HashMap<>();

        posicionAbiertasJugador.forEach( (posicion) -> {
            posicionesAbiertasConPeso.put(posicion, (int) rentabilidad(totalInverito, posicion.getCantidad() * llamadasApiMySQL.getLlamadaAPI(posicion.getNombre_activo()).getPrecio()));
        });

        return posicionesAbiertasConPeso;
    }

    public synchronized void actualizarSplits () {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            conectar();

            Map<String, JSONObject> infoSplitsPorAccion = new HashMap<>();
            List<LlamadaApi> todasLlamadasApi = llamadasApiMySQL.getTodasLlamadasApiCondicion(LlamadaApi::esTipoAccion);

            todasLlamadasApi.forEach( (llamada) -> {
                try {
                    JSONObject infoSplit = IEXCloud_API.getSplitInfoEmpresa(llamada.getSimbolo());
                    infoSplitsPorAccion.put(llamada.getNombre_activo(), infoSplit);
                } catch (Exception ignored) {
                    //IGNORED
                }
            });

            conectar();

            List<PosicionAbierta> posicionAbiertas = getTodasPosicionesAbiertasCondicion(PosicionAbierta::esTipoAccion);
            posicionAbiertas.forEach( (posicionAbierta) -> {
                JSONObject infoSplit = infoSplitsPorAccion.get(posicionAbierta.getNombre_activo());

                if(infoSplit != null){
                    realizarSplit(posicionAbierta, infoSplit);
                }
            });

            desconectar();
        }, 0L);
    }

    private void realizarSplit(PosicionAbierta pos, JSONObject infoSplit) {
        try{
            Date fechaHoy = new Date();
            Date dateSplit = dateFormater.parse((String) infoSplit.get("date"));

            int denominador = (int) infoSplit.get("fromFactor");
            int numerador = (int) infoSplit.get("toFactor");

            if (diferenciaDias(fechaHoy, dateSplit) == 0) {
                int cantidadDeAccionesConvertibles = pos.getCantidad() - (pos.getCantidad() % denominador);
                int accionesSobrantes = pos.getCantidad() % denominador;
                int accionesConvertidas = (cantidadDeAccionesConvertibles / denominador) * numerador;

                double precioAperturaConvertido = pos.getPrecio_apertura() / (numerador / denominador);

                this.setCantidad(pos.getId(), accionesConvertidas + accionesSobrantes);
                this.setPrecioApertura(pos.getId(), precioAperturaConvertido);

                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha actualizado el split de " + pos.getNombre_activo());
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void pagarDividendos() {
        MySQL.conectar();
        Date hoy = new Date();
        List<PosicionAbierta> posicionAbiertas = getTodasPosicionesAbiertas().stream()
                .filter(PosicionAbierta::esLargo)
                .filter(PosicionAbierta::esTipoAccion)
                .collect(Collectors.toList());

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
        transaccionesMySQL.conectar();
        for(PosicionAbierta posicionAbierta : posicionAbiertas) {
            double dividendo;
            Date fechaPagoDividendos;

            try {
                JSONObject jsonDeLosDividendos = this.getJSONDividendos(posicionAbierta.getNombre_activo());
                dividendo = getCantidadDePagoDeDividendoDesdeJSON(jsonDeLosDividendos);
                fechaPagoDividendos = getFechaPagoDividendosJSON(jsonDeLosDividendos);
            } catch (Exception e) {
                continue;
            }

            if (diferenciaDias(hoy, fechaPagoDividendos) == 0) {
                transaccionesMySQL.pagaDividendo(posicionAbierta.getNombre_activo(), posicionAbierta.getJugador(), dividendo, posicionAbierta.getCantidad());
            }
        }

        },0L);

        MySQL.desconectar();
    }

    public void mostrarDividendosCarteraEntera (Player player) {
        String nombrePlayer = player.getName();

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            MySQL.conectar();

            Map<String, LlamadaApi> mapAllLlamadas = llamadasApiMySQL.getMapOfAllLlamadasApi();
            List<PosicionAbierta> posicionesTickers = getPosicionesAbiertasJugadorCondicion(nombrePlayer, PosicionAbierta::esLargo).stream()
                    .filter(PosicionAbierta::esTipoAccion)
                    .filter(distinctBy(PosicionAbierta::getNombre_activo))
                    .collect(Collectors.toList());

            MySQL.desconectar();

            player.sendMessage(ChatColor.GOLD + "------------------------------------");
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "     PROXIMOS DIVIDENDOS DE TU CARTERA");
            player.sendMessage("           ");
            for (PosicionAbierta posicion : posicionesTickers) {
                try {
                    JSONObject jsonDividendos = IEXCloud_API.getProximosDividendos(posicion.getNombre_activo());

                    Date fehcaPago = getFechaPagoDividendosJSON(jsonDividendos);
                    double cantidadDePago = Double.parseDouble((String) jsonDividendos.get("amount"));
        
                    player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + mapAllLlamadas.get(posicion.getNombre_activo()).getNombre_activo() + ChatColor.RESET + ChatColor.GOLD  + ": Proximo dividendo: " +
                            dateFormater.format(fehcaPago) + " a " + ChatColor.GREEN + formatea.format(cantidadDePago) + " PC" + ChatColor.GOLD +
                            "/Accion ( " + ChatColor.BOLD + ( (int) rentabilidad(mapAllLlamadas.get(posicion.getNombre_activo()).getPrecio(), cantidadDePago) )  + "%" + ChatColor.RESET + "" + ChatColor.GOLD + " )");
                } catch (Exception ignored) {
                    //IGNORED
                }
            }
            player.sendMessage(ChatColor.GOLD + "------------------------------------");
        }, 0L);
    }

    public void mostrarDividendoEmpresa (Player player, String ticker) {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            MySQL.conectar();
            LlamadaApi infoAccion = llamadasApiMySQL.getLlamadaAPI(ticker);
            MySQL.desconectar();

            try{
                JSONObject jsonDividendos = IEXCloud_API.getProximosDividendos(ticker);
                Date fechaPago = getFechaPagoDividendosJSON(jsonDividendos);
                double cantidadAPagar = Double.parseDouble((String) jsonDividendos.get("amount"));

                String nombreEmpresa;
                double precioPorAccion;

                if(infoAccion == null){
                    nombreEmpresa = IEXCloud_API.getNombreEmpresa(ticker);
                    precioPorAccion = IEXCloud_API.getOnlyPrice(ticker);
                }else{
                    nombreEmpresa = infoAccion.getNombre_activo();
                    precioPorAccion = infoAccion.getPrecio();
                }

                player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + nombreEmpresa + ChatColor.RESET + ChatColor.GOLD  + ": Proximo dividendo: " +
                        dateFormater.format(fechaPago) + " a " + ChatColor.GREEN + formatea.format(cantidadAPagar) + " PC" + ChatColor.GOLD +
                        "/Accion ( " + ChatColor.BOLD + ( (int) rentabilidad(precioPorAccion, cantidadAPagar) )  + "%" + ChatColor.RESET + "" + ChatColor.GOLD + " )");

            }catch (Exception e) {
                player.sendMessage(ChatColor.DARK_RED + "No se ha encontrado ningun dividendo proximamante para esa accion");
            }
        }, 0L);
    }

    private double getCantidadDePagoDeDividendoDesdeJSON(JSONObject json) {
        return (double) json.get("amount");
    }

    private Date getFechaPagoDividendosJSON (JSONObject json) throws ParseException {
        return dateFormater.parse((String) json.get("paymentDate"));
    }

    private JSONObject getJSONDividendos (String ticker) throws Exception {
        JSONArray jsonArray = IEXCloud_API.getDividendo(ticker, "week");
        return (JSONObject) jsonArray.get(0);
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
