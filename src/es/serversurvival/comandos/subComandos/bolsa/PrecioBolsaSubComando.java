package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.main.Pixelcoin;
import es.serversurvival.objetos.apiHttp.IEXCloud_API;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PrecioBolsaSubComando extends BolsaSubCommand {
    private final String SCNombre = "precio";
    private final String sintaxis = "/bolsa precio <ticker>";
    private final String ayuda = "Consultar precio de una accion con un ticker (Texto identificatorio de una accion ejemplo amazon. AMZN. Estas se ven es es.investing.com " +
            "o en el comando de minecraft: /bolsa valores que muestra las estadisticas de las principales acciones amricanas )";

    public String getSCNombre() {
        return SCNombre;
    }


    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        String ticker = args[1];

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            try {
                double precio = IEXCloud_API.getOnlyPrice(ticker);

                player.sendMessage(ChatColor.GOLD + "El precio es: " + ChatColor.GREEN + precio + " $");
            } catch (Exception e) {
                player.sendMessage(ChatColor.DARK_RED + "Ticker: " + ticker + " no encontrado. Para consultarlo /bolsa valores o en es.investing.com. Recuerda que solo se puede acciones que cotizen en EEUU");
            }
        }, 0L);
    }
}
