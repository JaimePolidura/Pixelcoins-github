package es.serversurvival.bolsa.posicionesabiertas.comprarlargo;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa._shared.application.OrderExecutorProxy;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivoInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenPremarketCommand.*;
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
    private final ComprarLargoUseCase comprarLargoUseCase;
    private final ActivoInfoService activoInfoService;

    public ComprarLargoComandoRunner(){
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.activoInfoService = DependecyContainer.get(ActivoInfoService.class);
        this.comprarLargoUseCase = new ComprarLargoUseCase();
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

        ActivoInfo activoInfo = activoInfoService.getByNombreActivo(ticker, SupportedTipoActivo.ACCIONES);

        if(activoInfo.getPrecio() == -1){
            sender.sendMessage(DARK_RED + "Ticker no encontrado, los tickers se ven en /bolsa valores o en inernet como en es.investing.com. Solo se puede invertir en acciones que cotizen en Estados Unidos");
            return;
        }

        String nombreValor = activoInfo.getNombreActivoLargo();
        double precio = activoInfo.getPrecio();

        if(jugadoresService.getByNombre(sender.getName()).getPixelcoins() < (precio * cantidad)){
            sender.sendMessage(DARK_RED + "No tienes las suficientes pixelcoins para pagar " + cantidad + " " + ticker + " a " + FORMATEA.format(precio) + " $ -> " + FORMATEA.format(precio * cantidad) + " PC");
            return;
        }

        OrderExecutorProxy.execute(of(sender.getName(), ticker, cantidad, TipoAccion.LARGO_COMPRA, null), () -> {
            comprarLargoUseCase.comprarLargo(SupportedTipoActivo.ACCIONES, ticker.toUpperCase(), cantidad, sender.getName());

            Bukkit.broadcastMessage(GOLD + sender.getName() + " ha comprado " + cantidad + " acciones de "
                    + nombreValor + " a " + GREEN + FORMATEA.format(precio) + "PC");

            Funciones.enviarMensajeYSonido(Bukkit.getPlayer(sender.getName()), GOLD + "Has comprado " + FORMATEA.format(cantidad)
                    + " acciones a " + GREEN + FORMATEA.format(precio) + " PC" + GOLD + " que es un total de " + GREEN +
                    FORMATEA.format(precio) + " PC " + GOLD + " comandos: " + AQUA + "/bolsa vender /bolsa cartera", ENTITY_PLAYER_LEVELUP);
        });
    }
}
