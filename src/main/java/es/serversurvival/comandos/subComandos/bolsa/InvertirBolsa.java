package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.mySQL.*;
import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.util.Funciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Optional;

import static es.serversurvival.validaciones.Validaciones.*;

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
        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(3, mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[2], mensajeUsoIncorrecto(), NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        int nAccinesAComprar = Integer.parseInt(args[2]);
        String ticker = args[1];

        Funciones.POOL.submit(() -> {
            MySQL.conectar();
            jugadorPlayer.sendMessage(ChatColor.RED + "Cargando...");

            Optional<Double> precioOptional = llamadasApiMySQL.getPrecioAccion(ticker);
            if(!precioOptional.isPresent()){
                jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Ticker no encontrado, los tickers se ven en /bolsa valores o en inernet como en es.investing.com. Solo se puede invertir en acciones que cotizen en Estados Unidos");
                return;
            }

            String nombreValor = llamadasApiMySQL.getNombreActivo(ticker).get();
            double precio = precioOptional.get();

            if(jugadoresMySQL.getJugador(jugadorPlayer.getName()).getPixelcoins() < (precioAccion * nAccinesAComprar)){
                jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No tienes las suficientes pixelcoins para pagar " + nAccinesAComprar + " " + ticker + " a " + formatea.format(precioAccion) + " $ -> " + formatea.format(precioAccion * nAccinesAComprar) + " PC");
                return;
            }
            if(!Funciones.mercadoEstaAbierto()){
                ordenesMySQL.abrirOrdenCompraLargo(jugadorPlayer, ticker, nAccinesAComprar);
                return;
            }

            transaccionesMySQL.comprarUnidadBolsa(TipoActivo.ACCIONES, ticker.toUpperCase(), nombreValor,"acciones", precioAccion, nAccinesAComprar, jugadorPlayer.getName());

            MySQL.desconectar();
        });
    }
}
