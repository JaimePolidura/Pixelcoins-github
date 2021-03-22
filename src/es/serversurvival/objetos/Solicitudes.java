package es.serversurvival.objetos;

import java.sql.*;
import java.text.DecimalFormat;

import com.mysql.jdbc.Connection;
import es.serversurvival.task.ScoreboardPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.serversurvival.main.Funciones;
import org.bukkit.scoreboard.Score;

@SuppressWarnings("SpellCheckingInspection")
public class Solicitudes extends MySQL {
    private int cuenta = 0;
    public DecimalFormat formatea = new DecimalFormat("###,###.##");

    public void eliminarSolicitudEnviador(String enviador) {
        try {
            String consulta = "DELETE FROM solicitudes WHERE enviador = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, enviador);
            pst.executeUpdate();
            ;
        } catch (SQLException e) {

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
            String consulta = "SELECT id_tabla FROM solicitudes WHERE enviador = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setString(1, enviador);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                getIdTabla = rs.getInt("id_tabla");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return getIdTabla;
    }

    public int getTipo(String destinatario) {
        int tipo = 0;
        try {
            String consulta = "SELECT tipo FROM solicitudes WHERE destinatario = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setString(1, destinatario);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                tipo = rs.getInt("tipo");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return tipo;
    }

    public String getEnviador(String destinatario) {
        String enviador = "";
        try {
            String consulta = "SELECT enviador FROM solicitudes WHERE destinatario = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setString(1, destinatario);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                enviador = rs.getString("enviador");
                break;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enviador;
    }

    public void nuevaSolicitud(String enviador, String destinatario, int tipo, int id_tabla, Player p) {
        try {
            String consulta = "INSERT INTO solicitudes (enviador, destinatario, tipo, id_tabla) VALUES ('" + enviador + "','" + destinatario + "','" + tipo + "','" + id_tabla + "')";
            Statement st2 = (Statement) conexion.createStatement();
            st2.executeUpdate(consulta);

            Timer t = new Timer();
            //Cronometro
            cuenta = 0;
            TimerTask tt = new TimerTask() {
                @SuppressWarnings("DuplicateBranchesInSwitch")
                @Override
                public void run() {
                    //Si han pasado menos de 15s se incrementa la cuenta asi hasta 15 que quiere decir que ha pasado 15s
                    if (cuenta < 15) {
                        cuenta++;
                    } else {
                        //Nos conectamos a la misma clase para poder acceder a los metodos de Solicitudes
                        try {
                            conectar();
                            //CODIGO:
                            boolean reg = true;
                            reg = estRegistradoEnviador(enviador);
                            if (reg) {
                                p.sendMessage(ChatColor.GOLD + "Tu solicitud ha expirado");
                            } else {
                                t.cancel();
                                return;
                            }
                            eliminarSolicitudEnviador(enviador);

                            //Borramos los registros de las tablas
                            switch (tipo) {
                                case 1:
                                    try {
                                        Deudas d = new Deudas();
                                        d.conectar();
                                        d.borrarDeuda(id_tabla);
                                        d.desconectar();
                                    } catch (Exception e) {

                                    }
                                    break;
                                case 2:
                                    try {
                                        Empleados em = new Empleados();
                                        em.conectar();
                                        em.borrarEmplado(id_tabla);
                                        em.desconectar();
                                    } catch (Exception e) {

                                    }
                                    break;
                                case 3:
                                    try {
                                        Transacciones t = new Transacciones();
                                        t.conectar();
                                        t.borrarTransaccione(id_tabla);
                                        t.desconectar();
                                    } catch (Exception e) {

                                    }
                                    break;
                                case 4:
                                    try {
                                        Transacciones t = new Transacciones();
                                        t.conectar();
                                        t.borrarTransaccione(id_tabla);
                                        t.desconectar();
                                    } catch (Exception e) {

                                    }
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
            d.conectar();
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
            em.conectar();
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

        this.eliminarSolicitudDestinatario(destinatario);

        pixelcoins = d.getPixelcoins(id);
        interes = d.getInteres(id);
        dias = d.getTiempo(id);

        Jugador j = new Jugador();
        pixelcoinsEnviador = j.getDinero(enviador);

        //Comprobamos si las PC de la deuda es mayor al dinero del propio jugador
        if (pixelcoins > pixelcoinsEnviador) {
            p.sendMessage(ChatColor.DARK_RED + "No se ha podido aceptar el prestamo ya que el que te lo ha enviado a puesto una cantidad superior a su dibero :v");
            tp.sendMessage(ChatColor.DARK_RED + "No puedes dar un prestamos que esta por encima de tu dinero :v");
            d.borrarDeuda(id);
            return;
        }

        //Hacer transferencia

        t.realizarTransferencia(enviador, destinatario, pixelcoins, "", Transacciones.TIPO.DEUDAS_PRIMERPAGO, false);
        ScoreboardPlayer sp = new ScoreboardPlayer();
        sp.updateScoreboard(p);

        p.sendMessage(ChatColor.GOLD + "Has aceptado la solicitud de: " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC " + ChatColor.GOLD + "con un interes del: " + ChatColor.GREEN + interes +
                ChatColor.GOLD + " a " + ChatColor.GREEN + dias + ChatColor.GOLD + " dias");

        if (tp.isOnline()) {
            sp.updateScoreboard(tp);
            tp.sendMessage(ChatColor.GOLD + p.getName() + " Te ha aceptado la solicitud de deuda");
            tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        }
    }

    public void aceptarSolicitudTrabajo(Player p) {
        String destinatario = p.getName();
        boolean reg = this.estRegistradoDestinatario(destinatario);

        if (!reg) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviador una solicitud");
            return;
        }

        String enviador = this.getEnviador(destinatario);
        int tipo = this.getTipo(destinatario);

        if (tipo != 2) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviado una solititud de trabajar");
            return;
        }

        this.eliminarSolicitudEnviador(enviador);
        Player tp = Bukkit.getServer().getPlayer(enviador);
        tp.sendMessage(ChatColor.GOLD + "Has contratado a " + destinatario + ChatColor.AQUA + " /despedir /editarempleado");
        p.sendMessage(ChatColor.GOLD + "Ahora trabajas para " + enviador + ChatColor.AQUA + " /irse /mistrabajos");
    }

    public void aceptarSolicitudCompra(Player p) {
        String destinatario = p.getName();
        String enviador = this.getEnviador(destinatario);
        String empresa = "";
        int precio = 0;
        Jugador j = new Jugador();

        boolean reg = this.estRegistradoDestinatario(destinatario);
        if (!reg) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviado una solicitud");
            return;
        }
        int tipo = this.getTipo(destinatario);
        if (tipo != 3) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviado una solicitud de tiupo de compra de empresa");
            return;
        }

        int id = this.getIdTabla(enviador);
        Transacciones t = new Transacciones();

        empresa = t.getObjeto(id);
        precio = t.getCantidad(id);

        if (j.getDinero(destinatario) < precio) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes ir por encima de tus probabilidades");
            t.borrarTransaccione(id);
            return;
        }
        this.eliminarSolicitudEnviador(enviador);

        ArrayList<String> empleados = new ArrayList<String>();

        boolean trabaja = false;
        Empleados emp = new Empleados();
        trabaja = emp.trabajaEmpresa(p.getName(), empresa);

        if (trabaja) {
            int id_empleado = emp.getId(p.getName(), empresa);
            emp.borrarEmplado(id_empleado);
        }
        empleados = emp.getNombreEmpleadosEmpresa(empresa);

        Mensajes men = new Mensajes();
        for (int i = 0; i < empleados.size(); i++) {
            men.nuevoMensaje(empleados.get(i), "La empresa en la que trabajas " + empresa + " ha cambiado de owner a " + p.getName());
        }

        t.comprarEmpresa(enviador, destinatario, empresa, precio, p);

        p.sendMessage(ChatColor.GOLD + "Ahora eres dueño de " + ChatColor.DARK_AQUA + empresa + ChatColor.GOLD + " ,la has comprado por " + ChatColor.GREEN + precio + " PC");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Player tp = p.getServer().getPlayer(enviador);
        if (tp != null) {
            tp.sendMessage(ChatColor.GOLD + destinatario + " te ha comprado " + ChatColor.DARK_AQUA + empresa + ChatColor.GOLD + " por " + ChatColor.GREEN + precio + " PC " + ChatColor.GOLD + ", ahora ya no eres dueño");
            tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        }

        ScoreboardPlayer sp = new ScoreboardPlayer();
        sp.updateScoreboard(p);
        sp.updateScoreboard(tp);
    }

    public void rechazarSolicitudCompra(Player p) {
        String destinatario = p.getName();
        String enviador = this.getEnviador(destinatario);

        boolean reg = this.estRegistradoDestinatario(destinatario);
        if (!reg) {
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
            t.conectar();
            t.borrarTransaccione(id);
            this.eliminarSolicitudEnviador(enviador);
            t.desconectar();
        } catch (Exception e) {
            return;
        }
        p.sendMessage(ChatColor.GOLD + "Has rechazado la solicitud");
        Player tp = p.getServer().getPlayer(enviador);
        if (tp != null) {
            tp.sendMessage(ChatColor.DARK_RED + destinatario + " te ha rechazado la solicutud de comprar tu empresa");
        }

    }

    public void aceptarSolicitudBorrar(Player p) {
        String jugador = p.getName();

        if (!this.estRegistradoDestinatario(jugador)) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviado ninguna solicitud");
            return;
        }
        if (!this.estRegistradoEnviador(jugador)) {
            p.sendMessage(ChatColor.DARK_RED + "No te has enviado ninguna solicitud");
            return;
        }
        if (this.getTipo(p.getName()) != 4) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviado una solicitud de ese tipo");
            return;
        }
        int id_tabla = this.getIdTabla(p.getName());
        String empresa = "";
        this.eliminarSolicitudEnviador(p.getName());

        Transacciones t = new Transacciones();
        empresa = t.getObjeto(id_tabla);

        ArrayList<String> empleados = new ArrayList<String>();
        ArrayList<Integer> idEmpleados = new ArrayList<Integer>();

        Empleados empl = new Empleados();
        empleados = empl.getNombreEmpleadosEmpresa(empresa);
        idEmpleados = empl.getidEmpleadosEmpresa(empresa);
        for (int i = 0; i < idEmpleados.size(); i++) {
            empl.borrarEmplado(idEmpleados.get(i));
        }

        int liquidez = 0;
        Empresas em = new Empresas();
        liquidez = em.getPixelcoins(empresa);
        em.borrarEmpresa(empresa);

        Jugador j = new Jugador();
        j.setPixelcoin(p.getName(), j.getDinero(p.getName()) + liquidez);
        this.eliminarSolicitudEnviador(p.getName());

        p.sendMessage(ChatColor.GOLD + "Has borrado tu empresa: " + empresa + ", se ha retirado a tu cuenta un total de " + ChatColor.GREEN + liquidez + " PC");

        Player tp = null;
        Mensajes men = new Mensajes();

        for (int i = 0; i < empleados.size(); i++) {
            tp = p.getServer().getPlayer(empleados.get(i));

            if (tp != null) {
                tp.sendMessage(ChatColor.GOLD + p.getName() + " ha borrado su empresa donde trabajabas: " + empresa);
            } else {
                men.nuevoMensaje(empleados.get(i), "El owner de la empresa en la que trabajas: " + empresa + " la ha borrado, ya no existe");
            }
        }
        ScoreboardPlayer sp = new ScoreboardPlayer();
        sp.updateScoreboard(p);
    }

    public void rechazarSolicitudBorrar(Player p) {
        String jugador = p.getName();

        if (!this.estRegistradoDestinatario(jugador)) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviado ninguna solicitud");
            return;
        }
        if (!this.estRegistradoEnviador(jugador)) {
            p.sendMessage(ChatColor.DARK_RED + "No te has enviado ninguna solicitud");
            return;
        }
        if (this.getTipo(p.getName()) != 4) {
            p.sendMessage(ChatColor.DARK_RED + "No te han enviado una solicitud de ese tipo");
            return;
        }

        int id_tabla = this.getIdTabla(p.getName());

        try {
            Transacciones t = new Transacciones();
            t.conectar();
            t.borrarTransaccione(id_tabla);
            this.eliminarSolicitudEnviador(p.getName());
            t.desconectar();
        } catch (Exception e) {

        }

        p.sendMessage(ChatColor.GOLD + "Has cancelado borrar tu empresa");
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
                            d.conectar();
                            d.borrarDeuda(id_tabla);
                            d.desconectar();
                        } catch (Exception e) {

                        }
                        break;
                    case 2:
                        try {
                            Empleados em = new Empleados();
                            em.conectar();
                            em.borrarEmplado(id_tabla);
                            em.desconectar();
                        } catch (Exception e) {

                        }
                    case 3:
                    case 4:
                        try {
                            Transacciones t = new Transacciones();
                            t.conectar();
                            t.borrarTransaccione(id_tabla);
                            t.desconectar();
                        } catch (Exception e) {

                        }
                        break;
                    default:
                        break;
                }
                se.getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha borrado una solicitud en id: " + id_tabla + " tipo: " + tipo);
                pst2.setString(1, enviador);
                pst2.executeUpdate();
            }
        } catch (SQLException e) {

        }
    }

    public boolean estRegistradoEnviador(String enviador) {
        boolean registrado = false;

        try {
            String consulta = "SELECT enviador FROM solicitudes WHERE enviador = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setString(1, enviador);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                registrado = true;
            }
        } catch (Exception e) {

        }
        return registrado;
    }

    public boolean estRegistradoDestinatario(String destinatario) {
        boolean registrado = false;

        try {
            String consulta = "SELECT destinatario FROM solicitudes WHERE destinatario = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setString(1, destinatario);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                registrado = true;
            }
        } catch (Exception e) {

        }
        return registrado;
    }
}