package es.serversurvival.comandos.subComandos.ayuda;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NormasAyudaSubComando extends AyudaSubCommand {
    private final String SCNombre = "normas";
    private final String sintaxis = "/ayuda normas";
    private final String ayuda = "";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player p, String[] args) {
        p.sendMessage("          ");
        p.sendMessage(ChatColor.RED + " El incumplimiento de las siguintes normas resultara con un baneo de tiempo: La primera vez que has sido baneado: 1 dia, segunda vez 3 dias y tercera vez 10 dias. Normas:");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.RED + "1º " + ChatColor.GOLD + "Matar continuamente a alguien de manera que le inpida jugar");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.RED + "2º " + ChatColor.GOLD + "no insultar en el chat de manera que ofenda al jugador (ofendidito xd)");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.RED + "3º" + ChatColor.GOLD + "respetar el territorio de los demas (no expandirse de manera exajerada, no pegar las casas maximo 5 bloques de separacion sin el consentimiento del jugador)");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.RED + "4º " + ChatColor.GOLD + "no utilizar hacks (xray, kill aura, kurium etc)");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.RED + "5º " + ChatColor.GOLD + "no explotar bugs");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.RED + "Si has hecho algo grave que no entra en estas razones seras igualmente baneado");
        p.sendMessage("          ");
    }
}
