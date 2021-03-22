package es.serversurvival.config;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class Comandos {
    public static Connection conexion;
    public DecimalFormat formatea = new DecimalFormat("###,###.##");
    private Plugin plugin = Pixelcoin.getPlugin(Pixelcoin.class);

    //Metodo para conectarme a la base de datos
    public void conectar(String user, String pass, String dbName) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, user, pass);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, e);
        } catch (SQLException e) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //Metodo para desconectar
    public void desconectar() {
        try {
            conexion.close();
        } catch (SQLException e) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //--------------Ver pixelcoins ---------------
    public void mostrarDinero(Player p) {
        String nombreJugador = p.getName();
        int actuales = 0;
        boolean done = false;
        try {
            String consulta1 = "SELECT * FROM jugadores";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta1);
            String nombreActual = "";
            //recorrer la tabal dinero 
            while (rs.next()) {
                nombreActual = rs.getString("nombre");
                if (nombreActual.equalsIgnoreCase(nombreJugador)) {
                    actuales = rs.getInt("pixelcoin");
                    done = true;
                }
            }

        } catch (SQLException e) {
            p.sendMessage(ChatColor.RED + "Error en Comandos.mostrarDinero, hablar con el admin");
        }
        //se ha encontrado al jugador
        if (done == true) {
            p.sendMessage(ChatColor.GOLD + "Tienes: " + ChatColor.GREEN + formatea.format(actuales) + " PC");
        } else {
            p.sendMessage(ChatColor.GOLD + "Tienes: " + ChatColor.GREEN + "0 PC");
        }
    }

    //-----------Pagar pixelcoins a otro jugador--------------
    public void pagarPixelCoins(Player p, String tname, String scantidad) {
        int actualPagador = 0;
        int actualPagado = 0;
        int cantidad = 0;
        int caso = 0;
        int espaciosActualesPagador = 0;
        int espaciosActualesPagado = 0;
        int nventasPagador = 0;
        int ingresosPagador = 0;
        int gastosPagador = 0;
        int nventasPagado = 0;
        int ingresosPagado = 0;
        int gastosPagado = 0;

        boolean done = false;
        boolean pagador = false;
        boolean pagado = false;
        String NombrePagador = p.getName();
        String NombrePagado = tname;
        Player tp = null;

        //Comprobar si has metido como argumento numero
        try {
            cantidad = Integer.parseInt(scantidad);
            done = true;
        } catch (NumberFormatException e) {
            p.sendMessage(ChatColor.DARK_RED + "Introduce un numero, no texto de tal manera: /pagar <nombre del jugador> <cantidad a pagar>");
        }

        //Se ha introducido texto
        if (done == true) {
            try {
                String consulta1 = "SELECT * FROM jugadores";
                Statement st = conexion.createStatement();
                ResultSet rs;
                rs = st.executeQuery(consulta1);
                String nombreActual = "";

                //recorrer la tabla dinero
                while (rs.next()) {
                    nombreActual = rs.getString("nombre");
                    //se ha encontrado al pagador
                    if (nombreActual.equalsIgnoreCase(NombrePagador) == true) {
                        actualPagador = rs.getInt("pixelcoin");
                        espaciosActualesPagador = rs.getInt("espacios");
                        nventasPagador = rs.getInt("nventas");
                        ingresosPagador = rs.getInt("ingresos");
                        gastosPagador = rs.getInt("gastos");

                        pagador = true;
                        //se ha encontrado al pagado
                    } else if (nombreActual.equalsIgnoreCase(NombrePagado) == true) {
                        espaciosActualesPagado = rs.getInt("espacios");
                        actualPagado = rs.getInt("pixelcoin");
                        nventasPagado = rs.getInt("nventas");
                        ingresosPagado = rs.getInt("ingresos");
                        gastosPagado = rs.getInt("gastos");

                        pagado = true;
                    }

                    if (pagador == true && pagado == true) {
                        break;
                    }
                }

                //Se he enontrado pagado, pagador, el dinero que tenga el pagador no sea 0, que la cantidad a pagar sea menor o igual al dinero del pagador y que nosea el mismo
                if (pagador == true && pagado == true && cantidad > 0 && cantidad <= actualPagador && (NombrePagador.equalsIgnoreCase(NombrePagado) == false)) {
                    caso = 1;
                    //no sea ha encontrado al pagador O tiene 0 PC
                } else if (pagador == false || actualPagador == 0) {
                    caso = 2;
                    //la cantidad a pagar es superior al dinero del pagador
                } else if (cantidad >= actualPagador) {
                    caso = 3;
                    //el tontin se paga asi mismo
                } else if (NombrePagador.equalsIgnoreCase(NombrePagado) == true) {
                    caso = 4;
                    //no se ha encontrado al pagado
                } else if (pagado == false) {
                    caso = 5;
                } else {
                    caso = 6;
                }
            } catch (SQLException e) {
                p.sendMessage(ChatColor.RED + "Error en Comandos.pagarPixelCoins, hablar con el admin");
            }
        }

        switch (caso) {
            case 1:
                //quitar dinero al pagador
                try {
                    String consulta2 = "UPDATE jugadores SET pixelcoin = ?, espacios = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
                    PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);
                    pst.setInt(1, actualPagador - cantidad);
                    pst.setInt(2, espaciosActualesPagador);
                    pst.setInt(3, nventasPagador);
                    pst.setInt(4, ingresosPagador);
                    pst.setInt(5, gastosPagador + cantidad);
                    pst.setInt(6, ingresosPagador - (gastosPagador + cantidad));
                    pst.setString(7, NombrePagador);
                    pst.executeUpdate();
                } catch (SQLException e) {
                    p.sendMessage(ChatColor.RED + "Error en comandos.pagarPixelCoins.1, hablar con el admin");
                }
                // poner dinero al pagado
                try {
                    String consulta3 = "UPDATE jugadores SET pixelcoin = ?, espacios = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
                    PreparedStatement pst2 = (PreparedStatement) conexion.prepareStatement(consulta3);
                    pst2.setInt(1, actualPagado + cantidad);
                    pst2.setInt(2, espaciosActualesPagado);
                    pst2.setInt(3, nventasPagado + 1);
                    pst2.setInt(4, ingresosPagado + cantidad);
                    pst2.setInt(5, gastosPagado);
                    pst2.setInt(6, (ingresosPagado + cantidad) - gastosPagado);
                    pst2.setString(7, NombrePagado);
                    pst2.executeUpdate();
                } catch (SQLException e) {
                    p.sendMessage(ChatColor.RED + "Error en comandos.pagarPixelCoins.2, hablar con el admin");
                }
                tp = plugin.getServer().getPlayer(NombrePagado);
                p.sendMessage(ChatColor.GOLD + "Has pagado: " + ChatColor.RED + formatea.format(cantidad) + " PC." + ChatColor.GOLD + " Ahora tienes: " + ChatColor.GREEN + formatea.format(actualPagador - cantidad) + " PC");
                //enviar mensaje si esta online

                if (tp != null) {
                    tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                    tp.sendMessage(ChatColor.GOLD + NombrePagador + " te ha pagado: " + ChatColor.GREEN + "+" + formatea.format(cantidad) + " PC " + ChatColor.GOLD + "Ahora tienes: " +
                            ChatColor.GREEN + formatea.format(cantidad + actualPagado) + " PC");
                }

                break;
            case 2:
                p.sendMessage(ChatColor.DARK_RED + "Complicado pagar a alguien teniendo tu exactamente 0 pixelcoins :vv");
                break;
            case 3:
                p.sendMessage(ChatColor.DARK_RED + "No puedes dar mas dinero del que tienes :v");
                break;
            case 4:
                p.sendMessage(ChatColor.DARK_RED + "tu crees q el que ha hecho el plugin es tonto o algo lol");
                break;
            case 5:
                p.sendMessage(ChatColor.DARK_RED + "Ese men no ha jugado al server o tiene 0 pixelcoins :v");
            case 6:
                p.sendMessage(ChatColor.DARK_RED + "Como que no :v");
                break;
        }

    }

    //---------------Top ricos----------------
    public void topRicos(Player p) {
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

                p.sendMessage(ChatColor.GOLD + "" + i + "�: " + nombreActual + ":  " + ChatColor.GREEN + formatea.format(cantidadActual) + " PC");
            }
        } catch (SQLException e) {
            p.sendMessage(ChatColor.RED + "Error en Comandos.topRicos, hablar con el admin");
        }
    }

    //---------------Top pobres----------------
    public void topPobres(Player p) {
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

                p.sendMessage(ChatColor.GOLD + "" + i + "�: " + nombreActual + ":  " + ChatColor.GREEN + formatea.format(cantidadActual) + " PC");
            }
        } catch (SQLException e) {
            p.sendMessage(ChatColor.RED + "Error en Comandos.topPobres, hablar con el admin");
        }
    }

    //------------Top vendedores---------------
    public void topVendedores(Player p) {
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

                p.sendMessage(ChatColor.GOLD + "" + i + "�: " + nombreActual + ":  " + ChatColor.GREEN + formatea.format(nventas) + " ventas");
            }
        } catch (SQLException e) {
            p.sendMessage(ChatColor.RED + "Error en Comandos.topPobres, hablar con el admin");
        }
    }

    //-------------mostrar estadisticas-----------
    public void mostrarEstadisticas(Player p) {
        String nombreJugador = p.getName();
        int dinero = 0;
        int nventas = 0;
        int ingresos = 0;
        int gastos = 0;
        int beneficios = 0;

        try {
            String consulta = "SELECT * FROM jugadores";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("nombre").equalsIgnoreCase(nombreJugador)) {
                    nventas = rs.getInt("nventas");
                    ingresos = rs.getInt("ingresos");
                    gastos = rs.getInt("gastos");
                    beneficios = rs.getInt("beneficios");
                    dinero = rs.getInt("pixelcoin");
                }
            }

            if (dinero != 0) {
                p.sendMessage(ChatColor.GOLD + "Tus estadisticas (Todas medidas desde la tienda y del comando /pagar)");
                p.sendMessage(ChatColor.GOLD + "N� ventas (tienda): " + ChatColor.GREEN + nventas);
                p.sendMessage(ChatColor.GOLD + "Ingresos:  " + ChatColor.GREEN + ingresos);
                p.sendMessage(ChatColor.GOLD + "Gastos: " + ChatColor.GREEN + gastos);

                //Comprobar si el beneficio es negativo, si lo es lo ponemos en rojo sino el verde
                if (beneficios >= 0) {
                    p.sendMessage(ChatColor.GOLD + "Beneficios: " + ChatColor.GREEN + beneficios);
                } else {
                    p.sendMessage(ChatColor.GOLD + "Beneficios: " + ChatColor.RED + beneficios);
                }
            } else {
                p.sendMessage(ChatColor.GOLD + "Necesitas tener pixelcoins");
            }

        } catch (SQLException e) {
            p.sendMessage(ChatColor.DARK_RED + "Error en Comandos.mostrarEstadisticas");
        }
    }
}