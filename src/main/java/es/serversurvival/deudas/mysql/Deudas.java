package es.serversurvival.deudas.mysql;

import java.sql.*;
import java.util.*;
import java.util.Date;

import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.jugadores.mySQL.Jugadores;
import es.serversurvival.shared.mysql.MySQL;
import es.serversurvival.transacciones.mySQL.TipoTransaccion;
import es.serversurvival.utils.Funciones;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static es.serversurvival.utils.Funciones.*;

// II 325 -> 218 -> 190
public final class Deudas extends MySQL {
    public final static Deudas INSTANCE = new Deudas();
    private Deudas () {}

    public void nuevaDeuda(java.lang.String deudor, java.lang.String acredor, int pixelcoins, int tiempo, int interes) {
        java.lang.String fechaHoy = dateFormater.format(new Date());
        int cuota = (int) Math.round((double) pixelcoins / tiempo);

        executeUpdate("INSERT INTO deudas (deudor, acredor, pixelcoins_restantes, tiempo_restante, interes, cuota, fecha_ultimapaga) VALUES ('" + deudor + "','" + acredor + "','" + pixelcoins + "','" + tiempo + "','" + interes + "','" + cuota + "','" + fechaHoy + "')");
    }

    public Deuda getDeuda(int id){
        return (Deuda) buildObjectFromQuery(java.lang.String.format("SELECT * FROM deudas WHERE id = '%d'", id));
    }

    public List<Deuda> getDeudasAcredor(java.lang.String jugador){
        return buildListFromQuery(java.lang.String.format("SELECT * FROM deudas WHERE acredor = '%s'", jugador));
    }

    public List<Deuda> getDeudasDeudor(java.lang.String jugador){
        return buildListFromQuery(java.lang.String.format("SELECT * FROM deudas WHERE deudor = '%s'", jugador));
    }

    public List<Deuda> getAllDeudas () {
        return buildListFromQuery("SELECT * FROM deudas");
    }

    public void setPagoDeuda(int id, int pixelcoins, int tiempo, java.lang.String fecha) {
        executeUpdate("UPDATE deudas SET pixelcoins_restantes = '"+pixelcoins+"', tiempo_restante = '"+tiempo+"', fecha_ultimapaga = '"+fecha+"' WHERE id = '"+id+"'");
    }

    public void setAcredorDeudor (java.lang.String nombre, java.lang.String nuevoNombre) {
        executeUpdate("UPDATE deudas SET acredor = '"+nuevoNombre+"' WHERE acredor = '"+nombre+"'");
        executeUpdate("UPDATE deudas SET deudor = '"+nuevoNombre+"' WHERE deudor = '"+nombre+"'");
    }

    public void borrarDeuda(int id) {
        executeUpdate(java.lang.String.format("DELETE FROM deudas WHERE id = '%d'", id));
    }

    public boolean esDeudorDeDeuda(int id, java.lang.String deudor) {
        return !isEmptyFromQuery("SELECT * FROM deudas WHERE id = '"+id+"' AND deudor = '"+deudor+"'");
    }

    public int getAllPixelcoinsDeudasAcredor (java.lang.String jugador) {
        return getSumaTotalListInteger( getDeudasAcredor(jugador), Deuda::getPixelcoins_restantes);
    }

    public int getAllPixelcoinsDeudasDeudor (java.lang.String jugador) {
        return getSumaTotalListInteger( getDeudasDeudor(jugador), Deuda::getPixelcoins_restantes);
    }

    public Map<String, List<Deuda>> getAllDeudasAcredorMap () {
        return Funciones.mergeMapList(this.getAllDeudas(), Deuda::getAcredor);
    }

    public Map<String, List<Deuda>> getAllDeudasDeudorMap () {
        return Funciones.mergeMapList(this.getAllDeudas(), Deuda::getDeudor);
    }

    @SneakyThrows
    private Date formatFechaDeLaBaseDatosException (java.lang.String fecha) {
        return dateFormater.parse(fecha);
    }

    @SneakyThrows
    private Date formatFehcaDeHoyException () {
        return dateFormater.parse(dateFormater.format(new Date()));
    }

    @Override
    protected Deuda buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Deuda(
                rs.getInt("id"),
                rs.getString("deudor"),
                rs.getString("acredor"),
                rs.getInt("pixelcoins_restantes"),
                rs.getInt("tiempo_restante"),
                rs.getInt("interes"),
                rs.getInt("cuota"),
                rs.getString("fecha_ultimapaga")
        );
    }
}
