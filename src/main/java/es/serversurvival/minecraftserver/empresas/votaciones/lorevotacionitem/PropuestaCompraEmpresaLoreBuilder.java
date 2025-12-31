package es.serversurvival.minecraftserver.empresas.votaciones.lorevotacionitem;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.adquirir.proponer.AceptarOfertaCompraEmpresaVotacion;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static org.bukkit.ChatColor.GOLD;

@Service
@AllArgsConstructor
public class PropuestaCompraEmpresaLoreBuilder implements VotacionItemLoreBuilder<AceptarOfertaCompraEmpresaVotacion> {
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;

    @Override
    public List<String> build(AceptarOfertaCompraEmpresaVotacion votacion) {
        Empresa empresa = empresasService.getById(votacion.getEmpresaId());

        return List.of(
                GOLD + "Precio total: " + formatPixelcoins(votacion.getPrecioTotal()),
                GOLD + "Precio / Accion" + formatPixelcoins(votacion.getPrecioTotal() / empresa.getNTotalAcciones()),
                GOLD + "Comprador " + getCompradorName(votacion)
        );
    }

    private String getCompradorName(AceptarOfertaCompraEmpresaVotacion votacion) {
        if (votacion.isEsJugador()) {
            return jugadoresService.getNombreById(votacion.getCompradorId()) + " (Jugador)";
        } else {
            return empresasService.getById(votacion.getCompradorId()) + " (Empresa)";
        }
    }
}
