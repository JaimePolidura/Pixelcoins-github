package es.serversurvival.v2.minecraftserver.bolsa.precio;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

@Command("bolsa preico")
@RequiredArgsConstructor
public final class VerPrecioCommandRunner implements CommandRunnerArgs<VerPrecioComando> {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final ActivosBolsaService activosBolsaService;

    @Override
    public void execute(VerPrecioComando comando, Player player) {
        ActivoBolsa activoBolsa = activosBolsaService.getByNombreCortoAndTipoActivo(comando.getTicker(), TipoActivoBolsa.ACCION);
        double ultimoPrecio = activoBolsaUltimosPreciosService.getUltimoPrecio(comando.getTicker(), TipoActivoBolsa.ACCION);

        player.sendMessage(GOLD + "El precio de " + activoBolsa.getNombreLargo() + " es " + GREEN + Funciones.FORMATEA.format(ultimoPrecio) + "PC / Accion");
    }
}
