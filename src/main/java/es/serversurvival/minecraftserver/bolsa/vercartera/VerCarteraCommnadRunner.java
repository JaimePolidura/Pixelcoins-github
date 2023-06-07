package es.serversurvival.minecraftserver.bolsa.vercartera;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.bolsa.vercarteraresumida.CarteraBolsaResumida;
import es.serversurvival.pixelcoins.bolsa.vercarteraresumida.CarteraResumidaItem;
import es.serversurvival.pixelcoins.bolsa.vercarteraresumida.VerCarteraResumidaUseCase;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

@Command(
        value = "bolsa cartera",
        args = {"[jugador]"},
        explanation = "Ver tu cartera o la de otro jugador"
)
@RequiredArgsConstructor
public final class VerCarteraCommnadRunner implements CommandRunnerArgs<VerCarteraComando> {
    private final VerCarteraResumidaUseCase verCarteraResumidaUseCase;
    private final ActivosBolsaService activosBolsaService;
    private final JugadoresService jugadoresService;
    private final MenuService menuService;

    @Override
    public void execute(VerCarteraComando comando, Player player) {
        if(comando.getJugador() == null){
            mostrarMenuVerCarteraDelMismoJugador(comando, player);
        }else{
            mostrarCarteraResumidaDelOtroJugador(comando, player);
        }
    }

    private void mostrarMenuVerCarteraDelMismoJugador(VerCarteraComando comando, Player player) {

    }

    private void mostrarCarteraResumidaDelOtroJugador(VerCarteraComando comando, Player player) {
        UUID jugadorId = jugadoresService.getByNombre(comando.getJugador()).getJugadorId();

        CarteraBolsaResumida carteraBolsaResumida = verCarteraResumidaUseCase.ver(jugadorId);

        player.sendMessage(GOLD + "Cartera de " + comando.getJugador() + " ~ " + GREEN + Funciones.FORMATEA.format(carteraBolsaResumida.getValorTotalCartera()) + " PC: ");

        for (CarteraResumidaItem itemCartera : carteraBolsaResumida.getItems()) {
            String nombreActivo = activosBolsaService.getById(itemCartera.getActivoBolsaId())
                    .getNombreLargo();

            player.sendMessage(GOLD + " - " + itemCartera.getPeso() + "% " + nombreActivo + " " + itemCartera.getTipoBolsaApuesta().toString()
                    + " Precio medio: " + GREEN + Funciones.FORMATEA.format(itemCartera.getPrecioMedio()) + " PC " + GOLD + "Precio actual: "
                    + GREEN + itemCartera.getPrecioActual() + " PC");
        }
    }
}
