package es.serversurvival.bolsa.activosinfo.verprecioaccion;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.apiHttp.IEXCloud_API;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivoInfoService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Command(
        value = "bolsa precio",
        isAsync = true,
        args = {"ticker"},
        explanation = "Ver el precio de una accion <ticker> ticker de la accion, solo se pueden empresas americanas"
)
public class PrecioBolsaComandoExecutor extends PixelcoinCommand implements CommandRunnerArgs<PrecioBolsaComando> {
    private final ActivoInfoService activoInfoService;

    public PrecioBolsaComandoExecutor() {
        this.activoInfoService = DependecyContainer.get(ActivoInfoService.class);
    }

    @Override
    public void execute(PrecioBolsaComando comando, CommandSender player) {
        String ticker = comando.getTicker();

        try {
            double precio;
            if (llamadasApiMySQL.estaReg(ticker)) {
                precio = llamadasApiMySQL.getLlamadaAPI(ticker).getPrecio();
            }else {
                precio = IEXCloud_API.getOnlyPrice(ticker);
            }

            player.sendMessage(ChatColor.GOLD + "El precio es: " + ChatColor.GREEN + precio + " $");
        } catch (Exception e) {
            //e.printStackTrace();
            player.sendMessage(ChatColor.DARK_RED + "Ticker: " + ticker + " no encontrado. Para consultarlo /bolsa valores o en es.investing.com. Recuerda que solo se puede acciones que cotizen en EEUU");
        }
    }
}
