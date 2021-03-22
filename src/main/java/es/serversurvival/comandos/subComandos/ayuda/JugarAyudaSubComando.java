package es.serversurvival.comandos.subComandos.ayuda;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class JugarAyudaSubComando extends AyudaSubCommand {
    private String SCNombre = "jugar";
    private String sintaxis = "/ayuda jugar";
    private String ayuda = "";

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
        p.sendMessage(ChatColor.YELLOW + " Para jugar:");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.GOLD + "Para construir una casa la puedes construir donde quieras pero sin que este muy cerca de otra casa de otro jugador (/ayuda normas) y que no este en el spawn");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.GOLD + "Para proteger tus cofres/puertas/shulker box etc : " + ChatColor.WHITE + "/cprivate " + ChatColor.GOLD + "y click derecho en ellos");
        p.sendMessage(ChatColor.GOLD + "Para quitarles la proteccion: " + ChatColor.WHITE + "/remove " + ChatColor.GOLD + "y click derecho en ellos");
        p.sendMessage(ChatColor.GOLD + "Para ponerles contraseña " + ChatColor.WHITE + "/cpassword <contraseña> " + ChatColor.GOLD + "y click derecho en ellos " +
                ChatColor.GOLD + "y para desbloquarlos " + ChatColor.WHITE + "/cunlock <contraseña> " + ChatColor.GOLD + "y click derecho en ellos");
        p.sendMessage("          ");
        p.sendMessage("/warp " + ChatColor.GOLD + " y selecciones donde quieres teletransportarte");
        p.sendMessage("          ");
        p.sendMessage("/sethome " + ChatColor.GOLD + "Para fijar tu casa. " + ChatColor.WHITE + "/home " + ChatColor.GOLD + "Para ir a tu casa");
    }
}
