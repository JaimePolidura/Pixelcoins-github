package es.serversurvival.mySQL;

import com.sun.org.apache.regexp.internal.REDebugCompiler;
import es.serversurvival.apiHttp.FinancialModelingGrep;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.mySQL.enums.POSICION;
import es.serversurvival.mySQL.enums.VALORES;
import es.serversurvival.util.Funciones;
import es.serversurvival.apiHttp.IEXCloud_API;
import es.serversurvival.mySQL.tablasObjetos.LlamadaApi;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static es.serversurvival.mySQL.enums.POSICION.*;
import static es.serversurvival.util.Funciones.*;

/**
 * 405 -> 246
 */
public final class PosicionesAbiertas extends MySQL {
    public final static PosicionesAbiertas INSTANCE = new PosicionesAbiertas();

    public static final double PORCENTAJE_CORTO = 5;

    private PosicionesAbiertas () {}

    public PosicionAbierta nuevaPosicion(String jugador, String tipo, String nombre, int cantidad, double precioApertura, POSICION tipoPosicion) {
        String fecha = dateFormater.format(new Date());

        executeUpdate("INSERT INTO posicionesabiertas (jugador, tipo, nombre, cantidad, precioApertura, fechaApertura, tipoPosicion) VALUES ('" + jugador + "','"+tipo+"','" + nombre + "','" + cantidad + "','" + precioApertura + "', '" + fecha + "','"+tipoPosicion.toString()+"')");
        return new PosicionAbierta(getMaxId(), jugador, VALORES.ACCIONES.toString(), nombre, cantidad, precioApertura, fecha, tipoPosicion.toString());
    }

    public PosicionAbierta getPosicionAbierta(int id){
        ResultSet rs = executeQuery(String.format("SELECT * FROM posicionesabiertas WHERE id = '%d'", id));

        return (PosicionAbierta) buildSingleObjectFromResultSet(rs);
    }

    public List<PosicionAbierta> getPosicionesAbiertasJugador(String jugador){
        ResultSet rs = executeQuery(String.format("SELECT * FROM posicionesabiertas WHERE jugador = '%s'", jugador));

        return buildListFromResultSet(rs);
    }

    public List<PosicionAbierta> getPosicionesAbiertasJugadorCondicion (String jugador, Predicate<? super PosicionAbierta> condicion) {
        return getPosicionesAbiertasJugador(jugador).stream()
                .filter(condicion)
                .collect(Collectors.toList());
    }

    public List<PosicionAbierta> getTodasPosicionesAbiertas(){
        ResultSet rs = executeQuery("SELECT * FROM posicionesabiertas");

        return buildListFromResultSet(rs);
    }

    public List<PosicionAbierta> getTodasPosicionesAbiertasCondicion(Predicate<? super PosicionAbierta> condicion){
        return getTodasPosicionesAbiertas().stream()
                .filter(condicion)
                .collect(Collectors.toList());
    }

    public List<PosicionAbierta> getPosicionesAbiertasTipo (String tipo) {
        ResultSet rs = executeQuery(String.format("SELECT * FROM posicionesabiertas WHERE tipo = '%s'", tipo));

        return buildListFromResultSet(rs);
    }

    public List<PosicionAbierta> getPosicionesAbiertasCortas () {
        ResultSet rs = executeQuery("SELECT * FROM posicionesabiertas WHERE tipoPosicion = 'CORTO'");

        return buildListFromResultSet(rs);
    }

    public void borrarPosicionAbierta(int id) {
        executeUpdate(String.format("DELETE FROM posicionesabiertas WHERE id = '%d'", id));
    }

