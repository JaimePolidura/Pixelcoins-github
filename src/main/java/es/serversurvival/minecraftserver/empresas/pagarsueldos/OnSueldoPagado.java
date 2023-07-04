package es.serversurvival.minecraftserver.empresas.pagarsueldos;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.pagarsueldos.SueldoEmpresaPagado;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.GOLD;

@EventHandler
@AllArgsConstructor
public final class OnSueldoPagado {
    private final EnviadorMensajes enviadorMensajes;
    private final EmpresasService empresasService;

    @EventListener
    public void on(SueldoEmpresaPagado e) {
        Empresa empresa = empresasService.getById(e.getEmpresaId());

        enviadorMensajes.enviarMensajeYSonido(e.getJugadorId(), Sound.ENTITY_PLAYER_LEVELUP, GOLD + "Has sido pagado " + formatPixelcoins(e.getSueldo()) +
                "de la empresa " + empresa.getNombre() + " donde trabajas");
    }
}
