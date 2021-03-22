package es.serversurvival.objetos.mySQL;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Deudas;
import es.serversurvival.objetos.mySQL.MySQL;
import es.serversurvival.objetos.mySQL.tablasObjetos.Jugador;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class Jugadores extends MySQL {
    public DecimalFormat formatea = new DecimalFormat("###,###.##");

    //MostrarDinero
    public void mostarPixelcoin(Player p) {
        double dinero = this.getDinero(p.getName());

        if (dinero < 0) {
            p.sendMessage(ChatColor.GOLD + "Tienes: " + ChatColor.RED + formatea.format(dinero) + " PC");
        } else {
            p.sendMessage(ChatColor.GOLD + "Tienes: " + ChatColor.GREEN + formatea.format(dinero) + " PC");
        }
    }

    public Jugador getJugador(String jugador){
        Jugador toReturn = null;

        try{
            String consulta = "SELECT * FROM jugadores WHERE nombre = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                toReturn = new Jugador(
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
        }catch (Exception e){
            e.printStackTrace();
        }
        return toReturn;
    }

    //MostrarEstadisticas
    public void mostarEstadisticas(Player p) {
        Funciones f = new Funciones();
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
            int deuda = 0;
            int deudaDevolver = 0;
            try {
                d.conectar();
                deuda = d.getTodaDeudaAcredor(p.getName());
                deudaDevolver = d.getTodaDeudaDeudor(p.getName());
                d.desconectar();
            } catch (Exception e) {

            }

            p.sendMessage(ChatColor.GOLD + "Tus estadisticas" + ChatColor.AQUA + " (/ayuda estadisticas)");
            p.sendMessage(ChatColor.GOLD + "Nunmero de ventas: " + ChatColor.GREEN + formatea.format(nventas));
            p.sendMessage(ChatColor.GOLD + "Precio/venta: " + ChatColor.GREEN + precioMedio + " PC/Venta" + ChatColor.GOLD);
            p.sendMessage(ChatColor.GOLD + "Ingresos:  " + ChatColor.GREEN + formatea.format(ingresos));
            p.sendMessage(ChatColor.GOLD + "Gastos: " + ChatColor.GREEN + formatea.format(gastos));

            //Comprobar si el beneficio es negativo, si lo es lo ponemos en rojo sino el verde
            if (beneficios >= 0) {
                p.sendMessage(ChatColor.GOLD + "Beneficios: " + ChatColor.GREEN + formatea.format(beneficios) + ChatColor.GOLD + " Rentabilidad: " + ChatColor.GREEN + margen + "%");
            } else {
                p.sendMessage(ChatColor.GOLD + "Beneficios: " + ChatColor.RED + formatea.format(beneficios) + ChatColor.GOLD + " Rentabilidad: " + ChatColor.RED + formatea.format(margen) + "%");
            }

            p.sendMessage(ChatColor.GOLD + "Pixelcoins que debes (con intereses aplicados) (/deudas): " + ChatColor.GREEN + formatea.format(deudaDevolver) + " PC");
            p.sendMessage(ChatColor.GOLD + "Pixelcoins que te deben (con intereses aplicados) (/deudas): " + ChatColor.GREEN + formatea.format(deuda) + " PC");
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

    //MostarTopRicos
    public void mostarTopRicos(Player p) {
        p.sendMessage("  ");
        double cantidadActual = 0;
        int i = 0;
        String nombreActual = "";

        //recorrer consulta sacando los top 3 jugadores con mas dinero de la tabla dinero e ir enviandoselo al jugador mediante mesanges
        try {
            String consulta = "SELECT * FROM jugadores ORDER BY pixelcoin desc LIMIT 3 ";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            p.sendMessage(ChatColor.GOLD + "TOP RICOS:");
            while (rs.next()) {
                nombreActual = rs.getString("nombre");
                cantidadActual = rs.getDouble("pixelcoin");
                i++;

                p.sendMessage(ChatColor.GOLD + "" + i + "?: " + nombreActual + ":  " + ChatColor.GREEN + formatea.format(cantidadActual) + " PC");
            }
            rs.close();
        } catch (SQLException e) {
            p.sendMessage(ChatColor.RED + "Error en Jugador.mostarTopRicos, hablar con el admin");
        }
    }

    //MostrarTopPobres
    public void mostarTopPobres(Player p) {
        p.sendMessage("  ");
        double cantidadActual = 0;
        int i = 0;
        String nombreActual = "";

        //recorrer consulta sacando los top 3 jugadores  con menos dinero de la tabla dinero e ir enviandoselo al jugador mediante mesanges
        try {
            String consulta = "SELECT * FROM jugadores ORDER BY pixelcoin asc LIMIT 3 ";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            p.sendMessage(ChatColor.GOLD + "TOP POBRES:");
            while (rs.next()) {
                nombreActual = rs.getString("nombre");
                cantidadActual = rs.getDouble("pixelcoin");
                i++;

                if (cantidadActual < 0) {
                    p.sendMessage(ChatColor.GOLD + "" + i + "?: " + nombreActual + ":  " + ChatColor.RED + formatea.format(cantidadActual) + " PC");
                } else {
                    p.sendMessage(ChatColor.GOLD + "" + i + "?: " + nombreActual + ":  " + ChatColor.GREEN + formatea.format(cantidadActual) + " PC");
                }

            }
            rs.close();
        } catch (SQLException e) {
            p.sendMessage(ChatColor.RED + "Error en Jugador.mostarTopPobres, hablar con el admin");
        }
    }

    //MostarTopVendedores
    public void mostarTopVendedores(Player p) {
        p.sendMessage("  ");
        int nventas = 0;
        int i = 0;
        String nombreActual = "";

        //recorrer consulta sacando los top 3 jugadores  con menos dinero de la tabla dinero e ir enviandoselo al jugador mediante mesanges
        try {
            String consulta = "SELECT * FROM jugadores ORDER BY nventas desc LIMIT 3 ";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            p.sendMessage(ChatColor.GOLD + "TOP VENDEDORES (ventas en tienda, y veces que ha sido pagado):");
            while (rs.next()) {
                nombreActual = rs.getString("nombre");
                nventas = rs.getInt("nventas");
                i++;

                p.sendMessage(ChatColor.GOLD + "" + i + "?: " + nombreActual + ":  " + ChatColor.GREEN + formatea.format(nventas) + " ventas");
            }
            rs.close();
        } catch (SQLException e) {
            p.sendMessage(ChatColor.RED + "Error en Jugador.mostarTopPobres, hablar con el admin");
        }
    }

    //MostrarTopFiables
    public void mostrarTopFiables(Player p) {
        p.sendMessage("  ");
        int npagos = 0;
        int i = 0;
        String nombreActual = "";

        //recorrer consulta sacando los top 3 jugadores  con menos dinero de la tabla dinero e ir enviandoselo al jugador mediante mesanges
        try {
            String consulta = "SELECT * FROM jugadores ORDER BY npagos desc LIMIT 3 ";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            p.sendMessage(ChatColor.GOLD + "TOP FIABLES (Mas han pagado la deuda)");
            while (rs.next()) {
                nombreActual = rs.getString("nombre");
                npagos = rs.getInt("npagos");
                i++;

                p.sendMessage(ChatColor.GOLD + "" + i + "?: " + nombreActual + ": " + ChatColor.GREEN + formatea.format(npagos) + " veces");
            }
            rs.close();
        } catch (SQLException e) {
            p.sendMessage(ChatColor.RED + "Error en Jugador.mostrarTopFiables, hablar con el admin");
        }
    }

    //mostrarMenosFiables
    public void mostrarTopMenosFiables(Player p) {
        p.sendMessage("  ");
        int ninpagos = 0;
        int i = 0;
        String nombreActual = "";

        //recorrer consulta sacando los top 3 jugadores  con menos dinero de la tabla dinero e ir enviandoselo al jugador mediante mesanges
        try {
            String consulta = "SELECT * FROM jugadores ORDER BY ninpagos desc LIMIT 3 ";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            p.sendMessage(ChatColor.GOLD + "TOP MENOS FIABLES (Menos han podigo pagar la deuda):");
            while (rs.next()) {
                nombreActual = rs.getString("nombre");
                ninpagos = rs.getInt("ninpagos");
                i++;

                p.sendMessage(ChatColor.GOLD + "" + i + "?: " + nombreActual + ": " + ChatColor.GREEN + formatea.format(ninpagos) + " veces");
            }
            rs.close();
        } catch (SQLException e) {
            p.sendMessage(ChatColor.RED + "Error en Jugador.mostrarTopMenosFiables, hablar con el admin");
        }
    }

    //GetDinero
    public double getDinero(String nombre) {
        double dinero = 0;
        try {
            String consulta = "SELECT pixelcoin FROM jugadores WHERE nombre = ? ";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                dinero = rs.getDouble("pixelcoin");
                break;
            }
            rs.close();
        } catch (Exception e) {

        }

        return dinero;
    }

    //GetEspacios
    public int getEspacios(String nombre) {
        int espacios = 0;
        try {
            String consulta = "SELECT espacios FROM jugadores WHERE nombre = ? ";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                espacios = rs.getInt("espacios");
                break;
            }
            rs.close();
        } catch (Exception e) {

        }
        return espacios;
    }

    //GetNventas
    public int getNventas(String nombre) {
        int nventas = 0;
        try {
            String consulta = "SELECT nventas FROM jugadores WHERE nombre = ? ";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                nventas = rs.getInt("nventas");
                break;
            }
            rs.close();
        } catch (Exception e) {

        }
        return nventas;
    }

    //GetIngresos
    public double getIngresos(String nombre) {
        double ingresos = 0;
        try {
            String consulta = "SELECT ingresos FROM jugadores WHERE nombre = ? ";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ingresos = rs.getDouble("ingresos");
                break;
            }
            rs.close();
        } catch (Exception e) {

        }
        return ingresos;
    }

    public double getGastos(String nombre) {
        double gastos = 0;
        try {
            String consulta = "SELECT gastos FROM jugadores WHERE nombre = ? ";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                gastos = rs.getDouble("gastos");
                break;
            }
            rs.close();
        } catch (Exception e) {

        }
        return gastos;
    }

    //GetInpago
    public int getNinpago(String nombre) {
        int ninpagos = 0;
        try {
            String consulta = "SELECT ninpagos FROM jugadores WHERE nombre = ? ";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ninpagos = rs.getInt("ninpagos");
                break;
            }
            rs.close();
        } catch (Exception e) {

        }
        return ninpagos;
    }

    //Getnpagos
    public int getNpagos(String nombre) {
        int npagos = 0;
        try {
            String consulta = "SELECT npagos FROM jugadores WHERE nombre = ? ";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                npagos = rs.getInt("npagos");
                break;
            }
            rs.close();
        } catch (Exception e) {

        }
        return npagos;
    }

    public double getBeneficios(String nombre) {
        double beneficios = 0;
        try {
            String consulta = "SELECT beneficios FROM jugadores WHERE nombre = ? ";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                beneficios = rs.getDouble("beneficios");
                break;
            }
            rs.close();
        } catch (Exception e) {

        }
        return beneficios;
    }

    //A?adir Jugador
    public void nuevoJugador(String nombre, double pixelcoin, int espacios, int nventas, double ingresos, double gastos, double beneficios, int ninpagos, int npagos) {
        try {
            String consulta = "INSERT INTO jugadores (nombre, pixelcoin, espacios, nventas, ingresos, gastos, beneficios, ninpagos, npagos) VALUES ('" + nombre + "','" + pixelcoin + "','" + espacios + "','" + nventas + "','" + ingresos + "','" + gastos + "','" + beneficios + "','" + ninpagos + "','" + npagos + "')";
            Statement st2 = (Statement) conexion.createStatement();
            st2.executeUpdate(consulta);
        } catch (SQLException e) {

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

        }
    }

    //esta Registrado
    public boolean estaRegistrado(String nombre) {
        boolean registrado = false;
        try {
            String consulta = "SELECT nombre FROM jugadores WHERE nombre = ? ";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                registrado = true;
            }
            rs.close();
        } catch (Exception e) {

        }
        return registrado;
    }

    //Set pixelcoin
    public void setPixelcoin(String nombre, double pixelcoin) {
        try {
            String consulta = "UPDATE jugadores SET pixelcoin = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);

            pst.setDouble(1, pixelcoin);
            pst.setString(2, nombre);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    //SetNinpagos
    public void setNinpagos(String nombre, int ninpagos) {
        try {
            String consulta2 = "UPDATE jugadores SET ninpagos = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setInt(1, ninpagos);
            pst.setString(2, nombre);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    //SetNpagos
    public void setNpagos(String nombre, int npagos) {
        try {
            String consulta2 = "UPDATE jugadores SET npagos = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setInt(1, npagos);
            pst.setString(2, nombre);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    //Set espacios
    public void setEspacios(String nombre, int espacios) {
        try {
            String consulta2 = "UPDATE jugadores SET espacios = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setInt(1, espacios);
            pst.setString(2, nombre);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public List<Jugador> getAllJugadores(){
        List<Jugador> jugadors = new ArrayList<>();
        try{
            String consulta = "SELECT * FROM jugadores";
            ResultSet rs = conexion.createStatement().executeQuery(consulta);

            while (rs.next()){
                jugadors.add(new Jugador(
                        rs.getString("nombre"),
                        rs.getDouble("pixelcoin"),
                        rs.getInt("espacios"),
                        rs.getInt("nventas"),
                        rs.getDouble("ingresos"),
                        rs.getDouble("gastos"),
                        rs.getDouble("beneficios"),
                        rs.getInt("ninpagos"),
                        rs.getInt("npagos")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jugadors;
    }

    public Map<String, Double> getAllJugadoresDinero(){
        Map<String, Double> jugadors = new HashMap<>();
        try{
            String consulta = "SELECT nombre, pixelcoin FROM jugadores";
            ResultSet rs = conexion.createStatement().executeQuery(consulta);

            while (rs.next()){
                jugadors.put(
                        rs.getString("nombre"),
                        rs.getDouble("pixelcoin")
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jugadors;
    }

    public HashMap<String, Double> getTop5PlayersMenosPixelcoins() {
        HashMap<String, Double> jugadores = new HashMap<>();
        try {
            String consulta = "SELECT nombre, pixelcoin FROM jugadores ORDER BY pixelcoin ASC LIMIT 5";
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                jugadores.put(rs.getString("nombre"), rs.getDouble("pixelcoin"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Funciones.sortByValueCre(jugadores);
    }

    public HashMap<String, Integer> getTop5PlayersNVentas() {
        HashMap<String, Integer> jugadores = new HashMap<>();
        try {
            String consulta = "SELECT nombre,nventas FROM jugadores ORDER BY nventas DESC LIMIT 5";
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                jugadores.put(rs.getString("nombre"), rs.getInt("nventas"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Funciones.sortByValueDecreInt(jugadores);
    }

    public HashMap<String, Integer> getTop5PlayerFiables() {
        HashMap<String, Integer> jugadores = new HashMap<>();
        try {
            String consulta = "SELECT nombre,npagos FROM jugadores ORDER BY npagos DESC LIMIT 5";
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                jugadores.put(rs.getString("nombre"), rs.getInt("npagos"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Funciones.sortByValueDecreInt(jugadores);
    }

    public HashMap<String, Integer> getTop5PlayersMenosFiables() {
        HashMap<String, Integer> jugadores = new HashMap<>();
        try {
            String consulta = "SELECT nombre,ninpagos FROM jugadores ORDER BY ninpagos DESC LIMIT 5";
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                jugadores.put(rs.getString("nombre"), rs.getInt("ninpagos"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Funciones.sortByValueCreInt(jugadores);
    }
}