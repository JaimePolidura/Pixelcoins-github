package es.serversurvival.comandos.subComandos.ayuda;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EmpleoAyuda extends AyudaSubCommand {
    private final String SCNombre = "empleo";
    private final String sintaxis = "/ayuda empleo";
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
        p.sendMessage("   ");
        p.sendMessage(ChatColor.YELLOW + "Puedes ser contratado por otra empresa y ser pagado");
        p.sendMessage("   ");
        p.sendMessage("/empresas ver " + ChatColor.GOLD + "Ver todas las empresas que han sido creadas");
        p.sendMessage("   ");
        p.sendMessage("/empleo irse <empresa> " + ChatColor.GOLD + "Puedes irte de una empresa en la que trabajas");
        p.sendMessage("   ");
        p.sendMessage("/empleo misempleos " + ChatColor.GOLD + "Ver todos los trabajos en los que trabajas: sueldo, frecuencia de pago, cargo");
        p.sendMessage("   ");
        p.sendMessage("/comprar <empresa> <precio> " + ChatColor.GOLD + "Comprar servicio/producto de una empresa en concreto.");
    }
}
