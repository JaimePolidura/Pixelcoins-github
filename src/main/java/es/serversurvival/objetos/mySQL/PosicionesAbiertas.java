package es.serversurvival.objetos.mySQL;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.apiHttp.IEXCloud_API;
import es.serversurvival.objetos.mySQL.tablasObjetos.PosicionAbierta;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PosicionesAbiertas extends MySQL {
    private static String titulo = ChatColor.DARK_RED + "SELECCIONA Nº DE ACCIONES";
    private LlamadasApi llamadasApi = new LlamadasApi();

    public PosicionAbierta nuevaPosicion(String jugador, String tipo, String nombre, int cantidad, double precioApertura) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdf.format(dt);

        try {
            String consulta = "INSERT INTO posicionesabiertas (jugador, tipo, nombre, cantidad, precioApertura, fechaApertura) VALUES ('" + jugador + "','"+tipo+"','" + nombre + "','" + cantidad + "','" + precioApertura + "', '" + fecha + "')";
            Statement statement = conexion.createStatement();
            statement.executeUpdate(consulta);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new PosicionAbierta(getMaxId(), jugador, TIPOS.ACCIONES.toString(), nombre, cantidad, precioApertura, fecha);
    }

    private int getMaxId(){
        try{
            String consulta = "SELECT id FROM posicionesabiertas ORDER BY id DESC LIMIT 1";
            ResultSet rs = conexion.createStatement().executeQuery(consulta);

            while (rs.next()){
                return rs.getInt("id");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return -1;
    }

    public PosicionAbierta getPosicion(int id){
        try{
            String consulta = "SELECT * FROM posicionesabiertas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                return new PosicionAbierta(rs.getInt("id"), rs.getString("jugador"), rs.getString("tipo"), rs.getString("nombre"),
                        rs.getInt("cantidad"), rs.getDouble("precioApertura"), rs.getString("fechaApertura"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void borrarPosicion(int id) {
        try {
            String consulta = "DELETE FROM posicionesabiertas WHERE id = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getJugador(int id) {
        String nombre = "";
        try {
            String consulta = "SELECT jugador FROM posicionesabiertas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                nombre = rs.getString("jugador");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nombre;
    }

    public String getTipo(int id) {
        String tipo = "";
        try {
            String consulta = "SELECT tipo FROM posicionesabiertas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                tipo = rs.getString("tipo");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tipo;
    }

    public String getNombre(int id) {
        String nombre = "";
        try {
            String consulta = "SELECT nombre FROM posicionesabiertas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                nombre = rs.getString("nombre");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nombre;
    }

    public int getCantidad(int id) {
        int cantidad = 0;
        try {
            String consulta = "SELECT cantidad FROM posicionesabiertas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                cantidad = rs.getInt("cantidad");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cantidad;
    }

    public double getPreciopApertura(int id) {
        double apertura = 0;
        try {
            String consulta = "SELECT precioApertura FROM posicionesabiertas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                apertura = rs.getDouble("precioApertura");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apertura;
    }

    public String getFechaApertura(int id) {
        String fecha = "";
        try {
            String consulta = "SELECT fechaApertura FROM posicionesabiertas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                fecha = rs.getString("fechaApertura");
                break;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fecha;
    }

    public List<PosicionAbierta> getPosicionesJugador(String jugador){
        List<PosicionAbierta> posicionAbiertas = new ArrayList<>();
        try{
            String consulta = "SELECT * FROM posicionesabiertas WHERE jugador = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                posicionAbiertas.add(new PosicionAbierta(
                        rs.getInt("id"),
                        rs.getString("jugador"),
                        rs.getString("tipo"),
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precioApertura"),
                        rs.getString("fechaApertura")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return posicionAbiertas;
    }

    public boolean existeTicker(String nombre){
        try {
            String consulta = "SELECT * FROM posicionesabiertas WHERE nombre = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, nombre);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existe(int id) {
        boolean toReturn = false;
        try {
            String consulta = "SELECT id FROM posicionesabiertas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                toReturn = true;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public void setCantidad(int id, int cantidad) {
        try {
            String consulta = "UPDATE posicionesabiertas SET cantidad = ? WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, cantidad);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PosicionAbierta> getTodasPosicionesAbiertas(){
        List<PosicionAbierta> toReturn = new ArrayList<>();

        try{
            String consulta = "SELECT * FROM posicionesabiertas";
            ResultSet rs = conexion.createStatement().executeQuery(consulta);

            while (rs.next()){
                toReturn.add(new PosicionAbierta(
                        rs.getInt("id"),
                        rs.getString("jugador"),
                        rs.getString("tipo"),
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precioApertura"),
                        rs.getString("fechaApertura")
                ));
            }
        }catch (Exception e){e.printStackTrace();}
        return toReturn;
    }

    public void pagarDividendos(Plugin plugin) {
        try {
            String consulta = "SELECT nombre, cantidad, jugador, id FROM posicionesabiertas WHERE tipo = 'ACCIONES'";
            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery(consulta);

            String ticker;
            String jugador;
            int id;
            int nAcciones;

            JSONArray jsonArray;
            double diviendo = 0;
            Date fechaHoy = new Date();
            Date fechaPagoDividendo = null;
            Transacciones t = new Transacciones();

            while (resultSet.next()) {
                ticker = resultSet.getString("nombre");
                jugador = resultSet.getString("jugador");
                id = resultSet.getInt("id");
                nAcciones = resultSet.getInt("cantidad");

                try {
                    jsonArray = IEXCloud_API.getDividendo(ticker, "week");
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                    diviendo = (double) jsonObject.get("amount");
                    fechaPagoDividendo = new Date((String) jsonObject.get("paymentDate"));
                } catch (Exception e) {
                    continue;
                }

                if (Funciones.diferenciaDias(fechaHoy, fechaPagoDividendo) != 0) {
                    continue;
                }
                t.pagaDividendo(ticker, jugador, diviendo, nAcciones);
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha pagado " + diviendo + " PC de dividendo a " + jugador + " en la empresa: " + ticker);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        }
        return precio;
    }

    public double getPesoAccionEnCartera(int id){
        PosicionAbierta posicionAMedir = this.getPosicion(id);
        String jugador = posicionAMedir.getJugador();
        double invertidoEnAccion = posicionAMedir.getCantidad() * llamadasApi.getPrecio(posicionAMedir.getNombre());

        List<PosicionAbierta> posicionesJugador = this.getPosicionesJugador(jugador);
        double totalInvertido = posicionesJugador.stream()
                .mapToDouble((pos) -> pos.getCantidad() * llamadasApi.getPrecio(pos.getNombre()))
                .sum();
            
        return Funciones.rentabilidad(totalInvertido, invertidoEnAccion);
    }

    public static enum TIPOS{
        ACCIONES,
        CRIPTOMONEDAS,
        MATERIAS_PRIMAS
    }
}
