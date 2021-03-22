package es.serversurvival.objetos.mySQL;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

import es.serversurvival.objetos.mySQL.tablasObjetos.Empleado;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import es.serversurvival.objetos.mySQL.tablasObjetos.Jugador;
import es.serversurvival.objetos.task.ScoreboardTaskManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


import es.serversurvival.main.Funciones;
import net.md_5.bungee.api.ChatColor;

/**
 * 610 -> 300
 */
public class Empresas extends MySQL {
    public static final int CrearEmpresaNombreLonMax = 16;
    public static final int nMaxEmpresas = 5;
    public static final int CrearEmpresaDescLonMax = 200;
    private DecimalFormat formatea = new DecimalFormat("###,###.##");

    public Empresa nuevaEmpresa(String nombreEmpresa, String owner, double pixelcoins, double ingresos, double gastos, String icono, String descripcion) {
        executeUpdate("INSERT INTO empresas (nombre, owner, pixelcoins, ingresos, gastos, icono, descripcion) VALUES ('" + nombreEmpresa + "','" + owner + "','" + pixelcoins + "','" + ingresos + "','" + gastos + "','" + icono + "','" + descripcion + "')");

        return getEmpresa(getMaxId());
    }

    public void borrarEmpresa(String nombreEmpresa) {
        executeUpdate(String.format("DELETE FROM empresas WHERE nombre = '%s'", nombreEmpresa));
    }

