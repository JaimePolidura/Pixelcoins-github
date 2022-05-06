package es.serversurvival.tienda.vertienda;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "tienda ver",
        explanation = "Ver la tienda de objetos"
)
public class TiendaComandoRunner extends PixelcoinCommand implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender p) {
        OfertasMenu ofertasMenu = new OfertasMenu((Player) p);
    }
}
