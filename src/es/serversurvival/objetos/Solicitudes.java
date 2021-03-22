package es.serversurvival.objetos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Timer;
import java.util.TimerTask;

import es.serversurvival.config.Funciones;

public class Solicitudes extends MySQL {
    private Timer t;
    private int cuenta = 0;
    public DecimalFormat formatea = new DecimalFormat("###,###.##");

    public void eliminarSolicitudEnviador(String enviador, Player p) {
        try {
            String consulta = "DELETE FROM solicitudes WHERE enviador = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, enviador);
            pst.executeUpdate();
            ;
        } catch (SQLException e) {
            p.sendMessage("wtf");
        }
    }

    public void eliminarSolicitudDestinatario(String destinatario) {
        try {
            String consulta = "DELETE FROM solicitudes WHERE destinatario = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, destinatario);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    private int getIdTabla(String enviador) {
        int getIdTabla = 0;
        try {
            String consulta = "SELECT * FROM solicitudes";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("enviador").equalsIgnoreCase(enviador)) {
                    getIdTabla = rs.getInt("id_tabla");
                    break;
                }
            }

        } catch (SQLException e) {

        }
        return getIdTabla;
    }

    public int getTipo(String destinatario) {
        int tipo = 0;
        try {
            String consulta = "SELECT * FROM solicitudes";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("destinatario").equalsIgnoreCase(destinatario)) {
                    tipo = rs.getInt("tipo");
                    break;
                }
            }

        } catch (SQLException e) {

        }
        return tipo;
    }

    public String getEnviador(String destinatario) {
        String enviador = "";
        try {
            String consulta = "SELECT * FROM solicitudes";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("destinatario").equalsIgnoreCase(destinatario)) {
                    enviador = rs.getString("enviador");
                    break;
                }
            }

        } catch (SQLException e) {

        }
        return enviador;
    }

    public void nuevaSolicitud(String enviador, String destinatario, int tipo, int id_tabla, Player p) {
        try {
            String consulta = "INSERT INTO solicitudes (enviador, destinatario, tipo, id_tabla) VALUES ('" + enviador + "','" + destinatario + "','" + tipo + "','" + id_tabla + "')";
            Statement st2 = (Statement) conexion.createStatement();
            st2.executeUpdate(consulta);

            t = new Timer();
            //Cronometro
            cuenta = 0;
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    //Si han pasado menos de 15s se incrementa la cuenta asi hasta 15 que quiere decir que ha pasado 15s
                    if (cuenta < 15) {
                        cuenta++;
                    } else {
                        //Nos conectamos a la misma clase para poder acceder a los metodos de Solicitudes
                        try {
                            conectar("root", "", "pixelcoins");
                            //CODIGO:
                            boolean reg = true;
                            reg = estRegistradoEnviador(enviador);
                            if (reg) {
                                p.sendMessage(ChatColor.GOLD + "Tu solicitud ha expirado");
                            } else {
                                t.cancel();
                                return;
                            }
                            eliminarSolicitudEnviador(enviador, p);

                            //Borramos los registros de las tablas
                            switch (tipo) {
                                case 1:
                                    try {
                                        Deudas d = new Deudas();
                                        d.conectar("root", "", "pixelcoins");
                                        d.borrarDeuda(id_tabla);
                                        d.desconectar();
                                    } catch (Exception e) {

                                    }
                                    break;
                                case 2:
                                    try {
                                        Empleados em = new Empleados();
                                        em.conectar("root", "", "pixelcoins");
                                        em.borrarEmplado(id_tabla);
                                        em.desconectar();
                                    } catch (Exception e) {

                                    }
                                    break;
                                case 3:
                                    try {
                                        Transacciones t = new Transacciones();
                                        t.conectar("root", "", "pixelcoins");
                                        t.borrarTransaccione(id_tabla);
                                        t.desconectar();
                                    } catch (Exception e) {

                                    }
                                    break;
                                default:
                                    break;
                            }
                            desconectar();
                        } catch (Exception e) {

                        }
                        //Eliminamos el registro en la tabla solicitude

                        t.cancel();
                    }
                }
            };
            t.schedule(tt, 0, 1500);
        } catch (SQLException e) {

        }
    }

    public void cancelarSolicitudDeuda(Player p) {
        String destinatario = p.getName();

        int tipo = this.getTipo(destinatario);

        if (tipo != 1) {
            p.sendMessage(ChatColor.DARK_RED + "No te han mandado una solicitud de prestamo");
            return;
        }

        String enviador = this.getEnviador(destinatario);
        int id_tabla = this.getIdTabla(enviador);

        this.eliminarSolicitudDestinatario(destinatario);
        Player tp = Bukkit.getServer().getPlayer(enviador);

        try {
            Deudas d = new Deudas();
            d.conectar("root", "", "pixelcoins");
            d.borrarDeuda(id_tabla);
            d.desconectar();
        } catch (Exception e) {

        }


        p.sendMessage(ChatColor.GOLD + "Has cancelado la solicitud");

        if (tp.isOnline()) {
            tp.sendMessage(ChatColor.GOLD + p.getName() + " te ha cancelado la solicitud!");
            tp.playSound(tp.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
    }

    public void cancelarSolicitudTrabajo(Player p) {
        String destinatario = p.getName();

        int tipo = this.getTipo(destinatario);

        if (tipo != 2) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviado una solicitud para aceptar un trabajo");
            return;
        }

        String enviador = this.getEnviador(destinatario);
        int id_tabla = this.getIdTabla(enviador);

        this.eliminarSolicitudDestinatario(destinatario);
        Player tp = Bukkit.getServer().getPlayer(enviador);

        try {
            Empleados em = new Empleados();
            em.conectar("root", "", "pixelcoins");
            em.borrarEmplado(id_tabla);
            em.desconectar();
        } catch (Exception e) {

        }


        p.sendMessage(ChatColor.GOLD + "Has cancelado la solicitud");

        if (tp.isOnline()) {
            tp.sendMessage(ChatColor.GOLD + p.getName() + " te ha cancelado la solicitud!");
            tp.playSound(tp.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
    }

    public void aceptarSolicitudDeuda(Player p) {
        String destinatario = p.getName();
        int tipo = 0;
        tipo = this.getTipo(destinatario);

        if (tipo != 1) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviado una solicitud para un prestamo");
            return;
        }

        Funciones f = new Funciones();
        Transacciones t = new Transacciones();
        Deudas d = new Deudas();

        int pixelcoinsEnviador = 0;
        String enviador = this.getEnviador(destinatario);
        int id = this.getIdTabla(enviador);
        Player tp = Bukkit.getServer().getPlayer(enviador);
        int pixelcoins = 0;
        int interes = 0;
        int dias = 0;

        try {
            d.conectar("root", "", "pixelcoins");
            pixelcoins = d.getPixelcoins(id);
            interes = d.getInteres(id);
            dias = d.getTiempo(id);
            d.desconectar();
        } catch (Exception e) {

        }

        //Conseguir dinero del jugador
        try {
            Jugador j = new Jugador();
            j.conectar("root", "", "pixelcoins");
            pixelcoinsEnviador = j.getDinero(enviador);
            j.desconectar();
        } catch (Exception e) {

        }

        //Comprobamos si las PC de la deuda es mayor al dinero del propio jugador
        if (pixelcoins > pixelcoinsEnviador) {
            p.sendMessage(ChatColor.DARK_RED + "No se ha podido aceptar el prestamo ya que el que te lo ha enviado a puesto una cantidad superior a su dibero :v");
            tp.sendMessage(ChatColor.DARK_RED + "No puedes dar un prestamos que esta por encima de tu dinero :v");
            return;
        }

        //Hacer transferencia
        try {
            t.conectar("root", "", "pixelcoins");
            t.realizarTransferencia(enviador, destinatario, pixelcoins, "", "DEUDA", false);
            t.desconectar();
        } catch (Exception e) {

        }

        this.eliminarSolicitudEnviador(enviador, p);

        p.sendMessage(ChatColor.GOLD + "Has aceptado la solicitud de: " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC " + ChatColor.GOLD + "con un interes del: " + ChatColor.GREEN + interes + "% (" + formatea.format(f.interes(pixelcoins, interes)) + " PC )" +
                ChatColor.GOLD + " a " + ChatColor.GREEN + dias + ChatColor.GOLD + " dias");

        if (tp.isOnline()) {
            tp.sendMessage(ChatColor.GOLD + p.getName() + " Te ha aceptado la solicitud de deuda");
            tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        }
    }

    public void aceptarSolicitudTrabajo(Player p) {
        String destinatario = p.getName();
        boolean reg = this.estRegistradoDestinatario(destinatario);

        if (reg == false) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviador una solicitud");
            return;
        }

        String enviador = this.getEnviador(destinatario);
        int tipo = this.getTipo(destinatario);

        if (tipo != 2) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviado una solititud de trabajar");
            return;
        }

        this.eliminarSolicitudEnviador(enviador, p);
        Player tp = Bukkit.getServer().getPlayer(enviador);
        tp.sendMessage(ChatColor.GOLD + "Has contratado a " + destinatario + ChatColor.AQUA + " /despedir /editarempleado");
        p.sendMessage(ChatColor.GOLD + "Ahora trabajas para " + enviador + ChatColor.AQUA + " /irse /mistrabajos");
    }

    public void aceptarSolicitudCompra(Player p) {
        String destinatario = p.getName();
        String enviador = this.getEnviador(destinatario);
        String empresa = "";
        int precio = 0;

        boolean reg = this.estRegistradoDestinatario(destinatario);
        if (reg == false) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviado una solicitud");
        }
        int tipo = this.getTipo(destinatario);
        if (tipo != 3) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviado una solicitud de tiupo de compra de empresa");
            return;
        }

        int id = this.getIdTabla(enviador);

        try {
            Transacciones t = new Transacciones();
            t.conectar("root", "", "pixelcoins");
            empresa = t.getObjeto(id);
            precio = t.getCantidad(id);
            t.desconectar();

            try {
                boolean trabaja = false;
                Empleados emp = new Empleados();
                emp.conectar("root", "", "pixelcoins");
                trabaja = emp.trabajaEmpresa(p.getName(), empresa);

                if (trabaja == true) {
                    int id_empleado = emp.getId(p.getName(), empresa);
                    emp.borrarEmplado(id_empleado);
                }
                emp.desconectar();
            } catch (Exception e) {

            }
            t.conectar("root", "", "pixelcoins");
            t.comprarEmpresa(enviador, destinatario, empresa, precio, p);
            t.desconectar();
        } catch (Exception e) {

        }
        this.eliminarSolicitudEnviador(enviador, p);

        p.sendMessage(ChatColor.GOLD + "Ahora eres due�o de " + ChatColor.DARK_AQUA + empresa + ChatColor.GOLD + " ,la has comprado por " + ChatColor.GREEN + precio + " PC");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Player tp = p.getServer().getPlayer(enviador);
        if (tp != null) {
            tp.sendMessage(ChatColor.GOLD + destinatario + " te ha comprado " + ChatColor.DARK_AQUA + empresa + ChatColor.GOLD + " por " + ChatColor.GREEN + precio + " PC " + ChatColor.GOLD + ", ahora ya no eres due�o");
            tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        }
    }

    public void rechazarSolicitudCompra(Player p) {
        String destinatario = p.getName();
        String enviador = this.getEnviador(destinatario);

        boolean reg = this.estRegistradoDestinatario(destinatario);
        if (reg == false) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviado una solicitud");
        }
        int tipo = this.getTipo(destinatario);
        if (tipo != 3) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviado una solicitud de tiupo de compra de empresa");
            return;
        }
        int id = this.getIdTabla(enviador);
        try {
            Transacciones t = new Transacciones();
            t.conectar("root", "", "pixelcoins");
            t.borrarTransaccione(id);
            t.desconectar();
        } catch (Exception e) {
            return;
        }
        this.eliminarSolicitudEnviador(enviador, p);
        p.sendMessage(ChatColor.GOLD + "Has rechazado la solicitud");
        Player tp = p.getServer().getPlayer(enviador);
        if (tp != null) {
            tp.sendMessage(ChatColor.DARK_RED + destinatario + " te ha rechazado la solicutud de comprar tu empresa");
        }

    }

    public void setUp(Server se) {
        try {
            String consulta = "SELECT * FROM solicitudes";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            String consulta2 = "DELETE FROM solicitudes WHERE enviador = ?";
            PreparedStatement pst2 = (PreparedStatement) conexion.prepareStatement(consulta2);

            String enviador = "";

            while (rs.next()) {
                enviador = rs.getString("enviador");
                int tipo = rs.getInt("tipo");
                int id_tabla = rs.getInt("id_tabla");

                switch (tipo) {
                    case 1:
                        try {
                            Deudas d = new Deudas();
                            d.conectar("root", "", "pixelcoins");
                            d.borrarDeuda(id_tabla);
                            d.desconectar();
                        } catch (Exception e) {

                        }
                        break;
                    case 2:
                        try {
                            Empleados em = new Empleados();
                            em.conectar("root", "", "pixelcoins");
                            em.borrarEmplado(id_tabla);
                            em.desconectar();
                        } catch (Exception e) {

                        }
                    case 3:
                        try {
                            Transacciones t = new Transacciones();
                            t.conectar("root", "", "pixelcoins");
                            t.borrarTransaccione(id_tabla);
                            t.desconectar();
                        } catch (Exception e) {

                        }
                        break;
                    default:
                        break;
                }
                se.getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha borrado una solicitud en id: " + id_tabla + " tipo: " + tipo);
                pst2.executeUpdate();
            }
        } catch (SQLException e) {

        }
    }

    public boolean estRegistradoEnviador(String enviador) {
        boolean registrado = false;

        try {
            String consulta = "SELECT * FROM solicitudes";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("enviador").equalsIgnoreCase(enviador)) {
                    registrado = true;
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return registrado;
    }

    public boolean estRegistradoDestinatario(String destinatario) {
        boolean registrado = false;

        try {
            String consulta = "SELECT * FROM solicitudes";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("destinatario").equalsIgnoreCase(destinatario)) {
                    registrado = true;
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return registrado;
    }
}