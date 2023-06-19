package es.serversurvival.minecraftserver.jugadores.dinero;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.jugadores.patrimonio.CalculadorPatrimonioService;
import es.serversurvival.pixelcoins.jugadores.patrimonio.TipoCuentaPatrimonio;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
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
    public void execute(DineroComando comando, Player player) {
        UUID jugadorIdVerPatrimonio = getIdJugador(comando, player);

        Map<TipoCuentaPatrimonio, Double> desglosadoPatrimonio = calculadorPatrimonioService.calcularDesglosadoPorCuentas(jugadorIdVerPatrimonio);

        printPatrimonioJugador(desglosadoPatrimonio, comando, player);

        for (Map.Entry<TipoCuentaPatrimonio, Double> entry : desglosadoPatrimonio.entrySet()) {
            TipoCuentaPatrimonio tipoCuenta = entry.getKey();
            double pixelcoins = entry.getValue();

            player.sendMessage(GOLD + "   " + tipoCuenta.getAlias() + ": " + (pixelcoins < 0 ? RED : GREEN) + FORMATEA.format(pixelcoins) + " PC");
        }
    }

    private void printPatrimonioJugador(Map<TipoCuentaPatrimonio, Double> desglosadoPatrimonio, DineroComando comando, Player sender) {
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
