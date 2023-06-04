package es.serversurvival.v2.minecraftserver.jugadores.dinero;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1.jugadores._shared.application.CalculadorPatrimonio;
import es.serversurvival.v2.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.v2.pixelcoins.jugadores.patrimonio.CalculadorPatrimonioService;
import es.serversurvival.v2.pixelcoins.jugadores.patrimonio.TipoCuentaPatrimonio;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@Command(
        value = "dinero",
        explanation = "Ver todas tus pixelcoins",
        args = {"[jugador]"}
)
@AllArgsConstructor
public class DineroComandoRunner implements CommandRunnerArgs<DineroComando> {
    private final CalculadorPatrimonioService calculadorPatrimonioService;
    private final JugadoresService jugadoresService;

    @Override
    public void execute(DineroComando comando, CommandSender sender) {
        UUID jugadorIdVerPatrimonio = getIdJugador(comando, (Player) sender);

        Map<TipoCuentaPatrimonio, Double> desglosadoPatrimonio = calculadorPatrimonioService.calcularDesglosadoPorCuentas(jugadorIdVerPatrimonio);

        printPatrimonioJugador(desglosadoPatrimonio, comando, sender);

        for (Map.Entry<TipoCuentaPatrimonio, Double> entry : desglosadoPatrimonio.entrySet()) {
            TipoCuentaPatrimonio tipoCuenta = entry.getKey();
            double pixelcoins = entry.getValue();

            sender.sendMessage(GOLD + " " + tipoCuenta.getAlias() + ": " + (pixelcoins < 0 ? RED : GREEN) + FORMATEA.format(pixelcoins) + " PC");
        }
    }

    private void printPatrimonioJugador(Map<TipoCuentaPatrimonio, Double> desglosadoPatrimonio, DineroComando comando, CommandSender sender) {
        double suma = desglosadoPatrimonio.values().stream()
                .mapToDouble(a -> a)
                .sum();

        if(comando.getJugador() == null || comando.getJugador().equalsIgnoreCase("")){
            sender.sendMessage(GOLD + "Tu dinero total: " + GREEN + FORMATEA.format(suma) + " PC " + GOLD + "Esta formado por:");
        }else{
            sender.sendMessage(GOLD + "El dinero total de "+comando.getJugador()+" : " + GREEN + FORMATEA.format(suma) + " PC " + GOLD + "Esta formado por:");
        }
    }

    private UUID getIdJugador(DineroComando comando, Player sender) {
        return comando.getJugador() == null ? sender.getUniqueId() : jugadoresService.getByNombre(comando.getJugador()).getJugadorId();
    }
}
