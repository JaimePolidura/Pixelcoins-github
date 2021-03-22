package es.serversurvival.objetos.mySQL;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.serversurvival.objetos.mySQL.tablasObjetos.Deuda;
import es.serversurvival.objetos.task.ScoreboardTaskManager;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


import net.md_5.bungee.api.ChatColor;

public class Deudas extends MySQL {
    private ScoreboardTaskManager sp = new ScoreboardTaskManager();
    private DecimalFormat formatea = new DecimalFormat("###,###.##");

    public Deuda nuevaDeuda(String deudor, String acredor, int pixelcoins, int tiempo, int interes) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdf.format(dt);
        try {
            int cuota = (int) Math.round((double) pixelcoins / tiempo);

            String consulta = "INSERT INTO deudas (deudor, acredor, pixelcoins, tiempo, interes, cuota, fecha) VALUES ('" + deudor + "','" + acredor + "','" + pixelcoins + "','" + tiempo + "','" + interes + "','" + cuota + "','" + fecha + "')";
            Statement st2 = (Statement) conexion.createStatement();
            st2.executeUpdate(consulta);

            return new Deuda(getMaxDeuda(), deudor, acredor, pixelcoins, tiempo, interes, cuota, fecha);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getMaxDeuda(){
        try{
            String consulta = "SELECT id_deuda FROM deudas ORDER BY id_deuda DESC LIMIT 1";
            ResultSet resultSet = conexion.createStatement().executeQuery(consulta);

            while (resultSet.next()){
                return resultSet.getInt("id_deuda");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return -1;
    }

    public int getTodaDeudaDeudor(String nombre) {
        int deuda = 0;

        try {
            String consulta = "SELECT pixelcoins FROM deudas WHERE deudor = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                deuda = deuda + rs.getInt("pixelcoins");
            }
            rs.close();
        } catch (SQLException e) {

        }
        return deuda;
    }

    public int getTodaDeudaAcredor(String nombre) {
        int deuda = 0;

        try {
            String consulta = "SELECT pixelcoins FROM deudas WHERE acredor = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                deuda = deuda + rs.getInt("pixelcoins");
            }
            rs.close();
        } catch (SQLException e) {

        }
        return deuda;
    }

    public List<Deuda> getDeudasAcredor(String jugador){
        List<Deuda> deudas = new ArrayList<>();
        try{
            String consulta = "SELECT * FROM deudas WHERE acredor = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                deudas.add(new Deuda(
                        rs.getInt("id_deuda"),
                        rs.getString("deudor"),
                        rs.getString("acredor"),
                        rs.getInt("pixelcoins"),
                        rs.getInt("tiempo"),
                        rs.getInt("interes"),
                        rs.getInt("couta"),
                        rs.getString("fecha")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return deudas;
    }

    public List<Deuda> getDeudasDeudor(String jugador){
        List<Deuda> deudas = new ArrayList<>();
        try{
            String consulta = "SELECT * FROM deudas WHERE deudor = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                deudas.add(new Deuda(
                        rs.getInt("id_deuda"),
                        rs.getString("deudor"),
                        rs.getString("acredor"),
                        rs.getInt("pixelcoins"),
                        rs.getInt("tiempo"),
                        rs.getInt("interes"),
                        rs.getInt("couta"),
                        rs.getString("fecha")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return deudas;
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

    public String getDeudor(int id) {
        String deudor = "";
        try {
            String consulta = "SELECT deudor FROM deudas WHERE id_deuda = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                deudor = rs.getString("deudor");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return deudor;
    }

    public String getAcredor(int id) {
        String acredor = "";
        try {
            String consulta = "SELECT acredor FROM deudas WHERE id_deuda = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                acredor = rs.getString("acredor");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return acredor;
    }

    public int getPixelcoins(int id) {
        int pixelcoins = 0;
        try {
            String consulta = "SELECT pixelcoins FROM deudas WHERE id_deuda = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                pixelcoins = rs.getInt("pixelcoins");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return pixelcoins;
    }

    public int getTiempo(int id) {
        int tiempo = 0;
        try {
            String consulta = "SELECT tiempo FROM deudas WHERE id_deuda = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                tiempo = rs.getInt("tiempo");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return tiempo;
    }

    public int getInteres(int id) {
        int interes = 0;
        try {
            String consulta = "SELECT interes FROM deudas WHERE id_deuda = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                interes = rs.getInt("interes");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return interes;
    }

    public int getCuota(int id) {
        int cuota = 0;
        try {
            String consulta = "SELECT cuota FROM deudas WHERE id_deuda = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                cuota = rs.getInt("cuota");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return cuota;
    }

    public String getFecha(int id) {
        String fecha = "";
        try {
            String consulta = "SELECT fecha FROM deudas WHERE id_deuda = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                fecha = rs.getString("fecha");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return fecha;
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

        }
    }

    public void borrarDeuda(int id) {
        try {
            String consulta = "DELETE FROM deudas WHERE id_deuda = ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public boolean estaRegistradaId(int id) {
        boolean reg = false;
        try {
            String consulta = "SELECT id_deuda FROM deudas WHERE id_deuda = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                reg = true;
                rs.close();
            }
        } catch (SQLException e) {

        }
        return reg;
    }

    public boolean esAcredorDeDeuda(int id, String acredor) {
        boolean es = false;

        try {
            String consulta = "SELECT id_deuda FROM deudas WHERE acredor = ? AND id_deuda = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setString(1, acredor);
            pst.setInt(2, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                es = true;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return es;
    }

    public boolean esDeudorDeDeuda(int id, String deudor) {
        boolean es = false;
        try {
            String consulta = "SELECT id_deuda FROM deudas WHERE deudor = ? AND id_deuda = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setString(1, deudor);
            pst.setInt(2, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                es = true;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return es;
    }

    public void pagarDeudas(Server se) {
        int idActual = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String consulta = "SELECT * FROM deudas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);


            while (rs.next()) {
                idActual = rs.getInt("id_deuda");
                String fecha = rs.getString("fecha");
                Date fechaActual = null;
                Date fechaHoy = null;

                String acredor = rs.getString("acredor");
                String deudor = rs.getString("deudor");
                int cuota = rs.getInt("cuota");
                int tiempo = rs.getInt("tiempo");
                int pixelcoins = rs.getInt("pixelcoins");
                int pixelcoinsDeudor = 0;

                //Conseguimos al fecha de hoy a las ponemos al mismo formato que la fechaActual de la base dedatos
                try {
                    Date dt = new Date();

                    fechaActual = sdf.parse(fecha);
                    fechaHoy = sdf.parse(sdf.format(dt));

                } catch (Exception e) {
                    se.getConsoleSender().sendMessage(ChatColor.DARK_RED + " Error al pagar deuda.1 en id: " + idActual);
                    return;
                }
                Jugadores j = new Jugadores();
                pixelcoinsDeudor = (int) j.getDinero(deudor);

                int cuincide = fechaHoy.compareTo(fechaActual);

                //Si no cuincide la fecha de hoy y la de la base de datos quiere decir que hay que pagar la deuda
                if (cuincide != 0) {
                    //Comprobamos que el deudor tenga el dinero suciente para pagar la cuota
                    if (pixelcoinsDeudor >= cuota) {
                        //Realizamos transaccion de dinero
                        Transacciones t = new Transacciones();
                        t.realizarTransferencia(deudor, acredor, cuota, "", Transacciones.TIPO.DEUDAS_PAGAR_DEUDAS, true);

                        //Le añadimos un npago al deudor ya que ha pagado la deuda
                        int npagos = j.getNpagos(deudor) + 1;
                        j.setNpagos(deudor, npagos);

                        se.getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha pagado en id: " + idActual);

                        Mensajes men = new Mensajes();
                        men.nuevoMensaje(deudor, "Has pagado " + cuota + " PC por la deuda que tienes con " + acredor + " a " + (tiempo - 1) + " dias");
                        men.nuevoMensaje(acredor, deudor + " te ha pagado " + cuota + " PC por la deuda que tiene a " + (tiempo - 1) + " dias contigo");

                        //Si solo le queda 1 dia para pagar la deuda borramos la deuda sino no
                        if (tiempo == 1) {
                            this.borrarDeuda(idActual);
                            se.getConsoleSender().sendMessage(ChatColor.GREEN + "Borrada deuda en id: " + idActual);
                            men.nuevoMensaje(deudor, "Has acabado de pagar la deuda con " + acredor);
                            men.nuevoMensaje(acredor, deudor + " ha acabado de pagar la deuda contigo");
                        } else {
                            String fechaHoyS = sdf.format(fechaHoy);
                            this.setPagoDeuda(idActual, pixelcoins - cuota, tiempo - 1, fechaHoyS);
                        }
                    } else {
                        //Al no poder pagar la deuda le a?adimos un inpago
                        int ninpagos = j.getNinpago(deudor) + 1;

                        if (!j.estaRegistrado(deudor)) {
                            j.nuevoJugador(deudor, 0, 0, 0, 0, 0, 0, 1, 0);
                        } else {
                            j.setNinpagos(deudor, ninpagos);
                        }

                        se.getConsoleSender().sendMessage(ChatColor.GREEN + " No se puede pagar en id: " + idActual + " por falta de pixelcoins");
                        Mensajes men = new Mensajes();

                        men.nuevoMensaje(acredor, deudor + " no te ha podido pagar ese dia la deuda por falta de pixelcoins");
                        men.nuevoMensaje(deudor, "no has podido pagar un dia la deuda con " + acredor);
                        return;
                    }
                }
            }
        } catch (SQLException e) {
            se.getConsoleSender().sendMessage(ChatColor.DARK_RED + " Error al pagar en deuda id: " + idActual);
        }
    }

    public void cancelarDeuda(Player p, int id) {
        if (!this.estaRegistradaId(id)) {
            p.sendMessage(ChatColor.DARK_RED + "No hay ninguna id con ese numero, la id se ve en comando /deudas");
            return;
        }

        if (!this.esAcredorDeDeuda(id, p.getName())) {
            p.sendMessage(ChatColor.DARK_RED + "No eres el acredor de esa deuda, las id se ven en /deuda");
            return;
        }

        String deudor = this.getDeudor(id);
        int pixelcoinsDeuda = this.getPixelcoins(id);

        this.borrarDeuda(id);

        p.sendMessage(ChatColor.GOLD + "Has cancelado la deuda a " + deudor + "!");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Player tp = p.getServer().getPlayer(deudor);

        if (tp != null) {
            tp.sendMessage(ChatColor.GOLD + p.getName() + " te ha cancelado la deuda de " + ChatColor.GREEN + formatea.format(pixelcoinsDeuda) + " PC");
            tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            sp.updateScoreboard(tp);
        } else {
            Mensajes men = new Mensajes();
            men.nuevoMensaje(deudor, p.getName() + " te ha cencelado la deuda de " + pixelcoinsDeuda + " PC");
        }

        sp.updateScoreboard(p);
    }

    public void pagarDeuda(Player p, int id) {
        if (!this.estaRegistradaId(id)) {
            p.sendMessage(ChatColor.DARK_RED + "No esta registrada ninguna deuda con esa id, las ids se ven en /deudas");
            return;
        }
        if (!this.esDeudorDeDeuda(id, p.getName())) {
            p.sendMessage(ChatColor.DARK_RED + "No eres deudor de esa deuda, las ids se ven en el comando /deudas");
            return;
        }
        int pixelcoinsDeuda = this.getPixelcoins(id);
        String acredor = this.getAcredor(id);
        Jugadores j = new Jugadores();
        if (j.getDinero(p.getName()) < pixelcoinsDeuda) {
            p.sendMessage(ChatColor.DARK_RED + "No tienes las suficientes pixelcoins para pagar esa deuda, pixelcoins requeridas: " + ChatColor.GREEN + formatea.format(pixelcoinsDeuda) + " PC");
            return;
        }
        Transacciones t = new Transacciones();
        t.realizarTransferencia(p.getName(), acredor, pixelcoinsDeuda, " ", Transacciones.TIPO.DEUDAS_PAGAR_TODADEUDA, true);
        this.borrarDeuda(id);

        p.sendMessage(ChatColor.GOLD + "Has pagado a " + acredor + " toda la deuda: " + ChatColor.GREEN + formatea.format(pixelcoinsDeuda) + " PC");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);


        Player tp = p.getServer().getPlayer(acredor);
        if (tp != null) {
            tp.sendMessage(ChatColor.GOLD + p.getName() + " ta ha pagado toda la deuda: " + ChatColor.GREEN + formatea.format(pixelcoinsDeuda) + " PC");
            tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            sp.updateScoreboard(tp);
        } else {
            Mensajes men = new Mensajes();
            men.nuevoMensaje(acredor, p.getName() + " ta ha pagado toda la deuda: " + pixelcoinsDeuda + " PC");
        }
        sp.updateScoreboard(p);
    }
}