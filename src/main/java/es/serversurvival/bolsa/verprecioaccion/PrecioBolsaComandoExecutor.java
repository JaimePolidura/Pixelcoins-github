package es.serversurvival.bolsa.verprecioaccion;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.apiHttp.IEXCloud_API;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Command(
        value = "bolsa precio",
        isAsync = true,
        isSubCommand = true,
        args = {"ticker"}
)
public class PrecioBolsaComandoExecutor extends PixelcoinCommand implements CommandRunnerArgs<PrecioBolsaComando> {
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
