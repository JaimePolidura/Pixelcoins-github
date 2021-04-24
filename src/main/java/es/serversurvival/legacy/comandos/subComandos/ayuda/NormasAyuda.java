package es.serversurvival.legacy.comandos.subComandos.ayuda;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "ayuda normas")
public class NormasAyuda implements CommandRunner {
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("          ");
        sender.sendMessage(ChatColor.RED + " El incumplimiento de las siguintes normas resultara con un baneo de tiempo: La primera vez que has sido baneado: 1 dia, segunda vez 3 dias y tercera vez 10 dias. Normas:");
        sender.sendMessage("          ");
        sender.sendMessage(ChatColor.RED + "1º " + ChatColor.GOLD + "Matar continuamente a alguien de manera que le inpida jugar");
        sender.sendMessage("          ");
        sender.sendMessage(ChatColor.RED + "2º " + ChatColor.GOLD + "no insultar en el chat de manera que ofenda al jugador (ofendidito xd)");
        sender.sendMessage("          ");
        sender.sendMessage(ChatColor.RED + "3º" + ChatColor.GOLD + "respetar el territorio de los demas (no expandirse de manera exajerada, no pegar las casas maximo 5 bloques de separacion sin el consentimiento del jugador)");
        sender.sendMessage("          ");
        sender.sendMessage(ChatColor.RED + "4º " + ChatColor.GOLD + "no utilizar hacks (xray, kill aura, kurium etc)");
        sender.sendMessage("          ");
        sender.sendMessage(ChatColor.RED + "5º " + ChatColor.GOLD + "no explotar bugs");
        sender.sendMessage("          ");
        sender.sendMessage(ChatColor.RED + "Si has hecho algo grave que no entra en estas razones seras igualmente baneado");
        sender.sendMessage("          ");
    }
}
