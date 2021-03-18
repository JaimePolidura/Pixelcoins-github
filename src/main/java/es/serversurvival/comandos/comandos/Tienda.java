package es.serversurvival.comandos.comandos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.menus.menus.OfertasMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "tienda")
public class Tienda extends ComandoUtilidades implements CommandRunner {
    @Override
    public void execute(CommandSender p, String[] args){
        OfertasMenu ofertasMenu = new OfertasMenu((Player) p);
        ofertasMenu.openMenu();
    }
}
