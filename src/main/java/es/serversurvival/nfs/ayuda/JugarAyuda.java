package es.serversurvival.nfs.ayuda;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("ayuda jugar")
public class JugarAyuda implements CommandRunner {
    @Override
    public void execute(CommandSender sneder, String[] args) {
        sneder.sendMessage("          ");
        sneder.sendMessage(ChatColor.YELLOW + " Para jugar:");
        sneder.sendMessage("          ");
        sneder.sendMessage(ChatColor.GOLD + "Para construir una casa la puedes construir donde quieras pero sin que este muy cerca de otra casa de otro jugador (/ayuda normas) y que no este en el spawn");
        sneder.sendMessage("          ");
        sneder.sendMessage(ChatColor.GOLD + "Para proteger tus cofres/puertas/shulker box etc : " + ChatColor.WHITE + "/cprivate " + ChatColor.GOLD + "y click derecho en ellos");
        sneder.sendMessage(ChatColor.GOLD + "Para quitarles la proteccion: " + ChatColor.WHITE + "/remove " + ChatColor.GOLD + "y click derecho en ellos");
        sneder.sendMessage(ChatColor.GOLD + "Para ponerles contraseña " + ChatColor.WHITE + "/cpassword <contraseña> " + ChatColor.GOLD + "y click derecho en ellos " +
                ChatColor.GOLD + "y para desbloquarlos " + ChatColor.WHITE + "/cunlock <contraseña> " + ChatColor.GOLD + "y click derecho en ellos");
        sneder.sendMessage("          ");
        sneder.sendMessage("/warp " + ChatColor.GOLD + " y selecciones donde quieres teletransportarte");
        sneder.sendMessage("          ");
        sneder.sendMessage("/sethome " + ChatColor.GOLD + "Para fijar tu casa. " + ChatColor.WHITE + "/home " + ChatColor.GOLD + "Para ir a tu casa");
        sneder.sendMessage("          ");
        sneder.sendMessage("/back " + ChatColor.GOLD + "Para volver a los sitios donde has muerto");
        sneder.sendMessage("          ");
        sneder.sendMessage("/perfil " + ChatColor.GOLD + "Para ver todas tus estadisticas");
    }
}
