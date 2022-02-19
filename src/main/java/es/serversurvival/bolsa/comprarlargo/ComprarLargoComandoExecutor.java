package es.serversurvival.bolsa.comprarlargo;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerArgs;
import es.serversurvival.bolsa._shared.AbrirOrdenUseCase;
import es.serversurvival.bolsa._shared.ordenespremarket.mysql.AccionOrden;
import es.serversurvival.bolsa._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival._shared.utils.validaciones.Validaciones;
import main.Pair;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Optional;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.*;

@Command(
        value = "bolsa invertir",
        isAsync = true,
        isSubCommand = true,
        args = {"ticker", "cantidad"}
)
public class ComprarLargoComandoExecutor extends PixelcoinCommand implements CommandRunnerArgs<ComprarLargoComando> {
    @Override
    public void execute(ComprarLargoComando comando, CommandSender sender) {
        String ticker = comando.getTicker();
        int cantidad = comando.getCantidad();

        ValidationResult result = ValidatorService
                .startValidating(cantidad, NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            sender.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        sender.sendMessage(RED + "Cargando...");

        Optional<Pair<String, Double>> valorOpcional = llamadasApiMySQL.getPairNombreValorPrecio(ticker);

        if(!valorOpcional.isPresent()){
            sender.sendMessage(DARK_RED + "Ticker no encontrado, los tickers se ven en /bolsa valores o en inernet como en es.investing.com. Solo se puede invertir en acciones que cotizen en Estados Unidos");
            return;
        }

        String nombreValor = valorOpcional.get().getKey();
        double precio = valorOpcional.get().getValue();

        if(jugadoresMySQL.getJugador(sender.getName()).getPixelcoins() < (precio * cantidad)){
            sender.sendMessage(DARK_RED + "No tienes las suficientes pixelcoins para pagar " + cantidad + " " + ticker + " a " + formatea.format(precio) + " $ -> " + formatea.format(precio * cantidad) + " PC");
            return;
        }

        if(Funciones.mercadoNoEstaAbierto()) {
            AbrirOrdenUseCase.INSTANCE.abrirOrden(sender.getName(), ticker, cantidad, AccionOrden.LARGO_COMPRA, -1);
        } else {
            ComprarLargoUseCase.INSTANCE.abrir(TipoActivo.ACCIONES, ticker.toUpperCase(), nombreValor, "acciones", precio, cantidad, sender.getName());

            Bukkit.broadcastMessage(GOLD + sender.getName() + " ha comprado " + cantidad + " acciones de "
                    + nombreValor + " a " + GREEN + formatea.format(precio) + "PC");

            Funciones.enviarMensajeYSonido(Bukkit.getPlayer(sender.getName()), GOLD + "Has comprado " + formatea.format(cantidad)
                    + " acciones a " + GREEN + formatea.format(precio) + " PC" + GOLD + " que es un total de " + GREEN +
                    formatea.format(precio) + " PC " + GOLD + " comandos: " + AQUA + "/bolsa vender /bolsa cartera", ENTITY_PLAYER_LEVELUP);
        }
    }
}
