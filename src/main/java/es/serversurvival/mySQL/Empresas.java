package es.serversurvival.mySQL;

import java.sql.*;
import java.util.*;

import es.serversurvival.main.Pixelcoin;
import es.serversurvival.mySQL.eventos.empresas.EmpresaBorradaEvento;
import es.serversurvival.mySQL.tablasObjetos.Empleado;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


import es.serversurvival.util.Funciones;

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

    public void nuevaEmpresa(java.lang.String nombreEmpresa, java.lang.String owner, double pixelcoins, double ingresos, double gastos, java.lang.String icono, java.lang.String descripcion) {
        executeUpdate("INSERT INTO empresas (nombre, owner, pixelcoins, ingresos, gastos, icono, descripcion, cotizada) VALUES ('" + nombreEmpresa + "','" + owner + "','" + pixelcoins + "','" + ingresos + "','" + gastos + "','" + icono + "','" + descripcion + "', 0)");
    }

    public Empresa getEmpresa(java.lang.String empresa){
        return (Empresa) buildObjectFromQuery("SELECT * FROM empresas WHERE nombre = '"+empresa+"'");
    }

    public Empresa getEmpresa(int id){
        return (Empresa) buildObjectFromQuery("SELECT * FROM empresas WHERE id = '"+id+"'");
    }

    public List<Empresa> getEmpresasOwner(java.lang.String owner) {
        return buildListFromQuery("SELECT * FROM empresas WHERE owner = '"+owner+"'");
    }

    public List<Empresa> getTodasEmpresas() {
        return buildListFromQuery("SELECT * FROM empresas");
    }

    public boolean esCotizada (java.lang.String empresa) {
        return !isEmptyFromQuery("SELECT * FROM empresas WHERE nombre = '"+empresa+"' AND cotizada = 1");
    }

    public void setOwner(java.lang.String nombreEmpresa, java.lang.String nuevoOwner) {
        executeUpdate("UPDATE empresas SET owner = '"+nuevoOwner+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setTodosOwner (java.lang.String owner, java.lang.String nuevoOwner) {
        executeUpdate("UPDATE empresas SET owner = '"+nuevoOwner+"' WHERE owner = '"+owner+"'");
    }

    public void setPixelcoins(java.lang.String nombreEmpresa, double pixelcoins) {
        executeUpdate("UPDATE empresas SET pixelcoins = '"+pixelcoins+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setIngresos(java.lang.String nombreEmpresa, double ingresos) {
        executeUpdate("UPDATE empresas SET ingresos = '"+ingresos+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setGastos(java.lang.String nombreEmpresa, double gastos) {
        executeUpdate("UPDATE empresas SET gastos = '"+gastos+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setIcono(java.lang.String nombreEmpresa, java.lang.String icono) {
        executeUpdate("UPDATE empresas SET icono = '"+icono+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setNombre(java.lang.String nombreEmpresa, java.lang.String nuevoNombre) {
        executeUpdate("UPDATE empresas SET nombre = '"+nuevoNombre+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setDescripcion(java.lang.String nombreEmpresa, java.lang.String descripcion) {
        executeUpdate("UPDATE empresas SET descripcion = '"+descripcion+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setCotizada (java.lang.String empresaNombre) {
        executeUpdate("UPDATE empresas SET cotizada = 1 WHERE nombre = '"+empresaNombre+"'");
    }

    public void borrarEmpresa(java.lang.String nombreEmpresa) {
        executeUpdate(java.lang.String.format("DELETE FROM empresas WHERE nombre = '%s'", nombreEmpresa));
    }

    public double getAllPixelcoinsEnEmpresas (java.lang.String jugador){
        return getSumaTotalListDouble( getEmpresasOwner(jugador), Empresa::getPixelcoins );
    }

    public Map<java.lang.String, List<Empresa>> getAllEmpresasJugadorMap () {
        return Funciones.mergeMapList(this.getTodasEmpresas(), Empresa::getOwner);
    }

    public void crearEmpresa(java.lang.String nombreEmpresa, Player player, java.lang.String descripcion) {
        java.lang.String owner = player.getName();

        nuevaEmpresa(nombreEmpresa, owner, 0, 0, 0, "DIAMOND_PICKAXE", descripcion);

        player.sendMessage(ChatColor.GOLD + "Has creado una empresa! " + ChatColor.GOLD + "comandos utiles: " + ChatColor.AQUA + "/empresas depositar, /empresas contratar, /empresas logotipo, /empresas vertodas, /empresas miempresa /empresas editarnombre m?s: /ayuda empresario o /empresas ayuda");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        player.getServer().broadcastMessage(ChatColor.GOLD + owner + " ha creado una nueva empresa: " + ChatColor.DARK_AQUA + nombreEmpresa);
    }

    public void cambiarIcono(java.lang.String nombreEmpresa, Player player, java.lang.String icono) {
        this.setIcono(nombreEmpresa, icono);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Has cambiado el logotipo a: " + icono, Sound.ENTITY_PLAYER_LEVELUP);
    }

    public void cambiarNombre(Player player, java.lang.String empresa, java.lang.String nuevoNombre) {
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(empresa);

        setNombre(empresa, nuevoNombre);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Has cambiado de nombre a tu empresa!", Sound.ENTITY_PLAYER_LEVELUP);

        empleados.forEach( (empl) -> {
            mensajesMySQL.nuevoMensaje("", empl.getJugador(), "La empresa en la que trabajas: " + empresa + " ha cambiado a de nombre a " + nuevoNombre);
        });
    }

    public void cambiarDescripciom(java.lang.String nombreEmpresa, java.lang.String nuevaDescripcion, Player player) {
        this.setDescripcion(nombreEmpresa, nuevaDescripcion);

        player.sendMessage(ChatColor.GOLD + "Has cambiado la descripccion de tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa + ChatColor.GOLD + " a ver en " + ChatColor.AQUA + "/empresas vertodas");
    }

    public void borrarEmpresaManual (java.lang.String empresaNombre) {
        Empresa empresaABorrar = getEmpresa(empresaNombre);
        Jugador ownerJugador = jugadoresMySQL.getJugador(empresaABorrar.getOwner());
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(empresaNombre);

        jugadoresMySQL.setPixelcoin(ownerJugador.getNombre(), ownerJugador.getPixelcoins() + empresaABorrar.getPixelcoins());
        borrarEmpresa(empresaNombre);

        Pixelcoin.publish(new EmpresaBorradaEvento(ownerJugador.getNombre(), empresaNombre, empresaABorrar.getPixelcoins()));

        empleados.forEach( (empleado) -> {
            String mensajeOnline = ChatColor.GOLD + ownerJugador.getNombre() + " ha borrado su empresa donde trabajabas: " + empresaNombre;
            String mensajeOffline = "El owner de la empresa en la que trabajas: " + empresaNombre + " la ha borrado, ya no existe";

            enviarMensaje(empleado.getJugador(), mensajeOnline, mensajeOffline);
        });
    }

    public void solicitarServicio (Player quienSolicita, java.lang.String nombreEmpresa) {
        Empresa empresa = getEmpresa(nombreEmpresa);

        java.lang.String mensajeOnline = ChatColor.GOLD + quienSolicita.getName() + " te ha solicitado el servicio de tu empresa: " + nombreEmpresa;
        java.lang.String mensajeOffline = quienSolicita.getName() + " te ha solicitado el servicio de tu empresa: " + nombreEmpresa;

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