    private int getMaxId(){
        try{
            ResultSet rs = executeQuery("SELECT id_empresa FROM empresas ORDER BY id_empresa DESC LIMIT 1");
            while (rs.next()){
                return rs.getInt("id_empresa");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return -1;
    }

    public void setOwner(String nombreEmpresa, String nuevoOwner) {
        try {
            String consulta2 = "UPDATE empresas SET owner = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setString(1, nuevoOwner);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPixelcoins(String nombreEmpresa, double pixelcoins) {
        try {
            String consulta2 = "UPDATE empresas SET pixelcoins = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setDouble(1, pixelcoins);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setIngresos(String nombreEmpresa, double ingresos) {
        try {
            String consulta2 = "UPDATE empresas SET ingresos = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setDouble(1, ingresos);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setGastos(String nombreEmpresa, double gastos) {
        try {
            String consulta2 = "UPDATE empresas SET gastos = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setDouble(1, gastos);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setIcono(String nombreEmpresa, String icono) {
        try {
            String consulta2 = "UPDATE empresas SET icono = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setString(1, icono);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setNombre(String nombreEmpresa, String nuevoNombre) {
        try {
            String consulta2 = "UPDATE empresas SET nombre = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setString(1, nuevoNombre);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDescripcion(String nombreEmpresa, String descripcion) {
        try {
            String consulta2 = "UPDATE empresas SET descripcion = ? WHERE nombre = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setString(1, descripcion);
            pst.setString(2, nombreEmpresa);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Empresa> getEmpresasOwner(String owner) {
        List<Empresa> empresas = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM empresas WHERE owner = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setString(1, owner);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                empresas.add(buildEmpresaByResultset(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empresas;
    }

    public List<Empresa> getTodasEmpresas() {
        List<Empresa> empresas = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM empresas";
            ResultSet rs = conexion.createStatement().executeQuery(consulta);

            while (rs.next()) {
                empresas.add(buildEmpresaByResultset(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empresas;
    }

    public Empresa getEmpresa(String empresa){
        Empresa toReturn = null;
        try{
            String consulta = "SELECT * FROM empresas WHERE nombre = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, empresa);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                return buildEmpresaByResultset(rs);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return toReturn;
    }

    public Empresa getEmpresa(int id_empresa){
        Empresa toReturn = null;
        try{
            String consulta = "SELECT * FROM empresas WHERE id_empresa = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id_empresa);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                return buildEmpresaByResultset(rs);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return toReturn;
    }

    public void crearEmpresa(String nombreEmpresa, Player p, String descripcion) {
        String owner = p.getName();

        nuevaEmpresa(nombreEmpresa, owner, 0, 0, 0, "DIAMOND_PICKAXE", descripcion);

        p.sendMessage(ChatColor.GOLD + "Has creado una empresa! " + ChatColor.GOLD + "comandos utiles: " + ChatColor.AQUA + "/empresas depositar, /empresas contratar, /empresas logotipo, /empresas vertodas, /empresas miempresa /empresas editarnombre m?s: /ayuda empresario o /empresas ayuda");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        p.getServer().broadcastMessage(ChatColor.GOLD + owner + " ha creado una nueva empresa: " + ChatColor.DARK_AQUA + nombreEmpresa);

        ScoreboardTaskManager sp = new ScoreboardTaskManager();
        sp.updateScoreboard(p);
    }

    public void cambiarIcono(String nombreEmpresa, Player p, String icono) {
        this.setIcono(nombreEmpresa, icono);

        p.sendMessage(ChatColor.GOLD + "Has cambiado el logotipo a: " + icono);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public void cambiarNombre(Player p, String empresa, String nuevoNombre) {
        Empleados empleadosMySQL = new Empleados();
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(empresa);

        empleadosMySQL.cambiarEmpresaNombre(empresa, nuevoNombre);
        setNombre(empresa, nuevoNombre);

        p.sendMessage(ChatColor.GOLD + "Has cambiado de nombre a tu empresa!");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Mensajes mensajesMySQL = new Mensajes();
        empleados.forEach( (empl) -> {
            mensajesMySQL.nuevoMensaje(empl.getEmpleado(), "La empresa en la que trabajas: " + empresa + " ha cambiado a de nombre a " + nuevoNombre);
        });

        ScoreboardTaskManager sp = new ScoreboardTaskManager();
        sp.updateScoreboard(p);
    }

    public void verEmpresa(Player p, String nombreEmpresa) {
        Empresa empresa = this.getEmpresa(nombreEmpresa);

        String logitipo = empresa.getIcono();
        String descripcion = empresa.getDescripcion();
        double pixelcoins = empresa.getPixelcoins();
        double ingresos = empresa.getIngresos();
        double gastos = empresa.getGastos();
        double beneficios = ingresos - gastos;
        double rentabilidad = Funciones.rentabilidad(ingresos, beneficios);

        p.sendMessage(ChatColor.GOLD + "        Estadisticas para " + ChatColor.DARK_AQUA + nombreEmpresa);
        p.sendMessage(ChatColor.GOLD + "Pixelcoins en la empresa: " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC");
        p.sendMessage(ChatColor.GOLD + "Ingresos: " + ChatColor.GREEN + formatea.format(ingresos) + " PC");
        p.sendMessage(ChatColor.GOLD + "gastos: " + ChatColor.GREEN + formatea.format(gastos) + " PC");
        if (beneficios < 0) {
            p.sendMessage(ChatColor.GOLD + "beneficios: " + ChatColor.RED + formatea.format(beneficios) + ChatColor.GREEN + " PC");
        } else {
            p.sendMessage(ChatColor.GOLD + "beneficios: " + ChatColor.GREEN + formatea.format(beneficios) + " PC");
        }
        p.sendMessage(ChatColor.GOLD + "logotipo: " + logitipo);
        p.sendMessage(ChatColor.GOLD + "descripcion: " + descripcion);

        if (rentabilidad < 0) {
            p.sendMessage(ChatColor.GOLD + "rentabilidad: " + ChatColor.RED + ((int) rentabilidad) + "%");
        } else {
            p.sendMessage(ChatColor.GOLD + "rentabilidad: " + ((int) rentabilidad) + "%");
        }

        p.sendMessage(ChatColor.GOLD + "Empleados:");
        Empleados empleadosMySQL = new Empleados();
        empleadosMySQL.conectar();

        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(nombreEmpresa);
        empleados.forEach( (empleado) -> {
            p.sendMessage(ChatColor.GOLD + " -" + empleado.getEmpleado() + " cargo: " + empleado.getCargo() + " sueldo: " + ChatColor.GREEN + formatea.format(empleado.getSueldo()) + " PC/ " + Empleados.toStringTipoSueldo(empleado.getTipo()));
        });
    }

    public void cambiarDescripciom(String nombreEmpresa, String nuevaDescripcion, Player p) {
        this.setDescripcion(nombreEmpresa, nuevaDescripcion);

        p.sendMessage(ChatColor.GOLD + "Has cambiado la descripccion de tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa + ChatColor.GOLD + " a ver en " + ChatColor.AQUA + "/empresas vertodas");
    }

    public void borrarEmpresaManual (String empresaNombre) {
        ScoreboardTaskManager scoreboardTaskManager = new ScoreboardTaskManager();
        Transacciones transaccionesMySQL = new Transacciones();
        Mensajes mensajesMySQL = new Mensajes();
        Empleados empleadosMySQL = new Empleados();
        Jugadores jugadoresMySQL = new Jugadores();

        Empresa empresaABorrar = getEmpresa(empresaNombre);
        Jugador ownerJugador = jugadoresMySQL.getJugador(empresaABorrar.getOwner());
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(empresaNombre);

        jugadoresMySQL.setPixelcoin(ownerJugador.getNombre(), ownerJugador.getPixelcoin() + empresaABorrar.getPixelcoins());
        borrarEmpresa(empresaNombre);
        transaccionesMySQL.nuevaTransaccion(ownerJugador.getNombre(), ownerJugador.getNombre(), empresaABorrar.getPixelcoins(), empresaNombre, Transacciones.TIPO.EMPRESA_BORRAR);

        empleados.forEach( (empleado) -> {
            empleadosMySQL.borrarEmplado(empleado.getId());
            Player empleadoPlayer = Bukkit.getPlayer(empleado.getEmpleado());

            if(empleadoPlayer != null){
                empleadoPlayer.sendMessage(org.bukkit.ChatColor.GOLD + ownerJugador.getNombre() + " ha borrado su empresa donde trabajabas: " + empresaNombre);
                scoreboardTaskManager.updateScoreboard(empleadoPlayer);
            }else{
                mensajesMySQL.nuevoMensaje(empleado.getEmpleado(), "El owner de la empresa en la que trabajas: " + empresaNombre + " la ha borrado, ya no existe");
            }
        });
    }

    private Empresa buildEmpresaByResultset (ResultSet rs) throws SQLException {
        return new Empresa( rs.getInt("id_empresa"),
                rs.getString("nombre"),
                rs.getString("owner"),
                rs.getDouble("pixelcoins"),
                rs.getDouble("ingresos"),
                rs.getDouble("gastos"),
                rs.getString("icono"),
                rs.getString("descripcion"));
    }
}