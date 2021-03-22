package es.serversurvival.comandos.comandos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.menus.menus.TopMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@Command(name = "top")
public class Top extends ComandoUtilidades implements CommandRunner {
    public final static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "              TOP";

    @Override
    public void execute(CommandSender player, String[] args) {
        TopMenu topMenu = new TopMenu((Player) player);
        topMenu.openMenu();
    }
}
