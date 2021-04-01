package es.serversurvival.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.apiHttp.IEXCloud_API;
import es.serversurvival.mySQL.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Command(name = "bolsa precio")
public class PrecioBolsa extends PixelcoinCommand implements CommandRunner {
    @Override
    public void execute(CommandSender player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /bolsa precio <ticker>");
            return;
        }
        String ticker = args[1];
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            MySQL.conectar();
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
            MySQL.desconectar();
        }, 0L);
    }
}
