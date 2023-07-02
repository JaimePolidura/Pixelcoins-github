package es.serversurvival.minecraftserver.mensajes;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.mensajes._shared.domain.Mensaje;
import es.serversurvival.pixelcoins.mensajes.verpendientes.LeectorMensajesService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.List;

import static org.bukkit.ChatColor.*;

@Command(
        value = "mensajes",
        explanation = "Ver los mensajes pendientes"
)
@RequiredArgsConstructor
public final class VerMensajesPendientes implements CommandRunnerNonArgs {
    private final LeectorMensajesService leectorMensajesService;

    @Override
    public void execute(Player player) {
        List<Mensaje> mensajes = leectorMensajesService.verPendientes(player.getUniqueId());

        if(mensajes.size() == 0){
            player.sendMessage(DARK_AQUA + "No tienes mensajes pendientes");
            return;
        }

        player.sendMessage(DARK_AQUA + "Los mensajes pendientes ("+mensajes.size()+"): ");
        mensajes.forEach(mensaje -> {
            player.sendMessage(DARK_AQUA + " " + Funciones.toString(mensaje.getFechaEnvio()) + " " + mensaje.getMensaje());
        });
    }
}