    public boolean existeTicker(String nombre){
        try{
            ResultSet rs = executeQuery(String.format("SELECT * FROM posicionesabiertas WHERE nombre = '%s'", nombre));
            return rs.next();
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existe(int id) {
        return getPosicionAbierta(id) != null;
    }

    public void setCantidad(int id, int cantidad) {
        executeUpdate(String.format("UPDATE posicionesabiertas SET cantidad = '%d' WHERE id = '%d'", cantidad, id));
    }

    public void setPrecioApertura (int id, double precio) {
        executeUpdate(String.format("UPDATE posicionesabiertas SET precioApertura = '%d' WHERE id = '%d'", precio, id));
    }

    public void setJugador (String jugador, String nuevoJugador) {
        executeUpdate("UPDATE posicionesabiertas SET jugador = '"+nuevoJugador+"' WHERE jugador = '"+jugador+"'");
    }

    public double getAllPixeloinsEnAcciones (String jugador) {
        List<PosicionAbierta> posiciones = getPosicionesAbiertasJugadorCondicion(jugador, PosicionAbierta::esLargo);

        return getSumaTotalListDouble(posiciones, (pos) -> llamadasApiMySQL.getLlamadaAPI(pos.getNombre()).getPrecio() * pos.getCantidad());
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

    public double getPrecioActual(String simbolo, String tipo) throws Exception {
        double precio = -1;

        switch (tipo.toUpperCase()){
            case "ACCIONES":
                precio = IEXCloud_API.getOnlyPrice(simbolo);
                break;
            case "CRIPTOMONEDAS":
                precio = IEXCloud_API.getPrecioCriptomoneda(simbolo);
                break;
            case "MATERIAS_PRIMAS":
                precio = IEXCloud_API.getPrecioMateriaPrima(simbolo);
                break;
            case "INDICES":
                precio = getPrecioIndice(simbolo);
                break;
        }
        return precio;
    }

    public double getPesoAccionEnCartera(int id){
        PosicionAbierta posicionAMedir = this.getPosicionAbierta(id);
        String jugador = posicionAMedir.getJugador();

        LlamadaApi precioEnLaAPI = llamadasApiMySQL.getLlamadaAPI(posicionAMedir.getNombre());

        double invertidoEnAccion = posicionAMedir.getCantidad() * precioEnLaAPI.getPrecio();

        List<PosicionAbierta> posicionesJugador = this.getPosicionesAbiertasJugador(jugador);
        double totalInvertido = posicionesJugador.stream()
                .mapToDouble((pos) -> pos.getCantidad() * llamadasApiMySQL.getLlamadaAPI(pos.getNombre()).getPrecio())
                .sum();

        return rentabilidad(totalInvertido, invertidoEnAccion);
    }

    public Map<PosicionAbierta, Integer> getPosicionesAbiertasConPesoJugador(String jugador, double totalInverito) {
        List<PosicionAbierta> posicionAbiertasJugador = getPosicionesAbiertasJugador(jugador);
        Map<PosicionAbierta, Integer> posicionesAbiertasConPeso = new HashMap<>();

        posicionAbiertasJugador.forEach( (posicion) -> {
            posicionesAbiertasConPeso.put(posicion, (int) rentabilidad(totalInverito, posicion.getCantidad() * llamadasApiMySQL.getLlamadaAPI(posicion.getNombre()).getPrecio()));
        });

        return posicionesAbiertasConPeso;
    }

    public synchronized void actualizarSplits () {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            conectar();

            Map<String, JSONObject> infoSplitsPorAccion = new HashMap<>();
            List<LlamadaApi> todasLlamadasApi = llamadasApiMySQL.getTodasLlamadasApiCondicion(LlamadaApi::esTipoAccion);

            todasLlamadasApi.forEach( (llamada) -> {
                if(infoSplitsPorAccion.get(llamada.getSimbolo()) != null){
                    try {
                        JSONObject infoSplit = IEXCloud_API.getSplitInfoEmpresa(llamada.getSimbolo());
                        infoSplitsPorAccion.put(llamada.getNombreValor(), infoSplit);
                    } catch (Exception ignored) {
                        //IGNORED
                    }
                }
            });

            List<PosicionAbierta> posicionAbiertas = getTodasPosicionesAbiertasCondicion(PosicionAbierta::esTipoAccion);
            posicionAbiertas.forEach( (pos) -> {
                JSONObject infoSplit = infoSplitsPorAccion.get(pos.getNombre());

                if(infoSplit != null){
                    actualizarAccionSplit(pos, infoSplit);
                }
            });

            desconectar();
            }, 0L);
    }

    private void actualizarAccionSplit (PosicionAbierta pos, JSONObject infoSplit) {
        try{
            Date fechaHoy = new Date();
            Date dateSplit = dateFormater.parse((String) infoSplit.get("date"));

            int denominador = (int) infoSplit.get("fromFactor");
            int numerador = (int) infoSplit.get("toFactor");

            if (diferenciaDias(fechaHoy, dateSplit) == 0) {
                int cantidadDeAccionesConvertibles = pos.getCantidad() - (pos.getCantidad() % denominador);
                int accionesSobrantes = pos.getCantidad() % denominador;
                int accionesConvertidas = (cantidadDeAccionesConvertibles / denominador) * numerador;

                double precioAperturaConvertido = pos.getPrecioApertura() / (numerador / denominador);

                this.setCantidad(pos.getId(), accionesConvertidas + accionesSobrantes);
                this.setPrecioApertura(pos.getId(), precioAperturaConvertido);

                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha actualizado el split de " + pos.getNombre());
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void pagarDividendos() {
        MySQL.conectar();
        List<PosicionAbierta> posicionAbiertas = getPosicionesAbiertasTipo(VALORES.ACCIONES.toString());

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            transaccionesMySQL.conectar();
            for(PosicionAbierta posicionAbierta : posicionAbiertas) {
                if(posicionAbierta.esCorto()) continue;

                double dividendo;
                Date fechaPagoDividendos;

                try {
                    JSONObject jsonDeLosDividendos = this.getJSONDividendos(posicionAbierta.getNombre());
                    dividendo = getCantidadDePagoDeDividendoDesdeJSON(jsonDeLosDividendos);
                    fechaPagoDividendos = getFechaPagoDividendosJSON(jsonDeLosDividendos);
                } catch (Exception e) {
                    continue;
                }

                if (diferenciaDias(new Date(), fechaPagoDividendos) == 0) {
                    transaccionesMySQL.pagaDividendo(posicionAbierta.getNombre(), posicionAbierta.getJugador(), dividendo, posicionAbierta.getCantidad());
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
                    .filter(distinctBy(PosicionAbierta::getNombre))
                    .collect(Collectors.toList());
            MySQL.desconectar();

            player.sendMessage(ChatColor.GOLD + "------------------------------------");
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "     PROXIMOS DIVIDENDOS DE TU CARTERA");
            player.sendMessage("           ");
            for (PosicionAbierta posicion : posicionesTickers) {
                try {
                    JSONObject jsonDividendos = IEXCloud_API.getProximosDividendos(posicion.getNombre());

                    Date fehcaPago = getFechaPagoDividendosJSON(jsonDividendos);
                    double cantidadDePago = Double.parseDouble((String) jsonDividendos.get("amount"));
        
                    player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + mapAllLlamadas.get(posicion.getNombre()).getNombreValor() + ChatColor.RESET + ChatColor.GOLD  + ": Proximo dividendo: " +
                            dateFormater.format(fehcaPago) + " a " + ChatColor.GREEN + formatea.format(cantidadDePago) + " PC" + ChatColor.GOLD +
                            "/Accion ( " + ChatColor.BOLD + ( (int) rentabilidad(mapAllLlamadas.get(posicion.getNombre()).getPrecio(), cantidadDePago) )  + "%" + ChatColor.RESET + "" + ChatColor.GOLD + " )");
                } catch (Exception ignored) {
                }
            }
            player.sendMessage(ChatColor.GOLD + "------------------------------------");
        }, 0L);
    }

    public void mostrarDividendoEmpresa (Player player, String ticker) {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            try{
                JSONObject jsonDividendos = IEXCloud_API.getProximosDividendos(ticker);

                Date fechaPago = getFechaPagoDividendosJSON(jsonDividendos);
                double cantidadAPagar = Double.parseDouble((String) jsonDividendos.get("amount"));

                MySQL.conectar();
                LlamadaApi infoAccion = llamadasApiMySQL.getLlamadaAPI(ticker);
                MySQL.desconectar();

                String nombreEmpresa;
                double precioPorAccion;
                if(infoAccion == null){
                    nombreEmpresa = IEXCloud_API.getNombreEmpresa(ticker);
                    precioPorAccion = IEXCloud_API.getOnlyPrice(ticker);
                }else{
                    nombreEmpresa = infoAccion.getNombreValor();
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

    public String getTipoSimbolo (String simbolo) {
        String tipo = getNombreSimbolo(simbolo);

        if(tipo.equalsIgnoreCase(simbolo)){
            tipo = "ACCIONES";
        }

        return tipo;
    }

    private int getMaxId(){
        ResultSet rs = executeQuery("SELECT * FROM posicionesabiertas ORDER BY id DESC LIMIT 1");

        return ( (PosicionAbierta) buildSingleObjectFromResultSet(rs)).getId();
    }

    private double getPrecioIndice (String ticker) throws Exception {
        JSONObject jsonObject = (JSONObject) FinancialModelingGrep.getPrecioIndice("%5E" + ticker).get(0);
        return (double) jsonObject.get("price");
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
                rs.getString("tipo"),
                rs.getString("nombre"),
                rs.getInt("cantidad"),
                rs.getDouble("precioApertura"),
                rs.getString("fechaApertura"),
                rs.getString("tipoPosicion"));
    }
}