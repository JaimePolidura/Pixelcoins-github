package es.serversurvival.tienda.vertienda;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "tienda ver",
        explanation = "Ver la tienda de objetos"
)
public class TiendaComandoRunner implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender p) {
        OfertasMenu ofertasMenu = new OfertasMenu((Player) p);
    }
}
