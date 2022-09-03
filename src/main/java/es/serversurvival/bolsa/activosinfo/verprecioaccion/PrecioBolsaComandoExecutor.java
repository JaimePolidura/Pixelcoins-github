package es.serversurvival.bolsa.activosinfo.verprecioaccion;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.*;

@Command(
        value = "bolsa precio",
        isAsync = true,
        args = {"ticker"},
        explanation = "Ver el precio de una accion <ticker> ticker de la accion, solo se pueden empresas americanas"
)
@AllArgsConstructor
public class PrecioBolsaComandoExecutor implements CommandRunnerArgs<PrecioBolsaComando> {
    private final ActivosInfoService activoInfoService;

    @Override
    public void execute(PrecioBolsaComando comando, CommandSender player) {
        String ticker = comando.getTicker();

        player.sendMessage(RED + "Cargando...");

        var activoInfo = activoInfoService.getByNombreActivo(ticker, TipoActivo.ACCIONES);

        if(activoInfo.getPrecio() == -1){
            player.sendMessage(DARK_RED + "Ticker: " + ticker + " no encontrado. Para consultarlo /bolsa valores o en es.investing.com. " +
                    "Recuerda que solo se puede cantidad que cotizen en EEUU");
        }else{
            player.sendMessage(GOLD + "El precio de " + activoInfo.getNombreActivoLargo() + " es: " + GREEN +
                    activoInfo.getPrecio() + " $");
        }
    }
}
