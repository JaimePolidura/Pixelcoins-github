package es.serversurvival.mySQL;

import java.sql.*;
import java.util.*;

import es.serversurvival.mySQL.tablasObjetos.Empleado;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import es.serversurvival.mySQL.enums.TRANSACCIONES;
import es.serversurvival.task.ScoreBoardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


import es.serversurvival.util.Funciones;

/**
 * I 610 -> 300
 * II 323 -> 197
 */
public final class Empresas extends MySQL {
    public final static Empresas INSTANCE = new Empresas();
    private Empresas () {}

    public static final int CrearEmpresaNombreLonMax = 16;
    public static final int nMaxEmpresas = 5;
    public static final int CrearEmpresaDescLonMax = 200;

    public Empresa nuevaEmpresa(String nombreEmpresa, String owner, double pixelcoins, double ingresos, double gastos, String icono, String descripcion) {
        executeUpdate("INSERT INTO empresas (nombre, owner, pixelcoins, ingresos, gastos, icono, descripcion) VALUES ('" + nombreEmpresa + "','" + owner + "','" + pixelcoins + "','" + ingresos + "','" + gastos + "','" + icono + "','" + descripcion + "')");

        return getEmpresa(getMaxId());
    }

    private int getMaxId(){
        ResultSet rs = executeQuery("SELECT * FROM empresas ORDER BY id_empresa DESC LIMIT 1");
        Empresa cuenta = (Empresa) buildSingleObjectFromResultSet(rs);

        return cuenta != null ? cuenta.getId_empresa() : -1;
    }

    public Empresa getEmpresa(String empresa){
        ResultSet rs = executeQuery("SELECT * FROM empresas WHERE nombre = '"+empresa+"'");

        return (Empresa) buildSingleObjectFromResultSet(rs);
    }

    public Empresa getEmpresa(int id_empresa){
        ResultSet rs = executeQuery("SELECT * FROM empresas WHERE id_empresa = '"+id_empresa+"'");

        return (Empresa) buildSingleObjectFromResultSet(rs);
    }

    public void borrarEmpresa(String nombreEmpresa) {
        executeUpdate(String.format("DELETE FROM empresas WHERE nombre = '%s'", nombreEmpresa));
    }


