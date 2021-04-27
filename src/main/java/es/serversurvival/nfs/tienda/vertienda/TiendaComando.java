package es.serversurvival.nfs.tienda.vertienda;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.nfs.shared.comandos.PixelcoinCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("tienda")
public class TiendaComando extends PixelcoinCommand implements CommandRunner {
    @Override
    public void execute(CommandSender p, String[] args){
        OfertasMenu ofertasMenu = new OfertasMenu((Player) p);
    }
}
