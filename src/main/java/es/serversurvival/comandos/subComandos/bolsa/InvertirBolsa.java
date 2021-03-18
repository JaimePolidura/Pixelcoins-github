package es.serversurvival.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.mySQL.*;
import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.util.Funciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(name = "bolsa invertir")
public class InvertirBolsa extends ComandoUtilidades implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /bolsa invertir <ticker Ejmeplo amazon: AMZN> <nAcciones>";
    private boolean done = false;

    public void execute(CommandSender jugadorPlayer, String[] args) {
        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(3, usoIncorrecto))
                .andMayThrowException(() -> args[2], usoIncorrecto, NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            jugadorPlayer.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        int nAccinesAComprar = Integer.parseInt(args[2]);
        String ticker = args[1];

        Funciones.POOL.submit(() -> {
            MySQL.conectar();
            jugadorPlayer.sendMessage(RED + "Cargando...");

            Optional<Double> precioOptional = llamadasApiMySQL.getPrecioAccion(ticker);
            if(!precioOptional.isPresent()){
                jugadorPlayer.sendMessage(DARK_RED + "Ticker no encontrado, los tickers se ven en /bolsa valores o en inernet como en es.investing.com. Solo se puede invertir en acciones que cotizen en Estados Unidos");
                return;
            }

            String nombreValor = llamadasApiMySQL.getNombreActivo(ticker).get();
            double precio = precioOptional.get();

            if(jugadoresMySQL.getJugador(jugadorPlayer.getName()).getPixelcoins() < (precio * nAccinesAComprar)){
                jugadorPlayer.sendMessage(DARK_RED + "No tienes las suficientes pixelcoins para pagar " + nAccinesAComprar + " " + ticker + " a " + formatea.format(precio) + " $ -> " + formatea.format(precio * nAccinesAComprar) + " PC");
                return;
            }
            if(!Funciones.mercadoEstaAbierto()){
                ordenesMySQL.abrirOrdenCompraLargo((Player) jugadorPlayer, ticker, nAccinesAComprar);
                return;
            }

            transaccionesMySQL.comprarUnidadBolsa(TipoActivo.ACCIONES, ticker.toUpperCase(), nombreValor,"acciones", precio, nAccinesAComprar, jugadorPlayer.getName());

            MySQL.desconectar();
        });
    }
}
