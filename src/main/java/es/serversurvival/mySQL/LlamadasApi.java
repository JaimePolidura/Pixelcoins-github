package es.serversurvival.mySQL;

import es.jaime.EventListener;
import es.serversurvival.apiHttp.IEXCloud_API;
import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.mySQL.eventos.bolsa.PosicionAbiertaEvento;
import es.serversurvival.mySQL.eventos.bolsa.PosicionCerradaEvento;
import es.serversurvival.mySQL.tablasObjetos.LlamadaApi;
import es.serversurvival.util.Funciones;
import javafx.util.Pair;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * II 156 -> 121
 */
public final class LlamadasApi extends MySQL {
    public final static LlamadasApi INSTANCE = new LlamadasApi();

    @EventListener
    public void onOpenedPosition (PosicionAbiertaEvento event) {
        this.nuevaLlamadaSiNoEstaReg(event.getTicker(), event.getPrecioUnidad(), event.getTipoActivo(), event.getNombreValor());
    }

    @EventListener
    public void onClosedPosition (PosicionCerradaEvento evento) {
        this.borrarLlamadaSiNoEsUsada(evento.getTicker());
    }

    public void nuevaLlamada(String simbolo, double precio, TipoActivo tipo, String nombreValor){
        executeUpdate("INSERT INTO llamadasapi (simbolo, precio, tipo_activo, nombre_activo) VALUES ('"+simbolo+"','"+precio+"','"+tipo.toString()+"','"+nombreValor+"')");
    }

    public LlamadaApi getLlamadaAPI(String simbolo) {
        return (LlamadaApi) buildObjectFromQuery("SELECT * FROM llamadasapi WHERE simbolo = '"+simbolo+"'");
    }

    public List<LlamadaApi> getTodasLlamadasApi(){
        return buildListFromQuery("SELECT * FROM llamadasapi");
    }

    public List<LlamadaApi> getTodasLlamadasApiCondicion (Predicate<? super LlamadaApi> condicion) {
        return getTodasLlamadasApi().stream()
                .filter(condicion)
                .collect(Collectors.toList());
    }

    public Map<String, LlamadaApi> getMapOfAllLlamadasApi () {
        Map<String, LlamadaApi> mapLlamadas = new HashMap<>();

        List<LlamadaApi> llamadaApiList = getTodasLlamadasApi();
        llamadaApiList.forEach( llamada -> mapLlamadas.put(llamada.getSimbolo(), llamada) );

        return mapLlamadas;
    }

    public void borrarLlamada(String simbolo){
        executeUpdate(String.format("DELETE FROM llamadasapi WHERE simbolo = '%s'", simbolo));
    }

    public void setPrecio(String simbolo, double precio){
        executeUpdate("UPDATE llamadasapi SET precio = '"+precio+"' WHERE simbolo = '"+simbolo+"'");
    }

    public void setNombreValor(String simbolo, String nombreValor){
        executeUpdate("UPDATE llamadasapi SET nombre_activo = '"+nombreValor+"' WHERE simbolo = '"+simbolo+"'");
    }

    public boolean estaReg (String simbolo) {
        return getLlamadaAPI(simbolo) != null;
    }

    public void borrarLlamadaSiNoEsUsada (String ticker) {
        if(!posicionesAbiertasMySQL.existeTicker(ticker))
            borrarLlamada(ticker);
    }

    public void nuevaLlamadaSiNoEstaReg(String ticker, double precio, TipoActivo tipoactivo, String nombrevalor) {
        if(!estaReg(ticker))
            nuevaLlamada(ticker, precio, tipoactivo, nombrevalor);
    }

    public Optional<Pair<String, Double>> getPairNombreValorPrecio (String ticker) {
        try{
            double precio;
            String nombreValor;

            if(estaReg(ticker)){
                LlamadaApi llamadaApi = this.getLlamadaAPI(ticker);
                nombreValor = llamadaApi.getNombre_activo();
                precio = llamadaApi.getPrecio();
            }else{
                nombreValor = IEXCloud_API.getNombreEmpresa(ticker);
                precio = IEXCloud_API.getOnlyPrice(ticker);
            }

            return Optional.of(new Pair<>(nombreValor, precio));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public synchronized void actualizarPrecios (){
        List<LlamadaApi> llamadaApis = getTodasLlamadasApi();

        for (LlamadaApi llamadaApi : llamadaApis) {
            double precio = llamadaApi.getTipo_activo().getPrecio(llamadaApi.getSimbolo());

            this.setPrecio(llamadaApi.getSimbolo(), precio);
        }
    }

    public void mostrarRatioPer (Player player, String ticker) {
        Funciones.POOL.submit(() -> {
            try{
                Pair<String, Double> pairNombreValorPrecio = this.getPairNombreValorPrecio(ticker).get();

                String nombreValor = pairNombreValorPrecio.getKey();
                double precioAccion = pairNombreValorPrecio.getValue();
                double eps = IEXCloud_API.getEPS(ticker);

                if(eps != 0){
                    int ratioPer = (int) (precioAccion / eps);
                    player.sendMessage(ChatColor.GOLD + nombreValor + " cotiza a " + ChatColor.GREEN + formatea.format(precioAccion) + "PC" + ChatColor.GOLD + " por lo cual el PER es " + ChatColor.GREEN + formatea.format(ratioPer) + "x");
                }else{
                    player.sendMessage(ChatColor.GOLD + nombreValor + " cotiza a " + ChatColor.GREEN + formatea.format(precioAccion) + "PC" + ChatColor.GOLD + " por lo cual el PER es " + ChatColor.RED   + "infinito (no ganadinero xd)");
                }

            }catch (Exception e) {
                player.sendMessage(ChatColor.DARK_RED + "No se ha encontrado la accion. Para buscar el ticker lo pudes hacer en internet o el /bolsa valores. Recuerda que solo se pueden empresas que cotizen en EEUU");
            }
        });
    }

    @Override
    protected LlamadaApi buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new LlamadaApi(rs.getString("simbolo"),
                rs.getDouble("precio"),
                TipoActivo.valueOf(rs.getString("tipo_activo")),
                rs.getString("nombre_activo"));
    }
}
