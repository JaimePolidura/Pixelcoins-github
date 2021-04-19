package es.serversurvival.mySQL;

import java.sql.*;
import java.util.*;

import es.serversurvival.main.Pixelcoin;
import es.serversurvival.mySQL.eventos.TransaccionEvent;
import es.serversurvival.mySQL.tablasObjetos.Empleado;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import es.serversurvival.mySQL.enums.TipoTransaccion;
import es.serversurvival.task.ScoreBoardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


import es.serversurvival.util.Funciones;

import static es.serversurvival.mySQL.enums.TipoTransaccion.*;
import static es.serversurvival.mySQL.enums.TipoTransaccion.EMPRESA_DIVIDENDO_ACCION;
import static es.serversurvival.util.Funciones.*;

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

    public void nuevaEmpresa(String nombreEmpresa, String owner, double pixelcoins, double ingresos, double gastos, String icono, String descripcion) {
        executeUpdate("INSERT INTO empresas (nombre, owner, pixelcoins, ingresos, gastos, icono, descripcion, cotizada) VALUES ('" + nombreEmpresa + "','" + owner + "','" + pixelcoins + "','" + ingresos + "','" + gastos + "','" + icono + "','" + descripcion + "', 0)");
    }

    public Empresa getEmpresa(String empresa){
        return (Empresa) buildObjectFromQuery("SELECT * FROM empresas WHERE nombre = '"+empresa+"'");
    }

    public Empresa getEmpresa(int id){
        return (Empresa) buildObjectFromQuery("SELECT * FROM empresas WHERE id = '"+id+"'");
    }

    public List<Empresa> getEmpresasOwner(String owner) {
        return buildListFromQuery("SELECT * FROM empresas WHERE owner = '"+owner+"'");
    }

    public List<Empresa> getTodasEmpresas() {
        return buildListFromQuery("SELECT * FROM empresas");
    }

    public boolean esCotizada (String empresa) {
        return !isEmptyFromQuery("SELECT * FROM empresas WHERE nombre = '"+empresa+"' AND cotizada = 1");
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

    public void setCotizada (String empresaNombre) {
        executeUpdate("UPDATE empresas SET cotizada = 1 WHERE nombre = '"+empresaNombre+"'");
    }

    public void borrarEmpresa(String nombreEmpresa) {
        executeUpdate(String.format("DELETE FROM empresas WHERE nombre = '%s'", nombreEmpresa));
    }

    public double getAllPixelcoinsEnEmpresas (String jugador){
        return getSumaTotalListDouble( getEmpresasOwner(jugador), Empresa::getPixelcoins );
    }

    public Map<String, List<Empresa>> getAllEmpresasJugadorMap () {
        return Funciones.mergeMapList(this.getTodasEmpresas(), Empresa::getOwner);
    }

    public void crearEmpresa(String nombreEmpresa, Player player, String descripcion) {
        String owner = player.getName();

        nuevaEmpresa(nombreEmpresa, owner, 0, 0, 0, "DIAMOND_PICKAXE", descripcion);

        player.sendMessage(ChatColor.GOLD + "Has creado una empresa! " + ChatColor.GOLD + "comandos utiles: " + ChatColor.AQUA + "/empresas depositar, /empresas contratar, /empresas logotipo, /empresas vertodas, /empresas miempresa /empresas editarnombre m?s: /ayuda empresario o /empresas ayuda");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        player.getServer().broadcastMessage(ChatColor.GOLD + owner + " ha creado una nueva empresa: " + ChatColor.DARK_AQUA + nombreEmpresa);
    }

    public void cambiarIcono(String nombreEmpresa, Player player, String icono) {
        this.setIcono(nombreEmpresa, icono);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Has cambiado el logotipo a: " + icono, Sound.ENTITY_PLAYER_LEVELUP);
    }

    public void cambiarNombre(Player player, String empresa, String nuevoNombre) {
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(empresa);

        setNombre(empresa, nuevoNombre);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Has cambiado de nombre a tu empresa!", Sound.ENTITY_PLAYER_LEVELUP);

        empleados.forEach( (empl) -> {
            mensajesMySQL.nuevoMensaje("", empl.getJugador(), "La empresa en la que trabajas: " + empresa + " ha cambiado a de nombre a " + nuevoNombre);
        });
    }

    public void cambiarDescripciom(String nombreEmpresa, String nuevaDescripcion, Player player) {
        this.setDescripcion(nombreEmpresa, nuevaDescripcion);

        player.sendMessage(ChatColor.GOLD + "Has cambiado la descripccion de tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa + ChatColor.GOLD + " a ver en " + ChatColor.AQUA + "/empresas vertodas");
    }

    public void borrarEmpresaManual (String empresaNombre) {
        Empresa empresaABorrar = getEmpresa(empresaNombre);
        Jugador ownerJugador = jugadoresMySQL.getJugador(empresaABorrar.getOwner());
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(empresaNombre);

        jugadoresMySQL.setPixelcoin(ownerJugador.getNombre(), ownerJugador.getPixelcoins() + empresaABorrar.getPixelcoins());
        borrarEmpresa(empresaNombre);

        Pixelcoin.publish(new TransaccionEvent(ownerJugador.getNombre(), ownerJugador.getNombre(), empresaABorrar.getPixelcoins(), empresaNombre, EMPRESA_BORRAR));

        empleados.forEach( (empleado) -> {
            String mensajeOnline = ChatColor.GOLD + ownerJugador.getNombre() + " ha borrado su empresa donde trabajabas: " + empresaNombre;
            String mensajeOffline = "El owner de la empresa en la que trabajas: " + empresaNombre + " la ha borrado, ya no existe";

            enviarMensaje(empleado.getJugador(), mensajeOnline, mensajeOffline);
        });
    }

    public void solicitarServicio (Player quienSolicita, String nombreEmpresa) {
        Empresa empresa = getEmpresa(nombreEmpresa);

        String mensajeOnline = ChatColor.GOLD + quienSolicita.getName() + " te ha solicitado el servicio de tu empresa: " + nombreEmpresa;
        String mensajeOffline = quienSolicita.getName() + " te ha solicitado el servicio de tu empresa: " + nombreEmpresa;

        enviarMensaje(empresa.getOwner(), mensajeOnline, mensajeOffline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(nombreEmpresa);
        empleados.forEach( empleado -> {
            enviarMensajeYSonidoSiOnline(empleado.getJugador(), ChatColor.GOLD + quienSolicita.getName() +
                    " te ha solicitado el servicio de la empresa: " + nombreEmpresa, Sound.ENTITY_PLAYER_LEVELUP);
        });

        enviarMensajeYSonido(quienSolicita, ChatColor.GOLD + "Has solicitado el servicio", Sound.ENTITY_PLAYER_LEVELUP);
    }

    @Override
    protected Empresa buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Empresa( rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("owner"),
                rs.getDouble("pixelcoins"),
                rs.getDouble("ingresos"),
                rs.getDouble("gastos"),
                rs.getString("icono"),
                rs.getString("descripcion"),
                rs.getBoolean("cotizada")
        );
    }
}
