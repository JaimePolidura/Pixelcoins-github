package es.serversurvival.objetos.mySQL;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.tablasObjetos.Deuda;
import es.serversurvival.objetos.mySQL.tablasObjetos.Jugador;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class Jugadores extends MySQL {
    private DecimalFormat formatea = new DecimalFormat("###,###.##");

    public Jugador nuevoJugador(String nombre, double pixelcoin, int espacios, int nventas, double ingresos, double gastos, double beneficios, int ninpagos, int npagos) {
        String consulta = "INSERT INTO jugadores (nombre, pixelcoin, espacios, nventas, ingresos, gastos, beneficios, ninpagos, npagos) VALUES ('" + nombre + "','" + pixelcoin + "','" + espacios + "','" + nventas + "','" + ingresos + "','" + gastos + "','" + beneficios + "','" + ninpagos + "','" + npagos + "')";
        executeUpdate(consulta);

        return new Jugador(nombre, pixelcoin, espacios, nventas, ingresos, gastos, beneficios, ninpagos, npagos);
    }

    public double getDinero(String nombre) {
        try {
            ResultSet rs = executeQuery(String.format("SELECT pixelcoin FROM jugadores WHERE nombre = '%s'", nombre));

            while (rs.next()) {
                return rs.getDouble("pixelcoin");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getEspacios(String nombre) {
        try {
            ResultSet rs = executeQuery(String.format("SELECT espacios FROM jugadores WHERE nombre = '%s'", nombre));

            while (rs.next()) {
                return rs.getInt("espacios");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getNventas(String nombre) {
        try {
            ResultSet rs = executeQuery(String.format("SELECT nventas FROM jugadores WHERE nombre =  '%s'", nombre));

            while (rs.next()) {
                return rs.getInt("nventas");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }

    public double getIngresos(String nombre) {
        try {
            ResultSet rs = executeQuery(String.format("SELECT ingresos FROM jugadores WHERE nombre = '%s' ", nombre));

            while (rs.next()) {
                return rs.getDouble("ingresos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public double getGastos(String nombre) {
        try {
            ResultSet rs = executeQuery(String.format("SELECT gastos FROM jugadores WHERE nombre = '%s'", nombre));

            while (rs.next()) {
                return rs.getDouble("gastos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getNinpago(String nombre) {
        try {
            ResultSet rs = executeQuery(String.format("SELECT ninpagos FROM jugadores WHERE nombre = '%s'", nombre));

            while (rs.next()) {
                return rs.getInt("ninpago");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getNpagos(String nombre) {
        try {
            ResultSet rs = executeQuery(String.format("SELECT npagos FROM jugadores WHERE nombre = '%s'", nombre));

            while (rs.next()) {
                return rs.getInt("npagos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public double getBeneficios(String nombre) {
        try {
            ResultSet rs = executeQuery(String.format("SELECT beneficios FROM jugadores WHERE nombre = '%s'", nombre));

            while (rs.next()) {
                return rs.getInt("beneficios");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Jugador getJugador(String jugador){
        try{
            ResultSet rs = executeQuery(String.format("SELECT * FROM jugadores WHERE nombre = '%s'", jugador));

            while (rs.next()){
                return buildJugadorByResultSet(rs);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean estaRegistrado (String nombreJugador) {
        return getJugador(nombreJugador) != null;
    }

    public void setPixelcoin(String nombre, double pixelcoin) {
        try {
            String consulta = "UPDATE jugadores SET pixelcoin = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);

            pst.setDouble(1, pixelcoin);
            pst.setString(2, nombre);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setNinpagos(String nombre, int ninpagos) {
        try {
            String consulta2 = "UPDATE jugadores SET ninpagos = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setInt(1, ninpagos);
            pst.setString(2, nombre);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setNpagos(String nombre, int npagos) {
        try {
            String consulta2 = "UPDATE jugadores SET npagos = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);
            pst.setInt(1, npagos);
            pst.setString(2, nombre);

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setEspacios(String nombre, int espacios) {
        try {
            String consulta2 = "UPDATE jugadores SET espacios = ? WHERE nombre = ?";

            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);
            pst.setInt(1, espacios);
            pst.setString(2, nombre);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setEstadisticas(String nombre, double dinero, int nventas, double ingresos, double gastos) {
        try {
            String consulta2 = "UPDATE jugadores SET pixelcoin = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setDouble(1, dinero);
            pst.setInt(2, nventas);
            pst.setDouble(3, ingresos);
            pst.setDouble(4, gastos);
            pst.setDouble(5, ingresos - gastos);
            pst.setString(6, nombre);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Jugador> getAllJugadores(){
        List<Jugador> jugadors = new ArrayList<>();
        try{
            String consulta = "SELECT * FROM jugadores";
            ResultSet rs = conexion.createStatement().executeQuery(consulta);

            while (rs.next()){
                jugadors.add(buildJugadorByResultSet(rs));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jugadors;
    }

    public int getPosicionTopRicos (String player){
        List<Jugador> jugadores = this.getAllJugadores();

        jugadores.sort((o1, o2) -> {
            Double o1Double = o1.getPixelcoin();
            Double o2Double = o2.getPixelcoin();

            return o2Double.compareTo(o1Double);
        });

        int pos = 1;
        for(Jugador jugador : jugadores){
            if(jugador.getNombre().equalsIgnoreCase(player)) return pos;
            pos++;
        }

        return -1;
    }

    public int getPosicionTopVendedores (String player){
        List<Jugador> jugadores = this.getAllJugadores();

        jugadores.sort((o1, o2) -> {
            Integer o1Int = o1.getNventas();
            Integer o2Int = o2.getNventas();

            return o2Int.compareTo(o1Int);
        });

        int pos = 1;
        for(Jugador jugador : jugadores){
            if(jugador.getNombre().equalsIgnoreCase(player)) return pos;
            pos++;
        }
        return -1;
    }

    public List<Jugador> getTopRicos (){
        List<Jugador> toReturn = new ArrayList<>();

        try{
            ResultSet rs = executeQuery("SELECT * FROM jugadores ORDER BY pixelcoin");

            while (rs.next()){
                toReturn.add(buildJugadorByResultSet(rs));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return toReturn;
    }

    public List<Jugador> getTopPobres (){
        List<Jugador> toReturn = new ArrayList<>();

        try{
            ResultSet rs = executeQuery("SELECT * FROM jugadores ORDER BY pixelcoin ASC");

            while (rs.next()){
                toReturn.add(buildJugadorByResultSet(rs));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return toReturn;
    }

    public List<Jugador> getTopVendedores (){
        List<Jugador> toReturn = new ArrayList<>();

        try{
            ResultSet rs = executeQuery("SELECT * FROM jugadores ORDER BY nventas DESC");

            while (rs.next()){
                toReturn.add(buildJugadorByResultSet(rs));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return toReturn;
    }

    public List<Jugador> getTopFiables (){
        List<Jugador> toReturn = new ArrayList<>();
        try{
            ResultSet rs = executeQuery("SELECT * FROM jugadores ORDER BY npagos DESC");

            while (rs.next()){
                toReturn.add(buildJugadorByResultSet(rs));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return toReturn;
    }

    public List<Jugador> getTopMenosFiables (){
        List<Jugador> toReturn = new ArrayList<>();

        try{
            ResultSet rs = executeQuery("SELECT * FROM jugadores ORDER BY ninpagos DESC");
            while (rs.next()){
                toReturn.add(buildJugadorByResultSet(rs));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return toReturn;
    }

    public void mostarEstadisticas(Player p) {
        Deudas d = new Deudas();
        p.sendMessage("     ");

        Jugador jugador = this.getJugador(p.getName());
        if (jugador != null) {
            double nventas = jugador.getNventas();
            double ingresos = jugador.getIngresos();
            double gastos = jugador.getGastos();
            double beneficios = ingresos - gastos;
            double margen = Funciones.rentabilidad(ingresos, beneficios);
            double precioMedio = Math.round(ingresos / nventas);

            int ninpagos = jugador.getNinpagos();
            int npagos = jugador.getNpagos();
            int todaDeudaAcredor = 0;
            int todaDeudaDeudor = 0;

            d.conectar();
            todaDeudaAcredor = d.getDeudasAcredor(p.getName()).stream()
                .mapToInt(Deuda::getPixelcoins)
                .sum();

            todaDeudaDeudor = d.getDeudasAcredor(p.getName()).stream()
                .mapToInt(Deuda::getPixelcoins)
                .sum();
            d.desconectar();

            p.sendMessage(ChatColor.GOLD + "Tus estadisticas" + ChatColor.AQUA + " (/ayuda estadisticas)");
            p.sendMessage(ChatColor.GOLD + "Nunmero de ventas: " + ChatColor.GREEN + formatea.format(nventas));
            p.sendMessage(ChatColor.GOLD + "Precio/venta: " + ChatColor.GREEN + precioMedio + " PC/Venta" + ChatColor.GOLD);
            p.sendMessage(ChatColor.GOLD + "Ingresos:  " + ChatColor.GREEN + formatea.format(ingresos));
            p.sendMessage(ChatColor.GOLD + "Gastos: " + ChatColor.GREEN + formatea.format(gastos));

            if (beneficios >= 0) {
                p.sendMessage(ChatColor.GOLD + "Beneficios: " + ChatColor.GREEN + formatea.format(beneficios) + ChatColor.GOLD + " Rentabilidad: " + ChatColor.GREEN + margen + "%");
            } else {
                p.sendMessage(ChatColor.GOLD + "Beneficios: " + ChatColor.RED + formatea.format(beneficios) + ChatColor.GOLD + " Rentabilidad: " + ChatColor.RED + formatea.format(margen) + "%");
            }

            p.sendMessage(ChatColor.GOLD + "Pixelcoins que debes (con intereses aplicados) (/deudas): " + ChatColor.GREEN + formatea.format(todaDeudaDeudor) + " PC");
            p.sendMessage(ChatColor.GOLD + "Pixelcoins que te deben (con intereses aplicados) (/deudas): " + ChatColor.GREEN + formatea.format(todaDeudaAcredor) + " PC");
            p.sendMessage(ChatColor.GOLD + "Numero de veces que has pagado la deuda: " + ChatColor.GREEN + npagos);
            p.sendMessage(ChatColor.GOLD + "Numero de veces que no has pagado la deuda: " + ChatColor.GREEN + ninpagos);
        } else {
            p.sendMessage(ChatColor.RED + "No tienes pixelcoins (/ayuda dinero y /ayuda tienda)");
            p.sendMessage(ChatColor.GOLD + "N? ventas" + ChatColor.RED + 0);
            p.sendMessage(ChatColor.GOLD + "Precio/venta" + ChatColor.RED + "0" + " PC/Venta" + ChatColor.GOLD + " (ingresos/n? ventas)");
            p.sendMessage(ChatColor.GOLD + "Ingresos:  " + ChatColor.RED + 0);
            p.sendMessage(ChatColor.GOLD + "Gastos: " + ChatColor.RED + 0);
            p.sendMessage(ChatColor.GOLD + "Beneficios: " + ChatColor.RED + 0 + ChatColor.GOLD + " Rentabilidad: " + ChatColor.RED + 0 + "%");
            p.sendMessage(ChatColor.GOLD + "Pixelcoins que debes (con intereses aplicados) (/deudas): " + ChatColor.RED + 0 + " PC");
            p.sendMessage(ChatColor.GOLD + "Pixelcoins que te deben (con intereses aplicados) (/deudas): " + ChatColor.RED + 0 + " PC");
            p.sendMessage(ChatColor.GOLD + "Numero de veces que has pagado la deuda: " + ChatColor.RED + 0);
            p.sendMessage(ChatColor.GOLD + "Numero de veces que has pagado la deuda: " + ChatColor.RED + 0);
        }
    }

    private Jugador buildJugadorByResultSet (ResultSet rs) throws SQLException {
        return new Jugador(
                rs.getString("nombre"),
                rs.getDouble("pixelcoin"),
                rs.getInt("espacios"),
                rs.getInt("nventas"),
                rs.getDouble("ingresos"),
                rs.getDouble("gastos"),
                rs.getDouble("beneficios"),
                rs.getInt("ninpagos"),
                rs.getInt("npagos")
        );
    }
}