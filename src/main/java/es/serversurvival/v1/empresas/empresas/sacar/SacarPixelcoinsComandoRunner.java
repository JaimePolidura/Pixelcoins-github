package es.serversurvival.v1.empresas.empresas.sacar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas sacar",
        args = {"empresa", "pixelcoins"},
        explanation = "Sacar pixelcoins de tu empresa"
)
@AllArgsConstructor
public class SacarPixelcoinsComandoRunner implements CommandRunnerArgs<SacarPixelcoinsComando> {
    private final EnviadorMensajes enviadorMensajes;
    private final SacarPixelcoinsUseCase useCase;

    @Override
    public void execute(SacarPixelcoinsComando comando, CommandSender player) {
        double pixelcoinsASacar = comando.getPixelcoins();
        String empresaNombre = comando.getEmpresa();

        useCase.sacar(player.getName(), empresaNombre, pixelcoinsASacar);

        enviadorMensajes.enviarMensajeYSonido((Player) player, GOLD + "Has sacado " + GREEN + FORMATEA.format(pixelcoinsASacar)
                + " PC" + GOLD + " de tu empresa: " + DARK_AQUA + empresaNombre, Sound.ENTITY_PLAYER_LEVELUP);

    }
}
