package es.serversurvival.bolsa.other.comprarlargo;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.other._shared.AbrirOrdenUseCase;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.jugadores._shared.application.JugadoresService;
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
        args = {"ticker", "cantidad"},
        explanation = "Para comprar una accion. <ticker> ticker de la accion (solo se pueden acciones americanas) <cantidad> cantidad de acciones a comprar"
)
public class ComprarLargoComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<ComprarLargoComando> {
    private final JugadoresService jugadoresService;

    public ComprarLargoComandoRunner(){
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

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

        if(jugadoresService.getByNombre(sender.getName()).getPixelcoins() < (precio * cantidad)){
            sender.sendMessage(DARK_RED + "No tienes las suficientes pixelcoins para pagar " + cantidad + " " + ticker + " a " + formatea.format(precio) + " $ -> " + formatea.format(precio * cantidad) + " PC");
            return;
        }

        if(Funciones.mercadoNoEstaAbierto()) {
            AbrirOrdenUseCase.INSTANCE.abrirOrden(sender.getName(), ticker, cantidad, TipoAccion.LARGO_COMPRA, -1);
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
