package es.serversurvival.bolsa.posicionesabiertas.comprarlargo;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrderExecutorProxy;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
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
        explanation = "Para comprar una accion. <ticker> ticker de la accion (solo se pueden cantidad americanas) <cantidad> cantidad de cantidad a comprar"
)
public class ComprarLargoComandoRunner implements CommandRunnerArgs<ComprarLargoComando> {
    private final JugadoresService jugadoresService;
    private final ComprarLargoUseCase comprarLargoUseCase;
    private final ActivosInfoService activoInfoService;

    public ComprarLargoComandoRunner(){
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.activoInfoService = DependecyContainer.get(ActivosInfoService.class);
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

        ActivoInfo activoInfo = activoInfoService.getByNombreActivo(ticker, TipoActivo.ACCIONES);

        if(activoInfo.getPrecio() == -1){
            sender.sendMessage(DARK_RED + "Ticker no encontrado, los tickers se ven en /bolsa valores o en inernet como en es.investing.com. Solo se puede invertir en cantidad que cotizen en Estados Unidos");
            return;
        }

        String nombreValor = activoInfo.getNombreActivoLargo();
        double precio = activoInfo.getPrecio();

        if(jugadoresService.getByNombre(sender.getName()).getPixelcoins() < (precio * cantidad)){
            sender.sendMessage(DARK_RED + "No tienes las suficientes pixelcoins para pagar " + cantidad + " " + ticker + " a " + FORMATEA.format(precio) + " $ -> " + FORMATEA.format(precio * cantidad) + " PC");
            return;
        }

        var executedInMarket = OrderExecutorProxy.execute(of(sender.getName(), ticker, cantidad, TipoAccion.LARGO_COMPRA, null), () -> {
            comprarLargoUseCase.comprarLargo(sender.getName(), TipoActivo.ACCIONES, ticker.toUpperCase(), cantidad);
        });

        sendMessage(sender, cantidad, nombreValor, precio, executedInMarket);
    }

    private void sendMessage(CommandSender sender, int cantidad, String nombreValor, double precio, boolean executedInMarket) {
        if(executedInMarket){
            Bukkit.broadcastMessage(GOLD + sender.getName() + " ha comprado " + cantidad + " cantidad de "
                    + nombreValor + " a " + GREEN + FORMATEA.format(precio) + "PC");

            Funciones.enviarMensajeYSonido(Bukkit.getPlayer(sender.getName()), GOLD + "Has comprado " + FORMATEA.format(cantidad)
                    + " cantidad a " + GREEN + FORMATEA.format(precio) + " PC" + GOLD + " que es un total de " + GREEN +
                    FORMATEA.format(precio * cantidad) + " PC " + GOLD + " comandos: " + AQUA + "/bolsa cartera", ENTITY_PLAYER_LEVELUP);
        }else{
            sender.sendMessage(GOLD + "La compra no se ha podida ejecutar por que el mercado esta cerrado, cuando abra se ejecutara");
        }
    }
}
