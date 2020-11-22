package es.serversurvival.mySQL;

import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import es.serversurvival.mySQL.tablasObjetos.Deuda;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import es.serversurvival.mySQL.enums.TRANSACCIONES;
import es.serversurvival.task.ScoreBoardManager;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

// II 325 -> 218
public final class Deudas extends MySQL {
    public final static Deudas INSTANCE = new Deudas();
    private Deudas () {}

    public Deuda nuevaDeuda(String deudor, String acredor, int pixelcoins, int tiempo, int interes) {
        String fechaHoy = dateFormater.format(new Date());
        int cuota = (int) Math.round((double) pixelcoins / tiempo);

        executeUpdate("INSERT INTO deudas (deudor, acredor, pixelcoins, tiempo, interes, cuota, fecha) VALUES ('" + deudor + "','" + acredor + "','" + pixelcoins + "','" + tiempo + "','" + interes + "','" + cuota + "','" + fechaHoy + "')");
        return new Deuda(getMaxDeuda(), deudor, acredor, pixelcoins, tiempo, interes, cuota, fechaHoy);
    }

    public Deuda getDeuda(int id_deuda){
        ResultSet rs = executeQuery(String.format("SELECT * FROM deudas WHERE id_deuda = '%d'", id_deuda));

        return (Deuda) buildSingleObjectFromResultSet(rs);
    }

    public int getMaxDeuda(){
        ResultSet rs = executeQuery("SELECT * FROM deudas ORDER BY id_deuda DESC LIMIT 1");

        return ( (Deuda) buildSingleObjectFromResultSet(rs)).getId_deuda();
    }

    public List<Deuda> getDeudasAcredor(String jugador){
        ResultSet rs = executeQuery(String.format("SELECT * FROM deudas WHERE acredor = '%s'", jugador));

        return buildListFromResultSet(rs);
    }

    public List<Deuda> getDeudasDeudor(String jugador){
        ResultSet rs = executeQuery(String.format("SELECT * FROM deudas WHERE deudor = '%s'", jugador));

        return buildListFromResultSet(rs);
    }

    public List<Deuda> getAllDeudas () {
        ResultSet rs = executeQuery("SELECT * FROM deudas");

        return buildListFromResultSet(rs);
    }

    public void setPagoDeuda(int id, int pixelcoins, int tiempo, String fecha) {
        executeUpdate("UPDATE deudas SET pixelcoins = '"+pixelcoins+"', tiempo = '"+tiempo+"', fecha = '"+fecha+"' WHERE id_deuda = '"+id+"'");
    }

    public void setAcredorDeudor (String nombre, String nuevoNombre) {
        executeUpdate("UPDATE deudas SET acredor = '"+nuevoNombre+"' WHERE acredor = '"+nombre+"'");
        executeUpdate("UPDATE deudas SET deudor = '"+nuevoNombre+"' WHERE deudor = '"+nombre+"'");
    }

    public void borrarDeuda(int id) {
        executeUpdate(String.format("DELETE FROM deudas WHERE id_deuda = '%d'", id));
    }

    public boolean estaRegistradaId(int id) {
        return getDeuda(id) != null;
    }

    public boolean esAcredorDeDeuda(int id, String acredor) {
        ResultSet rs = executeQuery("SELECT id_deuda FROM deudas WHERE acredor = '"+acredor+"' AND id_deuda = '"+id+"'");

        return !isEmpty(rs);
    }

    public boolean esDeudorDeDeuda(int id, String deudor) {
        ResultSet rs = executeQuery("SELECT id_deuda FROM deudas WHERE deudor = '"+deudor+"' AND id_deuda = '"+id+"'");

        return !isEmpty(rs);
    }

    public int getAllPixelcoinsDeudasAcredor (String jugador) {
        return Funciones.getSumaTotalListInteger( getDeudasAcredor(jugador), Deuda::getPixelcoins);
    }

    public int getAllPixelcoinsDeudasDeudor (String jugador) {
        return Funciones.getSumaTotalListInteger( getDeudasDeudor(jugador), Deuda::getPixelcoins);
    }

