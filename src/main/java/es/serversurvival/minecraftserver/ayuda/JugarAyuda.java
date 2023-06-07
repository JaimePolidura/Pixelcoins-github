package es.serversurvival.minecraftserver.ayuda;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "jugar", explanation = "Ver las cosas basicas para jugar al servidor")
public class JugarAyuda implements CommandRunnerNonArgs {
    @Override
    public void execute(Player player) {
        player.sendMessage("          ");
        player.sendMessage(ChatColor.YELLOW + " Para jugar:");
        player.sendMessage("          ");
        player.sendMessage(ChatColor.GOLD + "Para construir una casa la puedes construir donde quieras pero sin que este muy cerca de otra casa de otro nombreAccionista (/ayuda normas) y que no este en el spawn");
        player.sendMessage("          ");
        player.sendMessage(ChatColor.GOLD + "Para proteger tus cofres/puertas/shulker box etc : " + ChatColor.WHITE + "/cprivate " + ChatColor.GOLD + "y click derecho en ellos");
        player.sendMessage(ChatColor.GOLD + "Para quitarles la proteccion: " + ChatColor.WHITE + "/remove " + ChatColor.GOLD + "y click derecho en ellos");
        player.sendMessage(ChatColor.GOLD + "Para ponerles contraseña " + ChatColor.WHITE + "/cpassword <contraseña> " + ChatColor.GOLD + "y click derecho en ellos " +
                ChatColor.GOLD + "y para desbloquarlos " + ChatColor.WHITE + "/cunlock <contraseña> " + ChatColor.GOLD + "y click derecho en ellos");
        player.sendMessage("          ");
        player.sendMessage("/warp " + ChatColor.GOLD + " y selecciones donde quieres teletransportarte");
        player.sendMessage("          ");
        player.sendMessage("/sethome " + ChatColor.GOLD + "Para fijar tu casa. " + ChatColor.WHITE + "/home " + ChatColor.GOLD + "Para ir a tu casa");
        player.sendMessage("          ");
        player.sendMessage("/back " + ChatColor.GOLD + "Para volver a los sitios donde has muerto");
        player.sendMessage("          ");
        player.sendMessage("/perfil " + ChatColor.GOLD + "Para ver todas tus estadisticas");
    }
}
