package es.serversurvival.comandos.subComandos.bolsa;

import com.oracle.xmlns.internal.webservices.jaxws_databinding.SoapBindingParameterStyle;
import es.serversurvival.mySQL.*;
import es.serversurvival.mySQL.enums.VALORES;
import es.serversurvival.util.Funciones;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.apiHttp.IEXCloud_API;
import net.minecraft.server.v1_16_R1.Scoreboard;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;

public class InvertirBolsa extends BolsaSubCommand {
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

        File xd = new File("xd");

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
            MySQL.conectar();
            try {
                jugadorPlayer.sendMessage(ChatColor.RED + "Cargando...");

                if(llamadasApiMySQL.estaReg(ticker)){
                    precioAccion = llamadasApiMySQL.getLlamadaAPI(ticker).getPrecio();
                }else {
                    precioAccion = IEXCloud_API.getOnlyPrice(ticker);
                }
                if(jugadoresMySQL.getJugador(jugadorPlayer.getName()).getPixelcoin() < (precioAccion * nAccinesAComprar)){
                    jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No tienes las suficientes pixelcoins para pagar " + nAccinesAComprar + " " + ticker + " a " + formatea.format(precioAccion) + " $ -> " + formatea.format(precioAccion * nAccinesAComprar) + " PC");
                    return;
                }
                
                String nombreValor = IEXCloud_API.getNombreEmpresa(ticker);
                nombreValor = Funciones.quitarCaracteres(nombreValor, '.', ',');
                nombreValor = Funciones.quitarPalabrasEntreEspacios(nombreValor, "group", "inc", "co", "corp");

                transaccionesMySQL.comprarUnidadBolsa(VALORES.ACCIONES.toString(), ticker.toUpperCase(), nombreValor,"acciones", precioAccion, nAccinesAComprar, jugadorPlayer);
                llamadasApiMySQL.actualizarSimbolo(ticker);

            }catch (IOException e) {
                jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Ticker no encontrado, los tickers se ven en /bolsa valores o en inernet como en es.investing.com. Solo se puede invertir en acciones que cotizen en Estados Unidos");
            }catch (Exception e){
                e.printStackTrace();
            }
            MySQL.desconectar();
        }, 0L);
    }
}