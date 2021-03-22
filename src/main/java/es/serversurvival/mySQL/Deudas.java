package es.serversurvival.mySQL;

import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

import es.serversurvival.mySQL.tablasObjetos.Deuda;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import es.serversurvival.mySQL.enums.TipoTransaccion;
import es.serversurvival.task.ScoreBoardManager;
import es.serversurvival.util.Funciones;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static es.serversurvival.util.Funciones.*;

// II 325 -> 218
public final class Deudas extends MySQL {
    public final static Deudas INSTANCE = new Deudas();
    private Deudas () {}

    public void nuevaDeuda(String deudor, String acredor, int pixelcoins, int tiempo, int interes) {
        String fechaHoy = dateFormater.format(new Date());
        int cuota = (int) Math.round((double) pixelcoins / tiempo);

        executeUpdate("INSERT INTO deudas (deudor, acredor, pixelcoins_restantes, tiempo_restante, interes, cuota, fecha_ultimapaga) VALUES ('" + deudor + "','" + acredor + "','" + pixelcoins + "','" + tiempo + "','" + interes + "','" + cuota + "','" + fechaHoy + "')");
    }

    public Deuda getDeuda(int id){
        return (Deuda) buildObjectFromQuery(String.format("SELECT * FROM deudas WHERE id = '%d'", id));
    }

    public List<Deuda> getDeudasAcredor(String jugador){
        return buildListFromQuery(String.format("SELECT * FROM deudas WHERE acredor = '%s'", jugador));
    }

    public List<Deuda> getDeudasDeudor(String jugador){
        return buildListFromQuery(String.format("SELECT * FROM deudas WHERE deudor = '%s'", jugador));
    }

    public List<Deuda> getAllDeudas () {
        return buildListFromQuery("SELECT * FROM deudas");
    }

    public void setPagoDeuda(int id, int pixelcoins, int tiempo, String fecha) {
        executeUpdate("UPDATE deudas SET pixelcoins_restantes = '"+pixelcoins+"', tiempo_restante = '"+tiempo+"', fecha_ultimapaga = '"+fecha+"' WHERE id = '"+id+"'");
    }

    public void setAcredorDeudor (String nombre, String nuevoNombre) {
        executeUpdate("UPDATE deudas SET acredor = '"+nuevoNombre+"' WHERE acredor = '"+nombre+"'");
        executeUpdate("UPDATE deudas SET deudor = '"+nuevoNombre+"' WHERE deudor = '"+nombre+"'");
    }

    public void borrarDeuda(int id) {
        executeUpdate(String.format("DELETE FROM deudas WHERE id = '%d'", id));
    }

    public boolean esDeudorDeDeuda(int id, String deudor) {
        return !isEmptyFromQuery("SELECT * FROM deudas WHERE id = '"+id+"' AND deudor = '"+deudor+"'");
    }

    public int getAllPixelcoinsDeudasAcredor (String jugador) {
        return getSumaTotalListInteger( getDeudasAcredor(jugador), Deuda::getPixelcoins_restantes);
    }

    public int getAllPixelcoinsDeudasDeudor (String jugador) {
        return getSumaTotalListInteger( getDeudasDeudor(jugador), Deuda::getPixelcoins_restantes);
    }

    public Map<String, List<Deuda>> getAllDeudasAcredorMap () {
        List<Deuda> deudas = this.getAllDeudas();
        Map<String, List<Deuda>> mapDeudas = new HashMap<>();

        deudas.forEach(deuda ->{
            if(mapDeudas.get(deuda.getAcredor()) == null){
                mapDeudas.put(deuda.getAcredor(), listOf(deuda));
            }else{
                List<Deuda> deudasList = mapDeudas.get(deuda.getAcredor());
                deudasList.add(deuda);

                mapDeudas.replace(deuda.getAcredor(), deudasList);
            }
        });

        return mapDeudas;
    }

    public Map<String, List<Deuda>> getAllDeudasDeudorMap () {
        List<Deuda> deudas = this.getAllDeudas();
        Map<String, List<Deuda>> mapDeudas = new HashMap<>();

        deudas.forEach(deuda ->{
            if(mapDeudas.get(deuda.getDeudor()) == null){
                mapDeudas.put(deuda.getDeudor(), listOf(deuda));
            }else{
                List<Deuda> deudasList = mapDeudas.get(deuda.getDeudor());
                deudasList.add(deuda);

                mapDeudas.replace(deuda.getDeudor(), deudasList);
            }
        });

        return mapDeudas;
    }

    public void pagarDeudas () {
        List<Deuda> todasLasDeudas = this.getAllDeudas();

        todasLasDeudas.forEach( (deuda) -> {
            Date fechaHoy = formatFehcaDeHoyException();
            Date fechaUltimaPagaBaseDatos = formatFechaDeLaBaseDatosException(deuda.getFecha_ultimapaga());
            Jugador deudor = jugadoresMySQL.getJugador(deuda.getDeudor());
            Jugador acredor = jugadoresMySQL.getJugador(deuda.getAcredor());

            boolean esElDiaDeLaPaga = fechaHoy.compareTo(fechaUltimaPagaBaseDatos) != 0;
            if(esElDiaDeLaPaga){
                if(deudor.getPixelcoins() >= deuda.getCouta()){
                    pagarDeudaYBorrarSiEsNecesario(deuda, acredor, deudor);
                }else{
                    sumarUnNinpagoYEnviarMensajeAlAcredor(acredor, deudor, deuda.getId());
                }
            }
        });
    }

