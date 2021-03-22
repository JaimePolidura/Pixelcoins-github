package es.serversurvival.objetos.mySQL;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.serversurvival.objetos.mySQL.tablasObjetos.Deuda;
import es.serversurvival.objetos.mySQL.tablasObjetos.Jugador;
import es.serversurvival.objetos.task.ScoreboardTaskManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


import net.md_5.bungee.api.ChatColor;


public class Deudas extends MySQL {
    private Jugadores jugadoresMySQL = new Jugadores();
    private ScoreboardTaskManager sp = new ScoreboardTaskManager();
    private DecimalFormat formatea = new DecimalFormat("###,###.##");
    private SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");


    public Deuda nuevaDeuda(String deudor, String acredor, int pixelcoins, int tiempo, int interes) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdf.format(dt);
        int cuota = (int) Math.round((double) pixelcoins / tiempo);

        executeUpdate("INSERT INTO deudas (deudor, acredor, pixelcoins, tiempo, interes, cuota, fecha) VALUES ('" + deudor + "','" + acredor + "','" + pixelcoins + "','" + tiempo + "','" + interes + "','" + cuota + "','" + fecha + "')");

        return new Deuda(getMaxDeuda(), deudor, acredor, pixelcoins, tiempo, interes, cuota, fecha);
    }

    public Deuda getDeuda(int id_deuda){
        try{
            ResultSet rs = executeQuery(String.format("SELECT * FROM deudas WHERE id_deuda = '%d'", id_deuda));

            while (rs.next()){
                return buildDeudaByResultset(rs);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public int getMaxDeuda(){
        try{
            ResultSet rs = executeQuery("SELECT id_deuda FROM deudas ORDER BY id_deuda DESC LIMIT 1");

            while (rs.next()){
                return rs.getInt("id_deuda");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return -1;
    }

    public List<Deuda> getDeudasAcredor(String jugador){
        List<Deuda> deudas = new ArrayList<>();
        try{
            ResultSet rs = executeQuery(String.format("SELECT * FROM deudas WHERE acredor = '%s'", jugador));

            while (rs.next()){
                deudas.add(buildDeudaByResultset(rs));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return deudas;
    }

    public List<Deuda> getDeudasDeudor(String jugador){
        List<Deuda> deudas = new ArrayList<>();
        try{
            ResultSet rs = executeQuery(String.format("SELECT * FROM deudas WHERE deudor = '%s'", jugador));

            while (rs.next()){
                deudas.add(buildDeudaByResultset(rs));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return deudas;
    }

    public List<Deuda> getAllDeudas () {
        List<Deuda> toReturn = new ArrayList<>();
        try {
            ResultSet rs = executeQuery("SELECT * FROM deudas");

            while (rs.next()) {
                toReturn.add(buildDeudaByResultset(rs));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    public void setPagoDeuda(int id, int pixelcoins, int tiempo, String fecha) {
        try {
            String consulta2 = "UPDATE deudas SET pixelcoins = ?, tiempo = ?, fecha = ? WHERE id_deuda = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst.setInt(1, pixelcoins);
            pst.setInt(2, tiempo);
            pst.setString(3, fecha);
            pst.setInt(4, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void borrarDeuda(int id) {
        try {
            String consulta = "DELETE FROM deudas WHERE id_deuda = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean estaRegistradaId(int id) {
        return getDeuda(id) != null;
    }

    public boolean esAcredorDeDeuda(int id, String acredor) {
        try {
            String consulta = "SELECT id_deuda FROM deudas WHERE acredor = ? AND id_deuda = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setString(1, acredor);
            pst.setInt(2, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean esDeudorDeDeuda(int id, String deudor) {
        try {
            String consulta = "SELECT id_deuda FROM deudas WHERE deudor = ? AND id_deuda = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setString(1, deudor);
            pst.setInt(2, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void mostarDeudas(Player p) {
        String nombre = p.getName();

        String nombret = "";
        int pixelcoins = 0;
        int dias = 0;
        int cuota = 0;
        int interes = 0;
        int id = 0;

        p.sendMessage(ChatColor.GOLD + "Jugadores que te deben: " + ChatColor.AQUA + "(/estadisticas)");
        List<Deuda> deudasAcredor = this.getDeudasAcredor(nombre);
        for(Deuda deuda : deudasAcredor){
            interes = deuda.getInteres();
            nombret = deuda.getDeudor();
            pixelcoins = deuda.getPixelcoins();
            dias = deuda.getTiempo();
            cuota = deuda.getCouta();
            id = deuda.getId_deuda();

            p.sendMessage("   " + ChatColor.GOLD + nombret + " " + ChatColor.GREEN + pixelcoins + " PC " + ChatColor.GOLD + "(Con " + interes + " interes aplicado) con cuota " + ChatColor.GREEN + cuota + " PC/dia " + ChatColor.GOLD + " (" + dias + " dias) " + ChatColor.DARK_AQUA + "id: " + id);
        }

        p.sendMessage(ChatColor.GOLD + "Jugadores que debes:");
        List<Deuda> deudasDeudor = this.getDeudasDeudor(nombre);
        for(Deuda deuda : deudasDeudor){
            interes = deuda.getInteres();
            nombret = deuda.getAcredor();
            pixelcoins = deuda.getPixelcoins();
            dias = deuda.getTiempo();
            cuota = deuda.getCouta();
            id = deuda.getId_deuda();

            p.sendMessage("   " + ChatColor.GOLD + "Debes a " + nombret + " " + ChatColor.GREEN + pixelcoins + " PC " + ChatColor.GOLD + "(Con " + interes + " interes aplicado) con cuota " + ChatColor.GREEN + cuota + " PC/dia" + ChatColor.GOLD + " (" + dias + " dias) " + ChatColor.DARK_AQUA + "id: " + id);
        }
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
            sp.updateScoreboard(playerDeudor);
        } else {
            Mensajes men = new Mensajes();
            men.nuevoMensaje(nombreDeudor, player.getName() + " te ha cencelado la deuda de " + pixelcoinsDeuda + " PC");
        }

        sp.updateScoreboard(player);
    }

    public void pagarDeuda(Player playeDeudor, int id) {
        Transacciones transaccionesMySQL = new Transacciones();
        Deuda deudaAPagar = getDeuda(id);
        int pixelcoinsDeuda = deudaAPagar.getPixelcoins();
        String acredor = deudaAPagar.getAcredor();

        transaccionesMySQL.realizarTransferencia(playeDeudor.getName(), acredor, pixelcoinsDeuda, " ", Transacciones.TIPO.DEUDAS_PAGAR_TODADEUDA, true);
        borrarDeuda(id);

        playeDeudor.sendMessage(ChatColor.GOLD + "Has pagado a " + acredor + " toda la deuda: " + ChatColor.GREEN + formatea.format(pixelcoinsDeuda) + " PC");
        playeDeudor.playSound(playeDeudor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Player playerAcredor = playeDeudor.getServer().getPlayer(acredor);
        if (playerAcredor != null) {
            playerAcredor.sendMessage(ChatColor.GOLD + playeDeudor.getName() + " ta ha pagado toda la deuda: " + ChatColor.GREEN + formatea.format(pixelcoinsDeuda) + " PC");
            playerAcredor.playSound(playerAcredor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            sp.updateScoreboard(playerAcredor);
        } else {
            Mensajes mensajesAcredor = new Mensajes();
            mensajesAcredor.nuevoMensaje(acredor, playeDeudor.getName() + " ta ha pagado toda la deuda: " + pixelcoinsDeuda + " PC");
        }
        sp.updateScoreboard(playeDeudor);
    }

    private void pagarDeudaYBorrarSiEsNecesario (Deuda deuda, Jugador acredor, Jugador deudor) {
        Transacciones transaccionesMySQL = new Transacciones();
        Mensajes mensajesMySQL = new Mensajes();

        String deudorNombre = deudor.getNombre();
        String acredorNombre = acredor.getNombre();
        int cuota = deuda.getCouta();
        int id = deuda.getId_deuda();
        int tiempo = deuda.getTiempo();
        int pixelcoinsDeuda = deuda.getPixelcoins();

        transaccionesMySQL.realizarTransferencia(deudorNombre, acredorNombre, cuota, "", Transacciones.TIPO.DEUDAS_PAGAR_DEUDAS, true);
        jugadoresMySQL.setNpagos(deudorNombre, deudor.getNpagos() + 1);

        if(tiempo == 1){
            borrarDeuda(id);

            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Borrada deuda en id: " + id);
            mensajesMySQL.nuevoMensaje(deudorNombre, "Has acabado de pagar la deuda con " + acredorNombre);
            mensajesMySQL.nuevoMensaje(acredorNombre, deudorNombre + " ha acabado de pagar la deuda contigo");
        }else{
            setPagoDeuda(id, pixelcoinsDeuda - cuota, tiempo - 1, dateFormater.format(formatFehcaDeHoyException()));

            mensajesMySQL.nuevoMensaje(deudorNombre, "Has pagado " + cuota + " PC por la deuda que tienes con " + acredorNombre + " a " + (tiempo - 1) + " dias");
            mensajesMySQL.nuevoMensaje(acredorNombre, deudorNombre + " te ha pagado " + cuota + " PC por la deuda que tiene a " + (tiempo - 1) + " dias contigo");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha pagado en id: " + id);
        }
    }

    private void sumarUnNinpagoYEnviarMensajeAlAcredor (Jugador acredor, Jugador deudor, int id) {
        Mensajes mensajesMySQL = new Mensajes();

        jugadoresMySQL.setNinpagos(deudor.getNombre(), deudor.getNinpagos() + 1);

        mensajesMySQL.nuevoMensaje(acredor.getNombre(), deudor.getNombre() + " no te ha podido pagar ese dia la deuda por falta de pixelcoins");
        mensajesMySQL.nuevoMensaje(deudor.getNombre(), "no has podido pagar un dia la deuda con " + acredor.getNombre());
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + " No se puede pagar en id: " + id + " por falta de pixelcoins");
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

    private Deuda buildDeudaByResultset(ResultSet rs) throws SQLException {
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