package es.serversurvival.empresas.editarempleado;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.shared.comandos.PixelcoinCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.*;

@Command("empresas editarempleado")
public class EditarEmpleadoComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas editarempleado <empresa>";

    @Override
    public void execute(CommandSender player, String[] args) {
        if(args.length >= 3)
            player.sendMessage(ChatColor.GOLD + "Para editar empleados: http://serversurvival.ddns.net/profile/empresas/info/" + args[2] +AQUA+" /cuenta" +GOLD+ "para registrarse");
        else
            player.sendMessage(ChatColor.GOLD + "Para editar empleados: http://serversurvival.ddns.net/profile/empresas "+AQUA+" /cuenta" +GOLD+ "para registrarse");
    }
}
