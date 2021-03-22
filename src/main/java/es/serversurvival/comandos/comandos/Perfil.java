package es.serversurvival.comandos.comandos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.menus.menus.PerfilMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "perfil")
public class Perfil extends ComandoUtilidades implements CommandRunner {

    @Override
    public void execute(CommandSender sender, String[] args) {
        PerfilMenu perfilMenu = new PerfilMenu((Player) sender);
        perfilMenu.openMenu();
    }
}
