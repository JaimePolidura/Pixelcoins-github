package es.serversurvival.objetos.mySQL;

import es.serversurvival.main.Pixelcoin;
import es.serversurvival.objetos.mySQL.tablasObjetos.LlamadaApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LlamadasApi extends MySQL {
    public void nuevaLlamada(String simbolo, double precio, String tipo){
        try{
            String consulta = "INSERT INTO llamadasapi (simbolo, precio, tipo) VALUES ('"+simbolo+"','"+precio+"','"+tipo+"')";
            conexion.createStatement().executeUpdate(consulta);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void borrarLlamada(String simbolo){
        try{
            String consulta = "DELETE FROM llamadasapi WHERE simbolo = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, simbolo);
            preparedStatement.executeUpdate();
        }catch (Exception e) {e.printStackTrace();}
    }

    public double getPrecio(String simbolo){
        try {
            String consulta = "SELECT precio FROM llamadasapi WHERE simbolo = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, simbolo);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                return rs.getDouble("precio");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public double getTipo(String simbolo){
        try {
            String consulta = "SELECT tipo FROM llamadasapi WHERE simbolo = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, simbolo);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                return rs.getDouble("tipo");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public LlamadaApi getLlamadaAPI(String simbolo){
        LlamadaApi llamadaApi = null;

        try{
            String consulta = "SELECT * FROM llamadasapi WHERE simbolo = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, simbolo);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                llamadaApi =  new LlamadaApi(
                        rs.getString("simbolo"),
                        rs.getDouble("precio"),
                        rs.getString("tipo")
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return llamadaApi;
    }

    public boolean estaReg(String simbolo){
        try{
            String consulta = "SELECT precio FROM llamadasapi WHERE simbolo = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, simbolo);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next())
                return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
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
            String consulta = "SELECT * FROM llamadasapi";
            ResultSet rs = conexion.createStatement().executeQuery(consulta);

            while (rs.next()){
                llamadaApis.add(new LlamadaApi(
                        rs.getString("simbolo"),
                        rs.getDouble("precio"),
                        rs.getString("tipo")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return llamadaApis;
    }

    public void actualizarSimbolo(String simbolo){
        PosicionesAbiertas posicionesAbiertas = new PosicionesAbiertas();
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            conectar();

            try{
                String tipo = this.getLlamadaAPI(simbolo).getTipo();

                double precio = posicionesAbiertas.getPrecioActual(simbolo, tipo);
                this.setPrecio(simbolo, precio);
            }catch (Exception e){e.printStackTrace();}
            desconectar();
        },0L);
    }

    public synchronized void actualizar(Server server){
        PosicionesAbiertas posicionesAbiertas = new PosicionesAbiertas();
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            conectar();

            List<LlamadaApi> llamadaApis = getTodasLlamaasapi();
            llamadaApis.forEach( (llamadaApi) -> {
                try {
                    double precio = posicionesAbiertas.getPrecioActual(llamadaApi.getSimbolo(), llamadaApi.getTipo());
                    this.setPrecio(llamadaApi.getSimbolo(), precio);

                    server.getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha actualizado el precio de " + llamadaApi.getSimbolo());
                } catch (Exception e) {
                    e.printStackTrace();
                    server.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Error al actualizar el precio de " + llamadaApi.getSimbolo());
                }
            });
            desconectar();
        }  , 0L);
    }
}