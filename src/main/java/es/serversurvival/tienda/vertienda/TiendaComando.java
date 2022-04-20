package es.serversurvival.tienda.vertienda;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerNonArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("tienda")
public class TiendaComando extends PixelcoinCommand implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender p) {
        OfertasMenu ofertasMenu = new OfertasMenu((Player) p);
    }
}