    public Map<String, List<Deuda>> getAllDeudasAcredorMap () {
        List<Deuda> deudas = this.getAllDeudas();
        Map<String, List<Deuda>> mapDeudas = new HashMap<>();

        deudas.forEach(deuda ->{
            if(mapDeudas.get(deuda.getAcredor()) == null){
                List<Deuda> newDeudaList = new ArrayList<>();
                newDeudaList.add(deuda);

                mapDeudas.put(deuda.getAcredor(), newDeudaList);
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
                List<Deuda> newDeudaList = new ArrayList<>();
                newDeudaList.add(deuda);

                mapDeudas.put(deuda.getDeudor(), newDeudaList);
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
            Date fechaUltimaPagaBaseDatos = formatFechaDeLaBaseDatosException(deuda.getFecha());
            Jugador deudor = jugadoresMySQL.getJugador(deuda.getDeudor());
            Jugador acredor = jugadoresMySQL.getJugador(deuda.getAcredor());

            if(fechaHoy.compareTo(fechaUltimaPagaBaseDatos) != 0){
                if(deudor.getPixelcoin() >= deuda.getCouta()){
                    pagarDeudaYBorrarSiEsNecesario(deuda, acredor, deudor);
                }else{
                    sumarUnNinpagoYEnviarMensajeAlAcredor(acredor, deudor, deuda.getId_deuda());
                }
            }
        });
    }

    public void cancelarDeuda(Player player, int id) {
        Deuda deudaACancelar = getDeuda(id);
        borrarDeuda(id);

        String nombreDeudor = deudaACancelar.getDeudor();
        int pixelcoinsDeuda = deudaACancelar.getPixelcoins();
        player.sendMessage(ChatColor.GOLD + "Has cancelado la deuda a " + nombreDeudor + "!");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Player playerDeudor = player.getServer().getPlayer(nombreDeudor);
        if (playerDeudor != null) {
            playerDeudor.sendMessage(ChatColor.GOLD + player.getName() + " te ha cancelado la deuda de " + ChatColor.GREEN + formatea.format(pixelcoinsDeuda) + " PC");
            playerDeudor.playSound(playerDeudor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            ScoreBoardManager.updateScoreboard(playerDeudor);
        } else {
            mensajesMySQL.nuevoMensaje("" ,nombreDeudor, player.getName() + " te ha cencelado la deuda de " + pixelcoinsDeuda + " PC");
        }

        ScoreBoardManager.updateScoreboard(player);
    }

    public void pagarDeuda(Player playeDeudor, int id) {
        Deuda deudaAPagar = getDeuda(id);
        int pixelcoinsDeuda = deudaAPagar.getPixelcoins();
        String acredor = deudaAPagar.getAcredor();

        transaccionesMySQL.realizarTransferencia(playeDeudor.getName(), acredor, pixelcoinsDeuda, " ", TRANSACCIONES.DEUDAS_PAGAR_TODADEUDA, true);
        borrarDeuda(id);

        playeDeudor.sendMessage(ChatColor.GOLD + "Has pagado a " + acredor + " toda la deuda: " + ChatColor.GREEN + formatea.format(pixelcoinsDeuda) + " PC");
        playeDeudor.playSound(playeDeudor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Player playerAcredor = playeDeudor.getServer().getPlayer(acredor);
        if (playerAcredor != null) {
            playerAcredor.sendMessage(ChatColor.GOLD + playeDeudor.getName() + " ta ha pagado toda la deuda: " + ChatColor.GREEN + formatea.format(pixelcoinsDeuda) + " PC");
            playerAcredor.playSound(playerAcredor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            ScoreBoardManager.updateScoreboard(playerAcredor);
        } else {
            mensajesMySQL.nuevoMensaje("", acredor, playeDeudor.getName() + " ta ha pagado toda la deuda: " + pixelcoinsDeuda + " PC");
        }
        ScoreBoardManager.updateScoreboard(playeDeudor);
    }

    private void pagarDeudaYBorrarSiEsNecesario (Deuda deuda, Jugador acredor, Jugador deudor) {
        String deudorNombre = deudor.getNombre();
        String acredorNombre = acredor.getNombre();
        int cuota = deuda.getCouta();
        int id = deuda.getId_deuda();
        int tiempo = deuda.getTiempo();
        int pixelcoinsDeuda = deuda.getPixelcoins();

        transaccionesMySQL.realizarTransferencia(deudorNombre, acredorNombre, cuota, "", TRANSACCIONES.DEUDAS_PAGAR_DEUDAS, true);
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

    private Date formatFechaDeLaBaseDatosException (String fecha) {
        try {
            return dateFormater.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date formatFehcaDeHoyException () {
        try {
            return dateFormater.parse(dateFormater.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Deuda buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Deuda(
                rs.getInt("id_deuda"),
                rs.getString("deudor"),
                rs.getString("acredor"),
                rs.getInt("pixelcoins"),
                rs.getInt("tiempo"),
                rs.getInt("interes"),
                rs.getInt("cuota"),
                rs.getString("fecha")
        );
    }
}
