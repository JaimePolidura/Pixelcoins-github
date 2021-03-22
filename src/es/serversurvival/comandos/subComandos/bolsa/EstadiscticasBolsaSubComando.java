package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.objetos.mySQL.PosicionesCerradas;
import es.serversurvival.objetos.mySQL.tablasObjetos.PosicionCerrada;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EstadiscticasBolsaSubComando extends BolsaSubCommand {
    private final String SCNombre = "estadisticas";
    private final String sintaxis = "/bolsa estadisticas";
    private final String ayuda = "Ver tus estadisticas de inversion";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        PosicionesCerradas posicionesCerradas = new PosicionesCerradas();
        posicionesCerradas.conectar();

        player.sendMessage(ChatColor.GOLD + "--------------------------------");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "     ESTADISTICAS");

        List<PosicionCerrada> idsMen = posicionesCerradas.getTop3PersonalMenosRentabilidadesID(player.getName());
        List<PosicionCerrada> idsMay = posicionesCerradas.getTop3PersonalRentabilidadesID(player.getName());

        player.sendMessage(ChatColor.GOLD + "Mejores operaciones:");
        int pos = 1;
        int cantidad;
        double apertura;
        double cierre;
        double rentabilidad;

        for (PosicionCerrada posicionCerrada : idsMay) {
            cantidad = posicionCerrada.getCantidad();
            apertura = posicionCerrada.getPrecioApertura();
            cierre = posicionCerrada.getPrecioCierre();
            rentabilidad = posicionCerrada.getRentabilidad();

            if(rentabilidad < 0 ){
                continue;
            }

            player.sendMessage(ChatColor.GOLD + "" + pos + ": " + posicionCerrada.getTipo().toLowerCase()  + " -> " + posicionCerrada.getNombre() + " : " + ChatColor.GREEN + "+" +
                    posicionCerrada.getRentabilidad() + "% -> +" + formatea.format((cantidad * cierre) - (cantidad * apertura)) + " PC "
                    + ChatColor.DARK_AQUA + "ID: " + posicionCerrada.getId());
            pos++;
        }
        player.sendMessage(ChatColor.GOLD + "Peores operaciones:");
        pos = 1;
        for (PosicionCerrada posicionCerrada : idsMen) {
            cantidad = posicionCerrada.getCantidad();
            apertura = posicionCerrada.getPrecioApertura();
            cierre = posicionCerrada.getPrecioCierre();
            rentabilidad = posicionCerrada.getRentabilidad();

            if(rentabilidad > 0){
                continue;
            }

            player.sendMessage(ChatColor.GOLD + "" + pos + ": " + posicionCerrada.getTipo().toLowerCase()  + " -> " + posicionCerrada.getNombre() + " : " + ChatColor.RED +
                    posicionCerrada.getRentabilidad() + "% -> " + formatea.format((cantidad * cierre) - (cantidad * apertura)) + " PC "
                    + ChatColor.DARK_AQUA + "ID:" + posicionCerrada.getId());
            pos++;
        }
        player.sendMessage(ChatColor.GOLD + "Si quieres ver todas las operaciones que has hecho y que tienes: " + ChatColor.AQUA + "/bolsa operacionesCerradas /bolsa cartera");
        player.sendMessage(ChatColor.GOLD + "--------------------------------");
        posicionesCerradas.desconectar();
    }
}