    private void pagarDeudaYBorrarSiEsNecesario (Deuda deuda, Jugador acredor, Jugador deudor) {
        String deudorNombre = deudor.getNombre();
        String acredorNombre = acredor.getNombre();
        int cuota = deuda.getCouta();
        int id = deuda.getId();
        int tiempo = deuda.getTiempo_restante();
        int pixelcoinsDeuda = deuda.getPixelcoins_restantes();

        transaccionesMySQL.realizarTransferenciaConEstadisticas(deudorNombre, acredorNombre, cuota, "", TipoTransaccion.DEUDAS_PAGAR_DEUDAS);
        jugadoresMySQL.setNpagos(deudorNombre, deudor.getNpagos() + 1);

        if(tiempo == 1){
            borrarDeuda(id);

            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Borrada deuda en id: " + id);
            mensajesMySQL.nuevoMensaje("",deudorNombre, "Has acabado de pagar la deuda con " + acredorNombre);
            mensajesMySQL.nuevoMensaje("", acredorNombre, deudorNombre + " ha acabado de pagar la deuda contigo");
        }else{
            setPagoDeuda(id, pixelcoinsDeuda - cuota, tiempo - 1, dateFormater.format(formatFehcaDeHoyException()));

            mensajesMySQL.nuevoMensaje("", deudorNombre, "Has pagado " + cuota + " PC por la deuda que tienes con " + acredorNombre + " a " + (tiempo - 1) + " dias");
            mensajesMySQL.nuevoMensaje(deudorNombre, acredorNombre, deudorNombre + " te ha pagado " + cuota + " PC por la deuda que tiene a " + (tiempo - 1) + " dias contigo");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha pagado en id: " + id);
        }
    }

    private void sumarUnNinpagoYEnviarMensajeAlAcredor (Jugador acredor, Jugador deudor, int id) {
        jugadoresMySQL.setNinpagos(deudor.getNombre(), deudor.getNinpagos() + 1);

        mensajesMySQL.nuevoMensaje("", acredor.getNombre(), deudor.getNombre() + " no te ha podido pagar ese dia la deuda por falta de pixelcoins");
        mensajesMySQL.nuevoMensaje("", deudor.getNombre(), "no has podido pagar un dia la deuda con " + acredor.getNombre());
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + " No se puede pagar en id: " + id + " por falta de pixelcoins");
    }

    public void cancelarDeuda(Player player, int id) {
        Deuda deudaACancelar = getDeuda(id);
        String nombreDeudor = deudaACancelar.getDeudor();
        int pixelcoinsDeuda = deudaACancelar.getPixelcoins_restantes();
        borrarDeuda(id);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Has cancelado la deuda a " + nombreDeudor + "!", Sound.ENTITY_PLAYER_LEVELUP);

        String mensajeOnline = ChatColor.GOLD + player.getName() + " te ha cancelado la deuda de " + ChatColor.GREEN + formatea.format(pixelcoinsDeuda) + " PC";
        String mensajeOffline = player.getName() + " te ha cencelado la deuda de " + pixelcoinsDeuda + " PC";
        enviarMensaje(nombreDeudor, mensajeOnline, mensajeOnline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        ScoreBoardManager.getInstance().updateScoreboard(player, Bukkit.getPlayer(nombreDeudor));
    }

    public void pagarDeuda(Player playeDeudor, int id) {
        Deuda deudaAPagar = getDeuda(id);
        int pixelcoinsDeuda = deudaAPagar.getPixelcoins_restantes();
        String acredor = deudaAPagar.getAcredor();

        transaccionesMySQL.realizarTransferenciaConEstadisticas(playeDeudor.getName(), acredor, pixelcoinsDeuda, " ", TipoTransaccion.DEUDAS_PAGAR_TODADEUDA);
        borrarDeuda(id);

        enviarMensajeYSonido(playeDeudor, ChatColor.GOLD + "Has pagado a " + acredor + " toda la deuda: " + ChatColor.GREEN + formatea.format(pixelcoinsDeuda) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

        String mensajeOnline = ChatColor.GOLD + playeDeudor.getName() + " ta ha pagado toda la deuda: " + ChatColor.GREEN + formatea.format(pixelcoinsDeuda) + " PC";
        String mensajeOffline = playeDeudor.getName() + " ta ha pagado toda la deuda: " + pixelcoinsDeuda + " PC";
        enviarMensaje(acredor, mensajeOnline, mensajeOffline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        ScoreBoardManager.getInstance().updateScoreboard(playeDeudor, Bukkit.getPlayer(acredor));
    }

    @SneakyThrows
    private Date formatFechaDeLaBaseDatosException (String fecha) {
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
