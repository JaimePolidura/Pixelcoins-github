package es.serversurvival.minecraftserver.empresas.votaciones.lorevotacionitem;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.empresas.cambiardirector.CambiarDirectorVotacion;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@Service
@AllArgsConstructor
public final class CambiarDirectorLoreBuilder implements VotacionItemLoreBuilder<CambiarDirectorVotacion> {
    private final JugadoresService jugadoresService;

    @Override
    public List<String> build(CambiarDirectorVotacion votacion) {
        return List.of(
                GOLD + "Nuevo director: " + jugadoresService.getNombreById(votacion.getNuevoDirectorJugadorId()),
                GOLD + "Sueldo: " + formatPixelcoins(votacion.getSueldo()) + "/ " + millisToDias(votacion.getPeriodoPagoMs()) + " dias",
                GOLD + "Razon cambio: " + votacion.getDescripcion()
        );
    }
}
