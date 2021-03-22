package es.serversurvival.objetos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class Jugador extends MySQL {

    public DecimalFormat formatea = new DecimalFormat("###,###.##");

    //MostrarDinero
    public void mostarPixelcoin(Player p) {
        int dinero = this.getDinero(p.getName());

        if (dinero < 0) {
            p.sendMessage(ChatColor.GOLD + "Tienes: " + ChatColor.RED + formatea.format(dinero) + " PC");
        } else {
            p.sendMessage(ChatColor.GOLD + "Tienes: " + ChatColor.GREEN + formatea.format(dinero) + " PC");
        }
    }

    //MostrarEstadisticas
    public void mostarEstadisticas(Player p) {
        boolean reg = this.estaRegistrado(p.getName());
        Deudas d = new Deudas();
        p.sendMessage("     ");
        if (reg) {
            double nventas = this.getNventas(p.getName());
            double ingresos = this.getIngresos(p.getName());
            double gastos = this.getGastos(p.getName());
            double beneficios = ingresos - gastos;
            double margen = Math.round((beneficios / ingresos) * 100);
            double precioMedio = Math.round(ingresos / nventas);

            int ninpagos = this.getNinpago(p.getName());
            int npagos = this.getNpagos(p.getName());
            int deuda = 0;
            int deudaDevolver = 0;
            try {
                d.conectar("root", "", "pixelcoins");
                deuda = d.getTodaDeudaAcredor(p.getName());
                deudaDevolver = d.getTodaDeudaDeudor(p.getName());
                d.desconectar();
            } catch (Exception e) {

            }

            p.sendMessage(ChatColor.GOLD + "Tus estadisticas" + ChatColor.AQUA + " (/ayuda estadisticas)");
            p.sendMessage(ChatColor.GOLD + "N? ventas: " + ChatColor.GREEN + formatea.format(nventas));
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
        int cantidadActual = 0;
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
                cantidadActual = rs.getInt("pixelcoin");
                i++;

                p.sendMessage(ChatColor.GOLD + "" + i + "?: " + nombreActual + ":  " + ChatColor.GREEN + formatea.format(cantidadActual) + " PC");
            }
        } catch (SQLException e) {
            p.sendMessage(ChatColor.RED + "Error en Jugador.mostarTopRicos, hablar con el admin");
        }
    }

    //MostrarTopPobres
    public void mostarTopPobres(Player p) {
        p.sendMessage("  ");
        int cantidadActual = 0;
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
                cantidadActual = rs.getInt("pixelcoin");
                i++;

                if (cantidadActual < 0) {
                    p.sendMessage(ChatColor.GOLD + "" + i + "?: " + nombreActual + ":  " + ChatColor.RED + formatea.format(cantidadActual) + " PC");
                } else {
                    p.sendMessage(ChatColor.GOLD + "" + i + "?: " + nombreActual + ":  " + ChatColor.GREEN + formatea.format(cantidadActual) + " PC");
                }

            }
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
        } catch (SQLException e) {
            p.sendMessage(ChatColor.RED + "Error en Jugador.mostrarTopMenosFiables, hablar con el admin");
        }
    }

    //GetDinero
    public int getDinero(String nombre) {
        int dinero = 0;
        try {
            String consulta = "SELECT * FROM jugadores";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombre)) {
                    dinero = rs.getInt("pixelcoin");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return dinero;
    }

    //GetEspacios
    public int getEspacios(String nombre) {
        int espacios = 0;
        try {
            String consulta = "SELECT * FROM jugadores";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombre)) {
                    espacios = rs.getInt("espacios");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return espacios;
    }

    //GetNventas
    public int getNventas(String nombre) {
        int nventas = 0;
        try {
            String consulta = "SELECT * FROM jugadores";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombre)) {
                    nventas = rs.getInt("nventas");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return nventas;
    }

    //GetIngresos
    public int getIngresos(String nombre) {
        int ingresos = 0;
        try {
            String consulta = "SELECT * FROM jugadores";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombre)) {
                    ingresos = rs.getInt("ingresos");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return ingresos;
    }

    //GetGastos
    public int getGastos(String nombre) {
        int gastos = 0;
        try {
            String consulta = "SELECT * FROM jugadores";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombre)) {
                    gastos = rs.getInt("gastos");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return gastos;
    }

    //GetInpago
    public int getNinpago(String nombre) {
        int ninpagos = 0;
        try {
            String consulta = "SELECT * FROM jugadores";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombre)) {
                    ninpagos = rs.getInt("ninpagos");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return ninpagos;
    }

    //Getnpagos
    public int getNpagos(String nombre) {
        int npagos = 0;
        try {
            String consulta = "SELECT * FROM jugadores";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombre)) {
                    npagos = rs.getInt("npagos");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return npagos;
    }

    //GetBeneficios
    public int getBeneficios(String nombre) {
        int beneficios = 0;
        try {
            String consulta = "SELECT * FROM jugadores";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombre)) {
                    beneficios = rs.getInt("beneficios");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return beneficios;
    }

    //A?adir Jugador
    public void nuevoJugador(String nombre, int pixelcoin, int espacios, int nventas, int ingresos, int gastos, int beneficios, int ninpagos, int npagos) {
        try {
            String consulta = "INSERT INTO jugadores (nombre, pixelcoin, espacios, nventas, ingresos, gastos, beneficios, ninpagos, npagos) VALUES ('" + nombre + "','" + pixelcoin + "','" + espacios + "','" + nventas + "','" + ingresos + "','" + gastos + "','" + beneficios + "','" + ninpagos + "','" + npagos + "')";
            Statement st2 = (Statement) conexion.createStatement();
            st2.executeUpdate(consulta);
        } catch (SQLException e) {

        }
    }

    public void setEstadisticas(String nombre, int dinero, int nventas, int ingresos, int gastos) {
        try {
            String consulta2 = "UPDATE jugadores SET pixelcoin = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setInt(1, dinero);
            pst.setInt(2, nventas);
            pst.setInt(3, ingresos);
            pst.setInt(4, gastos);
            pst.setInt(5, ingresos - gastos);
            pst.setString(6, nombre);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    //esta Registrado
    public boolean estaRegistrado(String nombre) {
        boolean registrado = false;

        try {
            String consulta = "SELECT * FROM jugadores";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombre)) {
                    registrado = true;
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return registrado;
    }

    //Set pixelcoin
    public void setPixelcoin(String nombre, int pixelcoin) {
        try {
            String consulta = "UPDATE jugadores SET pixelcoin = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);

            pst.setInt(1, pixelcoin);
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
}