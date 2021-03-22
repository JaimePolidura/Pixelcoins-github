package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.main.Pixelcoin;
import es.serversurvival.objetos.apiHttp.IEXCloud_API;
import es.serversurvival.objetos.mySQL.Transacciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class InvertirBolsaSubComando extends BolsaSubCommand {
    private final String SCNombre = "invertir";
    private final String sintaxis = "/bolsa invertir <ticker Ejmeplo amazon: AMZN> <nAcciones>";
    private final String ayuda = "Invertir en acciones con un ticker (Nombre identificatorio de una accion de una empresa, ver en es.investing.com o en /bolsa valores) y las acciones a comprar";
    private double precioAccion;
    private boolean done = false;

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
        if (args.length != 3) {
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }

        int nAcciones;
        try {
            nAcciones = Integer.parseInt(args[2]);
        } catch (Exception e) {
            player.sendMessage(ChatColor.DARK_RED + "Necesitas introducir un numero de acciones a comprar no texto");
            return;
        }

        if (nAcciones <= 0) {
            player.sendMessage(ChatColor.DARK_RED + "Debes de meter un numero de acciones a comprar que no sea negativo ni cero");
            return;
        }
        String ticker = args[1];

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            try {
                player.sendMessage(ChatColor.RED + "Cargando...");
                precioAccion = IEXCloud_API.getOnlyPrice(ticker);
                Transacciones t = new Transacciones();
                t.comprarAccion(ticker, precioAccion, nAcciones, player);
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage(ChatColor.DARK_RED + "Ticker no encontrado, los tickers se ven en /bolsa valores o en inernet como en es.investing.com. Solo se puede invertir en acciones que cotizen en Estados Unidos");
            }
        }, 0L);
    }
}