    public void setOwner(String nombreEmpresa, String nuevoOwner) {
        executeUpdate("UPDATE empresas SET owner = '"+nuevoOwner+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setTodosOwner (String owner, String nuevoOwner) {
        executeUpdate("UPDATE empresas SET owner = '"+nuevoOwner+"' WHERE owner = '"+owner+"'");
    }

    public void setPixelcoins(String nombreEmpresa, double pixelcoins) {
        executeUpdate("UPDATE empresas SET pixelcoins = '"+pixelcoins+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setIngresos(String nombreEmpresa, double ingresos) {
        executeUpdate("UPDATE empresas SET ingresos = '"+ingresos+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setGastos(String nombreEmpresa, double gastos) {
        executeUpdate("UPDATE empresas SET gastos = '"+gastos+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setIcono(String nombreEmpresa, String icono) {
        executeUpdate("UPDATE empresas SET icono = '"+icono+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setNombre(String nombreEmpresa, String nuevoNombre) {
        executeUpdate("UPDATE empresas SET nombre = '"+nuevoNombre+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setDescripcion(String nombreEmpresa, String descripcion) {
        executeUpdate("UPDATE empresas SET descripcion = '"+descripcion+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public List<Empresa> getEmpresasOwner(String owner) {
        ResultSet rs = executeQuery("SELECT * FROM empresas WHERE owner = '"+owner+"'");

        return buildListFromResultSet(rs);
    }

    public List<Empresa> getTodasEmpresas() {
        ResultSet rs = executeQuery("SELECT * FROM empresas");

        return buildListFromResultSet(rs);
    }

    public double getAllPixelcoinsEnEmpresas (String jugador){
        return Funciones.getSumaTotalListDouble( getEmpresasOwner(jugador), Empresa::getPixelcoins );
    }

    public Map<String, List<Empresa>> getAllEmpresasJugadorMap () {
        List<Empresa> empresas = this.getTodasEmpresas();
        Map<String, List<Empresa>> mapEmpresas = new HashMap<>();

        empresas.forEach(empresa -> {
            if(mapEmpresas.get(empresa.getOwner()) == null){
                List<Empresa> empresasList = new ArrayList<>();
                empresasList.add(empresa);

                mapEmpresas.put(empresa.getOwner(), empresasList);
            }else{
                List<Empresa> empresasList = mapEmpresas.get(empresa.getOwner());
                empresasList.add(empresa);

                mapEmpresas.replace(empresa.getOwner(), empresasList);
            }
        });

        return mapEmpresas;
    }

    public void crearEmpresa(String nombreEmpresa, Player player, String descripcion) {
        String owner = player.getName();

        nuevaEmpresa(nombreEmpresa, owner, 0, 0, 0, "DIAMOND_PICKAXE", descripcion);

        player.sendMessage(ChatColor.GOLD + "Has creado una empresa! " + ChatColor.GOLD + "comandos utiles: " + ChatColor.AQUA + "/empresas depositar, /empresas contratar, /empresas logotipo, /empresas vertodas, /empresas miempresa /empresas editarnombre m?s: /ayuda empresario o /empresas ayuda");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        player.getServer().broadcastMessage(ChatColor.GOLD + owner + " ha creado una nueva empresa: " + ChatColor.DARK_AQUA + nombreEmpresa);

        ScoreBoardManager.updateScoreboard(player);
    }

    public void cambiarIcono(String nombreEmpresa, Player player, String icono) {
        this.setIcono(nombreEmpresa, icono);

        player.sendMessage(ChatColor.GOLD + "Has cambiado el logotipo a: " + icono);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public void cambiarNombre(Player player, String empresa, String nuevoNombre) {
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(empresa);

        empleadosMySQL.cambiarEmpresaNombre(empresa, nuevoNombre);
        setNombre(empresa, nuevoNombre);

        player.sendMessage(ChatColor.GOLD + "Has cambiado de nombre a tu empresa!");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        empleados.forEach( (empl) -> {
            mensajesMySQL.nuevoMensaje("", empl.getEmpleado(), "La empresa en la que trabajas: " + empresa + " ha cambiado a de nombre a " + nuevoNombre);
        });

        ScoreBoardManager.updateScoreboard(player);
    }

    public void cambiarDescripciom(String nombreEmpresa, String nuevaDescripcion, Player player) {
        this.setDescripcion(nombreEmpresa, nuevaDescripcion);

        player.sendMessage(ChatColor.GOLD + "Has cambiado la descripccion de tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa + ChatColor.GOLD + " a ver en " + ChatColor.AQUA + "/empresas vertodas");
    }

    public void borrarEmpresaManual (String empresaNombre) {
        Empresa empresaABorrar = getEmpresa(empresaNombre);
        Jugador ownerJugador = jugadoresMySQL.getJugador(empresaABorrar.getOwner());
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(empresaNombre);

        jugadoresMySQL.setPixelcoin(ownerJugador.getNombre(), ownerJugador.getPixelcoin() + empresaABorrar.getPixelcoins());
        borrarEmpresa(empresaNombre);
        transaccionesMySQL.nuevaTransaccion(ownerJugador.getNombre(), ownerJugador.getNombre(), empresaABorrar.getPixelcoins(), empresaNombre, TRANSACCIONES.EMPRESA_BORRAR);

        empleados.forEach( (empleado) -> {
            empleadosMySQL.borrarEmplado(empleado.getId());
            Player empleadoPlayer = Bukkit.getPlayer(empleado.getEmpleado());

            if(empleadoPlayer != null){
                empleadoPlayer.sendMessage(org.bukkit.ChatColor.GOLD + ownerJugador.getNombre() + " ha borrado su empresa donde trabajabas: " + empresaNombre);
                ScoreBoardManager.updateScoreboard(empleadoPlayer);
            }else{
                mensajesMySQL.nuevoMensaje("" , empleado.getEmpleado(), "El owner de la empresa en la que trabajas: " + empresaNombre + " la ha borrado, ya no existe");
            }
        });
    }

    public void solicitarServicio (Player quienSolicita, String nombreEmpresa) {
        Empresa empresa = getEmpresa(nombreEmpresa);

        Player owner = Bukkit.getPlayer(empresa.getOwner());
        if(owner != null){
            owner.sendMessage(ChatColor.GOLD + quienSolicita.getName() + " te ha solicitado el servicio de tu empresa: " + nombreEmpresa);
            owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        }else{
            mensajesMySQL.nuevoMensaje("" ,empresa.getOwner(), quienSolicita.getName() + " te ha solicitado el servicio de tu empresa: " + nombreEmpresa);
        }

        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(nombreEmpresa);
        empleados.forEach( (empleado) -> {
            Player empleadoPlayer = Bukkit.getPlayer(empleado.getEmpleado());
            if(empleadoPlayer != null){
                empleadoPlayer.sendMessage(ChatColor.GOLD + quienSolicita.getName() + " te ha solicitado el servicio de la empresa: " + nombreEmpresa);
                empleadoPlayer.playSound(empleadoPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            }
        });

        quienSolicita.sendMessage(ChatColor.GOLD + "Has solicitado el servicio");
        quienSolicita.playSound(quienSolicita.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    @Override
    protected Empresa buildObjectFromResultSet(ResultSet rs) throws SQLException {
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
