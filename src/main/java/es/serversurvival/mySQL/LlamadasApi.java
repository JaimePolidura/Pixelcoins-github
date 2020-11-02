package es.serversurvival.mySQL;

import es.serversurvival.apiHttp.IEXCloud_API;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.mySQL.tablasObjetos.LlamadaApi;
import org.bukkit.Bukkit;
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
    private LlamadasApi () {}

    public void nuevaLlamada(String simbolo, double precio, String tipo){
        executeUpdate("INSERT INTO llamadasapi (simbolo, precio, tipo) VALUES ('"+simbolo+"','"+precio+"','"+tipo+"')");
    }

    public void nuevaLlamada(String simbolo, double precio, String tipo, String nombreValor){
        executeUpdate("INSERT INTO llamadasapi (simbolo, precio, tipo, nombrevalor) VALUES ('"+simbolo+"','"+precio+"','"+tipo+"','"+nombreValor+"')");
    }

    public LlamadaApi getLlamadaAPI(String simbolo) {
        ResultSet rs = executeQuery("SELECT * FROM llamadasapi WHERE simbolo = '"+simbolo+"'");

        return (LlamadaApi) buildSingleObjectFromResultSet(rs);
    }

    public List<LlamadaApi> getTodasLlamadasApi(){
        ResultSet rs = executeQuery("SELECT * FROM llamadasapi");

        return buildListFromResultSet(rs);
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
        executeUpdate("UPDATE llamadasapi SET nombrevalor = '"+nombreValor+"' WHERE simbolo = '"+simbolo+"'");
    }

    public boolean estaReg (String simbolo) {
        return getLlamadaAPI(simbolo) != null;
    }

    public void actualizarSimbolo(String simbolo){
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            conectar();
            try{
                String tipo = this.getLlamadaAPI(simbolo).getTipo();
                double precio = posicionesAbiertasMySQL.getPrecioActual(simbolo, tipo);

                this.setPrecio(simbolo, precio);
            }catch (Exception e){
                e.printStackTrace();
            }
            desconectar();
        },0L);
    }

    public synchronized void actualizarPrecios(){
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            MySQL.conectar();
            List<LlamadaApi> llamadaApis = getTodasLlamadasApi();
            int actualizadas = 0;

            for (LlamadaApi llamadaApi : llamadaApis) {
                try {
                    double precio = posicionesAbiertasMySQL.getPrecioActual(llamadaApi.getSimbolo(), llamadaApi.getTipo());
                    setPrecio(llamadaApi.getSimbolo(), precio);

                    actualizadas++;
                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Error al actualizar el precio de " + llamadaApi.getSimbolo() + " " + e.getMessage());
                }
            }

            if(Bukkit.getOnlinePlayers().size() != 0){
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Se han actualizado: " + actualizadas + " accione(s) de " + llamadaApis.size());
            }
            MySQL.desconectar();
        }  , 0L);
    }

    public synchronized void actualizarNobreEmpresa (String simbolo) {
        conectar();

        LlamadaApi llamadasApi = getLlamadaAPI(simbolo);
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            conectar();
            try {
                String nombreEmpresa = IEXCloud_API.getNombreEmpresa(simbolo);
                this.setNombreValor(simbolo, nombreEmpresa);
            } catch (Exception e) {
                e.printStackTrace();
            }
            desconectar();
        }, 0L);
    }

    public void mostrarRatioPer (Player player, String ticker) {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            try{
                llamadasApiMySQL.conectar();
                LlamadaApi accion = getLlamadaAPI(ticker);

                double precioAccion = 0;
                String nombreValor;
                if(accion == null){
                    precioAccion = IEXCloud_API.getOnlyPrice(ticker);
                    nombreValor = IEXCloud_API.getNombreEmpresa(ticker);
                }else{
                    precioAccion = accion.getPrecio();
                    nombreValor = accion.getNombreValor();
                }

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
            llamadasApiMySQL.desconectar();
        }, 0L);
    }

    @Override
    protected LlamadaApi buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new LlamadaApi(rs.getString("simbolo"), rs.getDouble("precio"), rs.getString("tipo"), rs.getString("nombrevalor"));
    }
}