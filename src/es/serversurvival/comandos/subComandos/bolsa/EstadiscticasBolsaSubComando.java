package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.objetos.mySQL.PosicionesCerradas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

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

        ArrayList<Integer> idsMen = posicionesCerradas.getTop3PersonalMenosRentabilidadesID(player.getName());
        ArrayList<Integer> idsMay = posicionesCerradas.getTop3PersonalRentabilidadesID(player.getName());

        player.sendMessage(ChatColor.GOLD + "Mejores operaciones:");
        int pos = 1;
        int nAcciones;
        double apertura;
        double cierre;

        for (int id : idsMay) {
            nAcciones = posicionesCerradas.getNAcciones(id);
            apertura = posicionesCerradas.getPrecioApertura(id);
            cierre = posicionesCerradas.getPrecioCierre(id);

            player.sendMessage(ChatColor.GOLD + "" + pos + ": " + posicionesCerradas.getTicker(id) + " -> " + ChatColor.GREEN + "+" +
                    posicionesCerradas.getRentabilidad(id) + "% -> +" + formatea.format((nAcciones * cierre) - (nAcciones * apertura)) + " PC "
                    + ChatColor.DARK_AQUA + "ID: " + id);
            pos++;
        }
        player.sendMessage(ChatColor.GOLD + "Peores operaciones:");
        pos = 1;
        for (int id : idsMen) {
            nAcciones = posicionesCerradas.getNAcciones(id);
            apertura = posicionesCerradas.getPrecioApertura(id);
            cierre = posicionesCerradas.getPrecioCierre(id);

            player.sendMessage(ChatColor.GOLD + "" + pos + ": " + posicionesCerradas.getTicker(id) + " -> " + ChatColor.RED +
                    posicionesCerradas.getRentabilidad(id) + "% -> " + formatea.format((nAcciones * cierre) - (nAcciones * apertura)) + " PC "
                    + ChatColor.DARK_AQUA + "ID:" + id);
            pos++;
        }
        player.sendMessage(ChatColor.GOLD + "Si quieres ver todas las operaciones que has hecho y que tienes: " + ChatColor.AQUA + "/bolsa operacionesCerradas /bolsa cartera");
        player.sendMessage(ChatColor.GOLD + "--------------------------------");
        posicionesCerradas.desconectar();
    }
}