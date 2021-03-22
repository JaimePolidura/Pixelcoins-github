package es.serversurvival.objetos.mySQL;

import es.serversurvival.main.Pixelcoin;
import es.serversurvival.objetos.mySQL.tablasObjetos.LlamadaApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class LlamadasApi extends MySQL {
    public void nuevaLlamada(String simbolo, double precio, String tipo){
        executeUpdate("INSERT INTO llamadasapi (simbolo, precio, tipo) VALUES ('"+simbolo+"','"+precio+"','"+tipo+"')");
    }

    public void borrarLlamada(String simbolo){
        executeUpdate(String.format("DELETE FROM llamadasapi WHERE simbolo = '%s'", simbolo));
    }

    public LlamadaApi getLlamadaAPI(String simbolo){
        try{
            ResultSet rs = executeQuery(String.format("SELECT * FROM llamadasapi WHERE simbolo = '%s'", simbolo));
            while (rs.next()){
                return buildLlamadaApiByResultset(rs);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void setPrecio(String simbolo, double precio){
        try{
            String constula = "UPDATE llamadasapi SET precio = ? WHERE simbolo = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(constula);
            preparedStatement.setDouble(1, precio);
            preparedStatement.setString(2, simbolo);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<LlamadaApi> getTodasLlamaasapi(){
        List<LlamadaApi> llamadaApis = new ArrayList<>();
        try{
            ResultSet rs = executeQuery("SELECT * FROM llamadasapi");

            while (rs.next()){
                llamadaApis.add(buildLlamadaApiByResultset(rs));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return llamadaApis;
    }

    public boolean estaReg (String simbolo) {
        return getLlamadaAPI(simbolo) != null;
    }

    public void actualizarSimbolo(String simbolo){
        PosicionesAbiertas posicionesAbiertasMySQL = new PosicionesAbiertas();
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

    public synchronized void actualizar(Server server){
        PosicionesAbiertas posicionesAbiertasMySQL = new PosicionesAbiertas();
        conectar();
        List<LlamadaApi> llamadaApis = getTodasLlamaasapi();
        desconectar();

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            conectar();
            llamadaApis.forEach( (llamadaApi) -> {
                try {
                    double precio = posicionesAbiertasMySQL.getPrecioActual(llamadaApi.getSimbolo(), llamadaApi.getTipo());
                    this.setPrecio(llamadaApi.getSimbolo(), precio);

                    server.getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha actualizado el precio de " + llamadaApi.getSimbolo());
                } catch (Exception e) {
                    e.printStackTrace();
                    server.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Error al actualizar el precio de " + llamadaApi.getSimbolo());
                }
                desconectar();
            });
        }  , 0L);
    }

    private LlamadaApi buildLlamadaApiByResultset (ResultSet rs) throws SQLException {
        return new LlamadaApi(rs.getString("simbolo"), rs.getDouble("precio"), rs.getString("tipo"));
    }
}