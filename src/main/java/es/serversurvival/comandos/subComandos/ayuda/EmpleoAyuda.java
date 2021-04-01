package es.serversurvival.comandos.subComandos.ayuda;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Command(name = "ayuda empleo")
public class EmpleoAyuda implements CommandRunner {

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("   ");
        sender.sendMessage(ChatColor.YELLOW + "Puedes ser contratado por otra empresa y ser pagado");
        sender.sendMessage("   ");
        sender.sendMessage("/empresas ver " + ChatColor.GOLD + "Ver todas las empresas que han sido creadas");
        sender.sendMessage("   ");
        sender.sendMessage("/empleo irse <empresa> " + ChatColor.GOLD + "Puedes irte de una empresa en la que trabajas");
        sender.sendMessage("   ");
        sender.sendMessage("/empleo misempleos " + ChatColor.GOLD + "Ver todos los trabajos en los que trabajas: sueldo, frecuencia de pago, cargo");
        sender.sendMessage("   ");
        sender.sendMessage("/comprar <empresa> <precio> " + ChatColor.GOLD + "Comprar servicio/producto de una empresa en concreto.");
    }
}
