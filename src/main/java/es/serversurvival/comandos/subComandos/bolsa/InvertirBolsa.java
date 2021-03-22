package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.main.Funciones;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.objetos.apiHttp.IEXCloud_API;
import es.serversurvival.objetos.mySQL.Jugadores;
import es.serversurvival.objetos.mySQL.LlamadasApi;
import es.serversurvival.objetos.mySQL.PosicionesAbiertas;
import es.serversurvival.objetos.mySQL.Transacciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;

public class InvertirBolsa extends BolsaSubCommand {
    private Transacciones transaccionedMySQL = new Transacciones();
    private Jugadores jugadoresMySQL = new Jugadores();
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

    public void execute(Player jugadorPlayer, String[] args) {
        if (args.length != 3) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        if(!Funciones.esInteger(args[2])){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Necesitas introducir un numero de acciones a comprar no texto");
            return;
        }
        int nAccinesAComprar = Integer.parseInt(args[2]);
        if (nAccinesAComprar <= 0) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Debes de meter un numero de acciones a comprar que no sea negativo ni cero");
            return;
        }
        String ticker = args[1];

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            jugadoresMySQL.conectar();
            try {
                jugadorPlayer.sendMessage(ChatColor.RED + "Cargando...");
                precioAccion = IEXCloud_API.getOnlyPrice(ticker);

                if(jugadoresMySQL.getJugador(jugadorPlayer.getName()).getPixelcoin() < (precioAccion * nAccinesAComprar)){
                    jugadorPlayer.sendMessage(net.md_5.bungee.api.ChatColor.DARK_RED + "No tienes las suficientes pixelcoins para pagar " + nAccinesAComprar + " " + ticker + " a " + formatea.format(precioAccion) + " $ -> " + formatea.format(precioAccion * nAccinesAComprar) + " PC");
                    return;
                }

                transaccionedMySQL.comprarUnidadBolsa(PosicionesAbiertas.TIPOS.ACCIONES.toString(), ticker, "acciones", precioAccion, nAccinesAComprar, jugadorPlayer);

            }catch (IOException e) {
                jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Ticker no encontrado, los tickers se ven en /bolsa valores o en inernet como en es.investing.com. Solo se puede invertir en acciones que cotizen en Estados Unidos");
            }catch (Exception e){
                e.printStackTrace();
            }
            jugadoresMySQL.desconectar();
        }, 0L);
    }
}