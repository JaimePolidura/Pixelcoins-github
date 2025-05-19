package es.serversurvival.minecraftserver.jugadores.pagar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.jugadores.pagar.HacerPagarParametros;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "pagar",
        args = {"destino", "pixelcoins"},
        explanation = "Pagar a un nombreAccionista online con tus pixelcoins"
)
@AllArgsConstructor
public class PagarComandoRunner implements CommandRunnerArgs<PagarComando> {
    private final UseCaseBus useCaseBus;

    @Override
    public void execute(PagarComando comando, Player sender) {
        useCaseBus.handle(HacerPagarParametros.of(
                sender.getUniqueId(),
                comando.getDestino().getUniqueId(),
                comando.getPixelcoins()
        ));
    }
}
