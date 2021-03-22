package es.serversurvival.objetos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Server;
import org.bukkit.entity.Player;


import net.md_5.bungee.api.ChatColor;

public class Deudas extends MySQL {

    public void nuevaDeuda(String deudor, String acredor, int pixelcoins, int tiempo, int interes) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdf.format(dt);
        try {
            int cuota = (int) Math.round((double) pixelcoins / tiempo);

            String consulta = "INSERT INTO deudas (deudor, acredor, pixelcoins, tiempo, interes, cuota, fecha) VALUES ('" + deudor + "','" + acredor + "','" + pixelcoins + "','" + tiempo + "','" + interes + "','" + cuota + "','" + fecha + "')";
            Statement st2 = (Statement) conexion.createStatement();
            st2.executeUpdate(consulta);
        } catch (SQLException e) {

        }
    }

    public int getTodaDeudaDeudor(String nombre) {
        int deuda = 0;

        try {
            String consulta1 = "SELECT * FROM deudas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta1);

            while (rs.next()) {
                if (rs.getString("deudor").equalsIgnoreCase(nombre)) {
                    deuda = rs.getInt("pixelcoins") + deuda;
                }
            }
        } catch (SQLException e) {

        }
        return deuda;
    }

    public int getTodaDeudaAcredor(String nombre) {
        int deuda = 0;

        try {
            String consulta1 = "SELECT * FROM deudas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta1);

            while (rs.next()) {
                if (rs.getString("acredor").equalsIgnoreCase(nombre)) {
                    deuda = rs.getInt("pixelcoins") + deuda;
                }
            }
        } catch (SQLException e) {

        }
        return deuda;
    }

    public void mostarDeudas(Player p) {
        String nombre = p.getName();
        try {
            String consulta1 = "SELECT * FROM deudas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta1);

            String nombret = "";
            int pixelcoins = 0;
            int dias = 0;
            int cuota = 0;
            int interes = 0;

            p.sendMessage(ChatColor.GOLD + "Jugadores que te deben: " + ChatColor.AQUA + "(/estadisticas)");
            while (rs.next()) {
                if (rs.getString("acredor").equalsIgnoreCase(nombre)) {
                    interes = rs.getInt("interes");
                    nombret = rs.getString("deudor");
                    pixelcoins = rs.getInt("pixelcoins");
                    dias = rs.getInt("tiempo");
                    cuota = rs.getInt("cuota");

                    p.sendMessage("   " + ChatColor.GOLD + nombret + " " + ChatColor.GREEN + pixelcoins + " PC " + ChatColor.GOLD + "(Con " + interes + " interes aplicado) con cuota " + ChatColor.GREEN + cuota + " PC/dia " + ChatColor.GOLD + " (" + dias + " dias)");
                }
            }
            p.sendMessage(ChatColor.GOLD + "Jugadores que debes:");
            rs = st.executeQuery(consulta1);
            while (rs.next()) {
                if (rs.getString("deudor").equalsIgnoreCase(nombre)) {
                    interes = rs.getInt("interes");
                    nombret = rs.getString("acredor");
                    pixelcoins = rs.getInt("pixelcoins");
                    dias = rs.getInt("tiempo");
                    cuota = rs.getInt("cuota");

                    p.sendMessage("   " + ChatColor.GOLD + "Debes a " + nombret + " " + ChatColor.GREEN + pixelcoins + " PC " + ChatColor.GOLD + "(Con " + interes + " interes aplicado) con cuota " + ChatColor.GREEN + cuota + " PC/dia" + ChatColor.GOLD + " (" + dias + " dias)");
                }
            }

        } catch (SQLException e) {

        }
    }

    public int getMaxId() {
        int id = 0;

        try {
            String consulta3 = "SELECT * FROM deudas ORDER BY id_deuda DESC LIMIT 1 ";
            Statement st3 = (Statement) conexion.createStatement();
            ResultSet rs3;
            rs3 = st3.executeQuery(consulta3);

            while (rs3.next()) {
                id = rs3.getInt("id_deuda");
            }
        } catch (SQLException e) {

        }
        return id;
    }

    public String getDeudor(int id) {
        String deudor = "";
        try {
            String consulta = "SELECT * FROM deudas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id_deuda") == id) {
                    deudor = rs.getString("deudor");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return deudor;
    }

    public String getAcredor(int id) {
        String acredor = "";
        try {
            String consulta = "SELECT * FROM deudas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id_deuda") == id) {
                    acredor = rs.getString("acredor");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return acredor;
    }

    public int getPixelcoins(int id) {
        int pixelcoins = 0;
        try {
            String consulta = "SELECT * FROM deudas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id_deuda") == id) {
                    pixelcoins = rs.getInt("pixelcoins");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return pixelcoins;
    }

    public int getTiempo(int id) {
        int tiempo = 0;
        try {
            String consulta = "SELECT * FROM deudas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id_deuda") == id) {
                    tiempo = rs.getInt("tiempo");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return tiempo;
    }

    public int getInteres(int id) {
        int interes = 0;
        try {
            String consulta = "SELECT * FROM deudas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id_deuda") == id) {
                    interes = rs.getInt("interes");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return interes;
    }

    public int getCuota(int id) {
        int cuota = 0;
        try {
            String consulta = "SELECT * FROM deudas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id_deuda") == id) {
                    cuota = rs.getInt("cuota");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return cuota;
    }

    public String getFecha(int id) {
        String fecha = "";
        try {
            String consulta = "SELECT * FROM deudas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id_deuda") == id) {
                    fecha = rs.getString("fecha");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return fecha;
    }

    public void setPagoDeuda(int id, int pixelcoins, int tiempo, String fecha) {
        try {
            String consulta2 = "UPDATE deudas SET pixelcoins = ?, tiempo = ?, fecha = ? WHERE id_deuda = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setInt(1, pixelcoins);
            pst.setInt(2, tiempo);
            pst.setString(3, fecha);
            pst.setInt(4, id);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void borrarDeuda(int id) {
        try {
            String consulta = "DELETE FROM deudas WHERE id_deuda = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void pagarDeuda(Server se) {
        int idActual = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String consulta = "SELECT * FROM deudas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);


            while (rs.next()) {
                idActual = rs.getInt("id_deuda");
                String fecha = rs.getString("fecha");
                Date fechaActual = null;
                Date fechaHoy = null;

                String acredor = rs.getString("acredor");
                String deudor = rs.getString("deudor");
                int cuota = rs.getInt("cuota");
                int tiempo = rs.getInt("tiempo");
                int pixelcoins = rs.getInt("pixelcoins");
                int pixelcoinsDeudor = 0;

                //Conseguimos al fecha de hoy a las ponemos al mismo formato que la fechaActual de la base dedatos
                try {
                    Date dt = new Date();

                    fechaActual = sdf.parse(fecha);
                    fechaHoy = sdf.parse(sdf.format(dt));

                } catch (Exception e) {
                    se.getConsoleSender().sendMessage(ChatColor.DARK_RED + " Error al pagar deuda.1 en id: " + idActual);
                    return;
                }
                Jugador j = new Jugador();
                try {
                    j.conectar("root", "", "pixelcoins");
                    pixelcoinsDeudor = j.getDinero(deudor);
                    j.desconectar();
                } catch (Exception e) {

                }

                int cuincide = fechaHoy.compareTo(fechaActual);

                //Si no cuincide la fecha de hoy y la de la base de datos quiere decir que hay que pagar la deuda
                if (cuincide != 0) {
                    //Comprobamos que el deudor tenga el dinero suciente para pagar la cuota
                    if (pixelcoinsDeudor >= cuota) {
                        //Realizamos transaccion de dinero
                        try {
                            Transacciones t = new Transacciones();
                            t.conectar("root", "", "pixelcoins");
                            t.realizarTransferencia(deudor, acredor, cuota, "", "PAGAR_CUOTA", true);
                            t.desconectar();
                        } catch (Exception e) {
                            se.getConsoleSender().sendMessage(ChatColor.DARK_RED + " Error al pagar en deuda id: " + idActual);
                            return;
                        }
                        //Le a?adimos un npago al deudor ya que ha pagado la deuda
                        try {
                            j.conectar("root", "", "pixelcoins");
                            int npagos = j.getNpagos(deudor) + 1;
                            j.setNpagos(deudor, npagos);
                            j.desconectar();
                        } catch (Exception e) {
                            se.getConsoleSender().sendMessage(ChatColor.DARK_RED + " Error al pagar en deuda id: " + idActual);
                            return;
                        }

                        se.getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha pagado en id: " + idActual);

                        Mensajes men = new Mensajes();
                        men.nuevoMensaje(deudor, "Has pagado " + cuota + " PC por la deuda que tienes con " + acredor + " a " + (tiempo - 1) + " dias");
                        men.nuevoMensaje(acredor, deudor + " te ha pagado " + cuota + " PC por la deuda que tiene a " + (tiempo - 1) + " dias contigo");

                        //Si solo le queda 1 dia para pagar la deuda borramos la deuda sino no
                        if (tiempo == 1) {
                            this.borrarDeuda(idActual);
                            se.getConsoleSender().sendMessage(ChatColor.GREEN + "Borrada deuda en id: " + idActual);
                            men.nuevoMensaje(deudor, "Has acabado de pagar la deuda con " + acredor);
                            men.nuevoMensaje(acredor, deudor + " ha acabado de pagar la deuda contigo");
                        } else {
                            String fechaHoyS = sdf.format(fechaHoy);
                            this.setPagoDeuda(idActual, pixelcoins - cuota, tiempo - 1, fechaHoyS);
                        }
                    } else {
                        //Al no poder pagar la deuda le a?adimos un inpago
                        try {
                            j.conectar("root", "", "pixelcoins");
                            int ninpagos = j.getNinpago(deudor) + 1;
                            j.setNinpagos(deudor, ninpagos);
                            j.desconectar();
                            se.getConsoleSender().sendMessage(ChatColor.GREEN + " No se puede pagar en id: " + idActual + " por falta de pixelcoins");
                            Mensajes men = new Mensajes();

                            men.nuevoMensaje(acredor, deudor + " no te ha podido pagar ese dia la deuda por falta de pixelcoins");
                            men.nuevoMensaje(deudor, "no has podido pagar un dia la deuda con " + acredor);
                        } catch (Exception e) {
                            se.getConsoleSender().sendMessage(ChatColor.DARK_RED + " Error al pagar en deuda id: " + idActual);
                            return;
                        }
                        return;
                    }
                }
            }
        } catch (SQLException e) {
            se.getConsoleSender().sendMessage(ChatColor.DARK_RED + " Error al pagar en deuda id: " + idActual);
        }
    }
}