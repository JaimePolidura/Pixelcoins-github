package es.serversurvival.minecraftserver.empresas.repatirdividendos;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.repartirdividendos.DividendosEmpresaRepartido;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.Sound.*;

@EventHandler
@AllArgsConstructor
public final class OnDividendosRepartidos {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final EnviadorMensajes enviadorMensajes;
    private final EmpresasService empresasService;

    @EventListener
    public void on(DividendosEmpresaRepartido e) {
        Empresa empresa = empresasService.getById(e.getEmpresaId());

        enviadorMensajes.enviarMensajeYSonido(empresa.getDirectorJugadorId(), ENTITY_PLAYER_LEVELUP, GOLD + "Se ha pagado todos los dividendos");

        accionistasEmpresasService.findByEmpresaId(empresa.getEmpresaId()).forEach(accionista -> {
            double totalRecibido = accionista.getNAcciones() * e.getDividendoPorAccion();

            enviadorMensajes.enviarMensajeYSonido(accionista.getAccionisaJugadorId(), ENTITY_PLAYER_LEVELUP, GOLD + "Has recibido " + formatPixelcoins(totalRecibido)
                    + " de dividendos de la empresa " + empresa.getNombre() + ". El dividendo ha sido de " + formatPixelcoins(e.getDividendoPorAccion()) + "/ accion");
        });
    }
}
