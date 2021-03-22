package es.serversurvival.objetos.mySQL;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empleado;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


import net.md_5.bungee.api.ChatColor;

public class Empleados extends MySQL {
    public DecimalFormat formatea = new DecimalFormat("###,###.##");
    private SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
    private static final List<String> tipoSueldos = Arrays.asList("d", "s", "2s", "m");

    public void nuevoEmpleado(String empleado, String empresa, double sueldo, String tipo, String cargo) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaPaga = sdf.format(dt);

        executeUpdate("INSERT INTO empleados (empleado, empresa, sueldo, cargo, tipo, fechaPaga) VALUES ('" + empleado + "','" + empresa + "','" + sueldo + "','" + cargo + "','" + tipo + "','" + fechaPaga + "')");
    }

    public Empleado getEmpleado(int id){
        try {
            ResultSet rs = executeQuery(String.format("SELECT * FROM empleados WHERE id = '%d'", id));
            rs.next();
            return buildEmpleadoByResultset(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Empleado getEmpleado (String nombre, String empresa) {
        try{
            ResultSet rs = executeQuery(String.format("SELECT * FROM empleados WHERE empleado = '%s' AND empresa = '%s'", nombre, empresa));

            while (rs.next()){
                return buildEmpleadoByResultset(rs);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean trabajaEmpresa(String empleado, String nombreEmpresa) {
        List<Empleado> trabajos = this.getTrabajosJugador(empleado);

        return trabajos.stream()
                .anyMatch((empl) -> empl.getEmpresa().equalsIgnoreCase(nombreEmpresa));
    }

    public void setSueldo(int id, double sueldo) {
        try {
            String consulta2 = "UPDATE empleados SET sueldo = ? WHERE id = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setDouble(1, sueldo);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    public void borrarEmplado(int id) {
        executeUpdate("DELETE FROM empleados WHERE id=\"" + id + "\"      ");
    }

    public List<Empleado> getAllEmpleados (){
        List<Empleado> toReturn = new ArrayList<>();
        try{
            ResultSet rs = executeQuery("SELECT * FROM empleados");
            while (rs.next()){
                toReturn.add(buildEmpleadoByResultset(rs));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public List<Empleado> getEmpleadosEmrpesa(String nombreEmpresa){
        List<Empleado> empleados = new ArrayList<>();

        try {
            String consulta = "SELECT * FROM empleados WHERE empresa = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombreEmpresa);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                empleados.add(buildEmpleadoByResultset(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empleados;
    }

    public List<Empleado> getTrabajosJugador(String jugador){
        List<Empleado> empleados = new ArrayList<>();

        try {
            String consulta = "SELECT * FROM empleados WHERE empleado = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                empleados.add(buildEmpleadoByResultset(rs));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return empleados;
    }

    public void cambiarEmpresaNombre(String empresa, String nuevoNombre) {
        List<Empleado> empleados = this.getEmpleadosEmrpesa(empresa);

        for (int i = 0; i < empleados.size(); i++) {
            setEmpresa(empleados.get(i).getId(), nuevoNombre);
        }
    }

    public void despedir(String nombreEmpresa, String empleado, String razon, Player p) {
        Empresas empresasMySQL = new Empresas();
        empresasMySQL.conectar();

        int id_emplado = getEmpleado(empleado, nombreEmpresa).getId();
        borrarEmplado(id_emplado);

        p.sendMessage(ChatColor.GOLD + "Has despedido a: " + empleado);
        Player tp = p.getServer().getPlayer(empleado);

        if (tp != null) {
            tp.sendMessage(ChatColor.RED + "Has sido despedido de " + nombreEmpresa + " razon: " + razon);
            tp.playSound(tp.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
        } else {
            Mensajes mensajesMySQL = new Mensajes();
            mensajesMySQL.nuevoMensaje(empleado, "Has sido despedido de " + nombreEmpresa + " por: " + razon);
        }
        empresasMySQL.desconectar();
    }

    public void irseEmpresa(String nombreEmpresa, Player p) {
        Empresas empresasMySQL = new Empresas();

        Empresa empresaAIRse = empresasMySQL.getEmpresa(nombreEmpresa);
        borrarEmplado(getEmpleado(p.getName(), nombreEmpresa).getId());

        p.sendMessage(ChatColor.GOLD + "Te has ido de: " + nombreEmpresa);
        Player ownerPlayer = p.getServer().getPlayer(empresaAIRse.getOwner());
        if (ownerPlayer != null) {
            ownerPlayer.sendMessage(ChatColor.RED + p.getName() + " Se ha ido de tu empresa: " + nombreEmpresa);
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
        } else {
            Mensajes mensajesMySQL = new Mensajes();
            mensajesMySQL.nuevoMensaje(ownerPlayer.getName(), p.getName() + " se ha ido de tu empresa: " + nombreEmpresa);
        }
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
            tipoSueldo = toStringTipoSueldo(tipoSueldo);

            p.sendMessage(ChatColor.GOLD + "   Empresa: " + empresa + " con un sueldo: " + ChatColor.GREEN + sueldo + " PC/" + tipoSueldo + org.bukkit.ChatColor.GOLD + " con el cargo de: " + cargo + " La ultima paga: " + ultimaPaga);
        }
    }

    public void editarTipoSueldo (Empresa empresa, Empleado empleado, String tipoSueldo) {
        this.setTipo(empleado.getId(), tipoSueldo);

        double sueldo = empleado.getSueldo();
        String tipoString = toStringTipoSueldo(tipoSueldo);
        Player jugadorAEditarPlayer = Bukkit.getPlayer(empleado.getEmpleado());
        Player sender = Bukkit.getPlayer(empresa.getOwner());

        if (jugadorAEditarPlayer != null) {
            jugadorAEditarPlayer.sendMessage(ChatColor.GOLD + sender.getName() + " te ha cambiado el tiempo por por el que cobras el sueldo, ahora cobras " + ChatColor.GREEN + formatea.format(sueldo) + " PC" + ChatColor.GOLD + " por " + tipoString);
            jugadorAEditarPlayer.playSound(jugadorAEditarPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        } else {
            Mensajes men = new Mensajes();
            men.nuevoMensaje(empleado.getEmpleado(), "Se te ha cambiado la frecuencia con la que cobras en la empresa: " + empresa.getNombre() + " " + sueldo + " PC/ " + tipoString);
        }
        sender.sendMessage(ChatColor.GOLD + "Has cambiado el tipo de pagado de sueldo de " + empleado.getEmpleado() + " en la empresa " + empresa.getNombre());
    }

    public void editarEmpleadoSueldo(Empresa empresa, Empleado empleado, double nuevoSueldo) {
        setSueldo(empleado.getId(), nuevoSueldo);

        double sueldoAntes = empleado.getSueldo();
        Player sender = Bukkit.getPlayer(empresa.getOwner());
        Player jugadorAEditar = Bukkit.getPlayer(empleado.getEmpleado());
        sender.sendMessage(ChatColor.GOLD + "Has cambiado el sueldo a " + jugadorAEditar + " a " + ChatColor.GREEN + formatea.format(nuevoSueldo) + " PC" + ChatColor.GOLD + " en la empresa: " + empresa);

        if (jugadorAEditar != null) {
            jugadorAEditar.sendMessage(ChatColor.GOLD + sender.getName() + " te ha cambiado el sueldo de " + empresa.getNombre() + " a " + ChatColor.GREEN + formatea.format(nuevoSueldo) + " PC" + ChatColor.GOLD + " antes tenias: " + ChatColor.GREEN + formatea.format(sueldoAntes) + " PC");
            jugadorAEditar.playSound(jugadorAEditar.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        } else {
            Mensajes mensajesMySQL = new Mensajes();
            mensajesMySQL.nuevoMensaje(empleado.getEmpleado(), "Se te ha cambiado el sueldo de la empresa: " + empresa.getNombre() + " a " + formatea.format(nuevoSueldo) + " PC antes tenias " + sueldoAntes + " PC");
        }
    }

    public void pagarSueldos() {
        Transacciones transaccionesMySQL = new Transacciones();
        Date hoy = formatFehcaDeHoyException();

        List<Empleado> todosLosEmpleados = getAllEmpleados();
        for (Empleado empl : todosLosEmpleados) {
            Date actualEmpl = formatFechaDeLaBaseDatosException(empl.getFechaPaga());
            String tipoSueldo = empl.getTipo();

            long diferenciaDias = Funciones.diferenciaDias(hoy, actualEmpl);

            if ((tipoSueldo.equalsIgnoreCase("d") && diferenciaDias <= 1) || (tipoSueldo.equalsIgnoreCase("s") && diferenciaDias <= 7) ||
                    (tipoSueldo.equalsIgnoreCase("2s") && diferenciaDias <= 14) || (tipoSueldo.equalsIgnoreCase("m") && diferenciaDias <= 30)) {
                continue;
            }

            Mensajes mensajesMySQL = new Mensajes();
            boolean sePago = transaccionesMySQL.pagarSalario(empl.getEmpleado(), empl.getEmpresa(), empl.getSueldo());

            if (sePago) {
                setFechaPaga(empl.getId(), dateFormater.format(hoy));
                mensajesMySQL.nuevoMensaje(empl.getEmpleado(), "Has cobrado " + empl.getSueldo() + " PC de parte de la empresa: " + empl.getEmpresa());
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha pagado " + empl.getSueldo() + " a " + empl.getEmpleado() + " en la empresa: " + empl.getEmpresa());
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "La empresa: " + empl.getEmpresa() + " no ha podido pagar " + empl.getSueldo() + " a " + empl.getEmpleado());
                mensajesMySQL.nuevoMensaje(empl.getEmpleado(), "No has podido cobrar tu sueldo por parte de " + empl.getEmpresa() + " por que no tiene las suficientes pixelcoins");
            }
        }
    }

    public static boolean esUnTipoDeSueldo (String tipo) {
        return tipoSueldos.stream().anyMatch( (ite) -> ite.equalsIgnoreCase(tipo) );
    }

    public static String toStringTipoSueldo(String tipoSueldo) {
        String nombreTipoSueldo = null;
        switch (tipoSueldo) {
            case "d":
                nombreTipoSueldo = "dia";
                break;
            case "s":
                nombreTipoSueldo = "semana";
                break;
            case "2s":
                nombreTipoSueldo = "2 semanas";
                break;
            case "m":
                nombreTipoSueldo = "mes";
                break;
        }
        return nombreTipoSueldo;
    }

    private Date formatFechaDeLaBaseDatosException (String fecha) {
        try {
            return formatFechaDeLaBaseDatos(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date formatFehcaDeHoyException () {
        try {
            return formatFehcaDeHoy();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date formatFechaDeLaBaseDatos (String fecha) throws ParseException {
        return dateFormater.parse(fecha);
    }

    private Date formatFehcaDeHoy () throws ParseException {
        Date hoy = new Date();
        return dateFormater.parse(dateFormater.format(hoy));
    }

    private Empleado buildEmpleadoByResultset (ResultSet rs) throws SQLException {
        return new Empleado( rs.getInt("id"),
                rs.getString("empleado"),
                rs.getString("empresa"),
                rs.getDouble("sueldo"),
                rs.getString("cargo"),
                rs.getString("tipo"),
                rs.getString("fechaPaga"));
    }
}