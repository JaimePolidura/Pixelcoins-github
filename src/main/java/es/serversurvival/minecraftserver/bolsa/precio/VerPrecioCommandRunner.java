package es.serversurvival.minecraftserver.bolsa.precio;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;
import es.serversurvival._shared.utils.Funciones;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "bolsa precio",
        args = {"ticker"},
        isAsync = true
)
@RequiredArgsConstructor
public final class VerPrecioCommandRunner implements CommandRunnerArgs<VerPrecioComando> {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final ActivosBolsaService activosBolsaService;

    @Override
    public void execute(VerPrecioComando comando, Player player) {
        ActivoBolsa activoBolsa = activosBolsaService.getByNombreCortoAndTipoActivo(comando.getTicker(), TipoActivoBolsa.ACCION);
        double ultimoPrecio = activoBolsaUltimosPreciosService.getUltimoPrecio(activoBolsa.getActivoBolsaId(), player.getUniqueId());

        player.sendMessage(GOLD + "El precio de " + activoBolsa.getNombreLargo() + " es " + formatPixelcoins(ultimoPrecio) + "/ Accion");
    }
}
