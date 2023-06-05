package es.serversurvival.v2.minecraftserver.ayuda;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "normas", explanation = "Ver todas las normas del servidor")
public class NormasAyuda implements CommandRunnerNonArgs {
    public void execute(Player player) {
        player.sendMessage("          ");
        player.sendMessage(ChatColor.RED + " El incumplimiento de las siguintes normas resultara con un baneo de tiempo: La primera vez que has sido baneado: 1 dia, segunda vez 3 dias y tercera vez 10 dias. Normas:");
        player.sendMessage("          ");
        player.sendMessage(ChatColor.RED + "1º " + ChatColor.GOLD + "Matar continuamente a alguien de manera que le inpida jugar");
        player.sendMessage("          ");
        player.sendMessage(ChatColor.RED + "2º " + ChatColor.GOLD + "no insultar en el chat de manera que ofenda al nombreAccionista (ofendidito xd)");
        player.sendMessage("          ");
        player.sendMessage(ChatColor.RED + "3º" + ChatColor.GOLD + "respetar el territorio de los demas (no expandirse de manera exajerada, no pegar las casas maximo 5 bloques de separacion sin el consentimiento del nombreAccionista)");
        player.sendMessage("          ");
        player.sendMessage(ChatColor.RED + "4º " + ChatColor.GOLD + "no utilizar hacks (xray, kill aura, kurium etc)");
        player.sendMessage("          ");
        player.sendMessage(ChatColor.RED + "5º " + ChatColor.GOLD + "no explotar bugs");
        player.sendMessage("          ");
        player.sendMessage(ChatColor.RED + "Si has hecho algo grave que no entra en estas razones seras igualmente baneado");
        player.sendMessage("          ");
    }
}
