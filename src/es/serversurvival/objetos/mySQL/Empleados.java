package es.serversurvival.objetos.mySQL;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.fabric.xmlrpc.base.Array;
import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empleado;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


import net.md_5.bungee.api.ChatColor;

public class Empleados extends MySQL {
    public DecimalFormat formatea = new DecimalFormat("###,###.##");

    public void nuevoEmpleado(String empleado, String empresa, double sueldo, String tipo, String cargo) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String fechaPaga = sdf.format(dt);

        try {
            String consulta = "INSERT INTO empleados (empleado, empresa, sueldo, cargo, tipo, fechaPaga) VALUES ('" + empleado + "','" + empresa + "','" + sueldo + "','" + cargo + "','" + tipo + "','" + fechaPaga + "')";
            Statement st2 = (Statement) conexion.createStatement();
            st2.executeUpdate(consulta);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean trabajaEmpresa(String empleado, String nombreEmpresa) {
        boolean trabaja = false;
        try {
            String consulta = "SELECT id FROM empleados WHERE empleado = ? AND empresa = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setString(1, empleado);
            pst.setString(2, nombreEmpresa);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                trabaja = true;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return trabaja;
    }

    public int getId(String empleado, String empresa) {
        int id = -1;
        try {
            String consulta = "SELECT id FROM empleados WHERE empleado = ? AND empresa = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, empleado);
            pst.setString(2, empresa);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                id = rs.getInt("id");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return id;
    }

    public String getEmpleado(int id) {
        String empleado = "";
        try {
            String consulta = "SELECT empleado FROM empleados WHERE id = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                empleado = rs.getString("empleado");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return empleado;
    }

    public String getEmpresa(int id) {
        String empresa = "";
        try {
            String consulta = "SELECT empresa FROM empleados WHERE id = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                empresa = rs.getString("empresa");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return empresa;
    }

    public double getSueldo(int id) {
        double sueldo = 0;
        try {
            String consulta = "SELECT sueldo FROM empleados WHERE id = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setDouble(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                sueldo = rs.getDouble("sueldo");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return sueldo;
    }

    public String getCargo(int id) {
        String cargo = "";
        try {
            String consulta = "SELECT cargo FROM empleados WHERE id = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                cargo = rs.getString("cargo");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return cargo;
    }

    public String getTipo(int id) {
        String tipo = "";
        try {
            String consulta = "SELECT tipo FROM empleados WHERE id = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                tipo = rs.getString("tipo");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return tipo;
    }

    public String getFechaPaga(int id) {
        String fecha = "";
        try {
            String consulta = "SELECT fechaPaga FROM empleados WHERE id = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                fecha = rs.getString("fechaPaga");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return fecha;
    }

    public void setSueldo(int id, double sueldo) {
        try {
            String consulta2 = "UPDATE empleados SET sueldo = ? WHERE id = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setDouble(1, sueldo);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void setCargo(int id, String cargo) {
        try {
            String consulta2 = "UPDATE empleados SET cargo = ? WHERE id = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setString(1, cargo);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void setTipo(int id, String tipo) {
        try {
            String consulta2 = "UPDATE empleados SET tipo = ? WHERE id = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setString(1, tipo);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void setFechaPaga(int id, String fechaPaga) {
        try {
            String consulta2 = "UPDATE empleados SET fechaPaga = ? WHERE id = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setString(1, fechaPaga);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void setEmpresa(int id, String empresa) {
        try {
            String consulta2 = "UPDATE empleados SET empresa = ? WHERE id = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setString(1, empresa);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void borrarEmplado(int id) {
        try {
            String consulta2 = "DELETE FROM empleados WHERE id=\"" + id + "\"      ";
            Statement st2 = conexion.createStatement();
            st2.executeUpdate(consulta2);
        } catch (SQLException e) {

        }
    }

    public List<Empleado> getEmpleadosEmrpesa(String nombreEmpresa){
        List<Empleado> empleados = new ArrayList<>();

        try {
            String consulta = "SELECT * FROM empleados WHERE empresa = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombreEmpresa);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                empleados.add(new Empleado(
                        rs.getInt("id"),
                        rs.getString("empleado"),
                        rs.getString("empresa"),
                        rs.getDouble("sueldo"),
                        rs.getString("cargo"),
                        rs.getString("tipo"),
                        rs.getString("fechaPaga")
                ));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empleados;
    }

    public void cambiarEmpresaNombre(String empresa, String nuevoNombre) {
        List<Empleado> empleados = this.getEmpleadosEmrpesa(empresa);

        for (int i = 0; i < empleados.size(); i++) {
            this.setEmpresa(empleados.get(i).getId(), nuevoNombre);
        }
    }

    public void despedir(String nombreEmpresa, String empleado, String razon, Player p) {
        Empresas em = new Empresas();

        boolean trabaja = this.trabajaEmpresa(empleado, nombreEmpresa);
        if (!trabaja) {
            p.sendMessage(ChatColor.DARK_RED + "Creo que ese men no trabaja en tu empresa");
            return;
        }

        try {
            em.conectar();
            boolean reg = em.estaRegistradoNombre(nombreEmpresa);

            if (!reg) {
                p.sendMessage(ChatColor.DARK_RED + "Esa empresa no exsiste");
                return;
            }
            boolean ow = em.esOwner(p.getName(), nombreEmpresa);
            if (!ow) {
                p.sendMessage(ChatColor.DARK_RED + "No eres owner de esa empresa");
                return;
            }
        } catch (Exception e) {

        }
        int id_emplado = this.getId(empleado, nombreEmpresa);
        this.borrarEmplado(id_emplado);

        p.sendMessage(ChatColor.GOLD + "Has despedido a: " + empleado);
        Player tp = p.getServer().getPlayer(empleado);

        if (tp.isOnline()) {
            tp.sendMessage(ChatColor.RED + "Has sido despedido de " + nombreEmpresa + " razon: " + razon);
            tp.playSound(tp.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
        } else {
            Mensajes men = new Mensajes();
            men.nuevoMensaje(empleado, "Has sido despedido de " + nombreEmpresa + " por: " + razon);
        }
        em.desconectar();
    }

    public void irseEmpresa(String nombreEmpresa, Player p) {
        Empresas em = new Empresas();
        String nombreOwner = "";

        boolean reg = em.estaRegistradoNombre(nombreEmpresa);

        if (!reg) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }

        boolean ow = em.esOwner(p.getName(), nombreEmpresa);
        if (ow) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes irte de tu propia empresa. /venderempresa y si realmente te quieres deshacer de ella: /borrarempresa");
            return;
        }
        nombreOwner = em.getOwner(nombreEmpresa);

        boolean trabaja = this.trabajaEmpresa(p.getName(), nombreEmpresa);
        if (!trabaja) {
            p.sendMessage(ChatColor.DARK_RED + "No te puedes ir de una empresa de la que no trabajas");
            return;
        }

        this.borrarEmplado(this.getId(p.getName(), nombreEmpresa));

        p.sendMessage(ChatColor.GOLD + "Te has ido de: " + nombreEmpresa);

        Player tp = p.getServer().getPlayer(nombreOwner);

        if (tp != null) {
            tp.sendMessage(ChatColor.RED + p.getName() + " Se ha ido de tu empresa: " + nombreEmpresa);
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
        } else {
            Mensajes men = new Mensajes();
            men.nuevoMensaje(nombreOwner, p.getName() + " se ha ido de tu empresa: " + nombreEmpresa);
        }
    }

    public void contratar(String empleado, String empresa, double sueldo, String tipo, String cargo, Player p, Player tp2) {
        String owner = p.getName();
        this.nuevoEmpleado(empleado, empresa, sueldo, tipo, cargo);

        p.sendMessage(ChatColor.GOLD + "Has enviado una solicitud para contratar a: " + empleado);

        switch (tipo) {
            case "s":
                tipo = "semana";
                break;
            case "2s":
                tipo = "2 semanas";
                break;
            case "m":
                tipo = "mes";
                break;
            case "d":
                tipo = "dia";
                break;
        }

        tp2.sendMessage(ChatColor.GOLD + owner + " te ha enviado una solicitud para trabajar en: " + ChatColor.DARK_AQUA + empresa + ChatColor.GOLD + " para trabajar como: " + cargo + " a " + ChatColor.GREEN + formatea.format(sueldo) + " PC/" + tipo
                + ChatColor.AQUA + " /aceptarTrabajo " + " /rechazarTrabajo");
        tp2.playSound(tp2.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public List<Empleado> getTrabajosJugador(String jugador){
        List<Empleado> empleados = new ArrayList<>();

        try {
            String consulta = "SELECT * FROM empleados WHERE empleado = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                empleados.add(new Empleado(
                        rs.getInt("id"),
                        rs.getString("empleado"),
                        rs.getString("empresa"),
                        rs.getDouble("sueldo"),
                        rs.getString("cargo"),
                        rs.getString("tipo"),
                        rs.getString("fechaPaga")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return empleados;
    }

    public void mostarTrabajos(Player p) {
        List<Empleado> trabajos = this.getTrabajosJugador(p.getName());

        String empresa;
        String cargo;
        double sueldo;
        String tipoSueldo;
        String ultimaPaga;

        p.sendMessage(ChatColor.GOLD + "Tus trabajos:");
        for(Empleado trabajo : trabajos) {
            empresa = trabajo.getEmpresa();
            cargo = trabajo.getCargo();
            sueldo = trabajo.getSueldo();
            tipoSueldo = trabajo.getTipo();
            ultimaPaga = trabajo.getFechaPaga();
            tipoSueldo = Empresas.toStringTipoSueldo(tipoSueldo);

            p.sendMessage(ChatColor.GOLD + "   Empresa: " + empresa + " con un sueldo: " + ChatColor.GREEN + sueldo + " PC/" + tipoSueldo + org.bukkit.ChatColor.GOLD + " con el cargo de: " + cargo + " La ultima paga: " + ultimaPaga);
        }
    }


    public void editarSueldo(String tname, String empresa, Player p, String tipo, String valor) {
        Empresas emp = new Empresas();

        boolean reg = emp.estaRegistradoNombre(empresa);
        if (!reg) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no exsiste");
            return;
        }
        boolean ow = emp.esOwner(p.getName(), empresa);
        if (!ow) {
            p.sendMessage(ChatColor.DARK_RED + "No eres due?o de eas empresa");
            return;
        }


        boolean trabaja = this.trabajaEmpresa(tname, empresa);
        Player tp = p.getServer().getPlayer(tname);

        if (!trabaja) {
            p.sendMessage(ChatColor.DARK_RED + "Ese jugador no trabaja en la empresa");
            return;
        }
        int id = this.getId(tname, empresa);
        if (tipo.equalsIgnoreCase("sueldo")) {
            double sueldoAntes = this.getSueldo(id);
            double valorInt = Double.parseDouble(valor);
            this.setSueldo(id, valorInt);

            p.sendMessage(ChatColor.GOLD + "Has cambiado el sueldo a " + tname + " a " + ChatColor.GREEN + formatea.format(valorInt) + " PC" + ChatColor.GOLD + " en la empresa: " + empresa);
            if (tp != null) {
                tp.sendMessage(ChatColor.GOLD + p.getName() + " te ha cambiado el sueldo de " + empresa + " a " + ChatColor.GREEN + formatea.format(valorInt) + " PC" + ChatColor.GOLD + " antes tenias: " + ChatColor.GREEN + formatea.format(sueldoAntes) + " PC");
                tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            } else {
                Mensajes men = new Mensajes();
                men.nuevoMensaje(tname, "Se te ha cambiado el sueldo de la empresa: " + empresa + " a " + formatea.format(valorInt) + " PC antes tenias " + sueldoAntes + " PC");
            }
        } else {
            String tipoString = "";
            switch (valor) {
                case "s":
                    tipoString = "semana";
                    break;
                case "2s":
                    tipoString = "2 por semana";
                    break;
                case "m":
                    tipoString = "mes";
                    break;
                case "d":
                    tipoString = "dia";
                    break;
            }
            this.setTipo(id, valor);
            double sueldo = this.getSueldo(id);
            if (tp != null) {
                tp.sendMessage(ChatColor.GOLD + p.getName() + " te ha cambiado el tiempo por por el que cobras el sueldo, ahora cobras " + ChatColor.GREEN + formatea.format(sueldo) + " PC" + ChatColor.GOLD + " por " + tipoString);
                tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            } else {
                Mensajes men = new Mensajes();
                men.nuevoMensaje(tname, "Se te ha cambiado la frecuencia con la que cobras en la empresa: " + empresa + " " + sueldo + " PC/ " + tipoString);
            }
            p.sendMessage(ChatColor.GOLD + "Has cambiado el tipo de pagado de sueldo de " + tname + " en la empresa " + empresa);
        }
    }

    public void pagarSueldos(Server se) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date hoy = new Date();
        Date actual = new Date();

        Funciones f = new Funciones();
        Transacciones t = new Transacciones();
        try {
            hoy = sdf.parse(sdf.format(hoy));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int id_empleado;
        double sueldo;
        long dif;
        boolean sePago = false;
        String empleado;
        String empresa;
        String tipoSueldo;
        String fechaPaga;
        String fechaHoyString = sdf.format(hoy);

        try {
            String consulta = "SELECT * FROM empleados";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                id_empleado = rs.getInt("id");
                empleado = rs.getString("empleado");
                empresa = rs.getString("empresa");
                tipoSueldo = rs.getString("tipo");
                fechaPaga = rs.getString("fechaPaga");
                sueldo = rs.getDouble("sueldo");
                actual = sdf.parse(fechaPaga);

                dif = Funciones.diferenciaDias(hoy, actual);
                switch (tipoSueldo) {
                    case "d":
                        if (dif < 1) {
                            continue;
                        }
                        break;
                    case "s":
                        if (dif < 7) {
                            continue;
                        }
                        break;
                    case "2s":
                        if (dif < 14) {
                            continue;
                        }
                        break;
                    case "m":
                        if (dif < 30) {
                            continue;
                        }
                        break;
                }
                Mensajes men = new Mensajes();
                sePago = t.pagarSalario(empleado, empresa, sueldo);
                if (!sePago) {
                    se.getConsoleSender().sendMessage(ChatColor.RED + "La empresa: " + empresa + " no ha podido pagar " + sueldo + " a " + empleado);
                    men.nuevoMensaje(empleado, "No has podido cobrar tu sueldo por parte de " + empresa + " por que no tiene las suficientes pixelcoins");
                    se.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "2");
                } else {
                    this.setFechaPaga(id_empleado, fechaHoyString);
                    men.nuevoMensaje(empleado, "Has cobrado " + sueldo + " PC de parte de la empresa: " + empresa);
                    se.getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha pagado " + sueldo + " a " + empleado + " en la empresa: " + empresa);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}