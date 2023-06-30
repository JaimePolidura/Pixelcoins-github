package es.serversurvival.minecraftserver.bolsa.vercartera;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.bolsa.vercarteraresumida.CarteraBolsaResumida;
import es.serversurvival.pixelcoins.bolsa.vercarteraresumida.CarteraResumidaItem;
import es.serversurvival.pixelcoins.bolsa.vercarteraresumida.VerCarteraResumidaUseCase;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.Jugador;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.*;
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
        if(comando.getJugador() == null || comando.getJugador().equalsIgnoreCase("")){
            menuService.open(player, MiCarteraBolsaMenu.class);
            return;
        }

        mostrarCarteraResumidaDelOtroJugador(comando.getJugador() == null || comando.getJugador().equals("") ?
                player.getName() : comando.getJugador(), player);
    }

    private void mostrarCarteraResumidaDelOtroJugador(String jugadorVerCartera, Player player) {
        UUID jugadorId = jugadoresService.getByNombre(jugadorVerCartera).getJugadorId();

        CarteraBolsaResumida carteraBolsaResumida = verCarteraResumidaUseCase.ver(jugadorId);

        player.sendMessage(GOLD + "Cartera de " + jugadorVerCartera + " ~ " + formatPixelcoins(carteraBolsaResumida.getValorTotalCartera()) + " :");

        for (CarteraResumidaItem itemCartera : carteraBolsaResumida.getItems()) {
            String nombreActivo = activosBolsaService.getById(itemCartera.getActivoBolsaId())
                    .getNombreLargo();

            player.sendMessage(GOLD + " - " + formatPorcentaje(itemCartera.getPeso()) + nombreActivo + " " + itemCartera.getTipoBolsaApuesta().toString()
                    + " Precio medio: " + formatRentabilidad(itemCartera.getPrecioMedio()) + GOLD + "Precio actual: " + formatPixelcoins(itemCartera.getPrecioActual()));
        }
    }
}
