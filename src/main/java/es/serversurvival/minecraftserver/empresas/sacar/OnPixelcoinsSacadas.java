package es.serversurvival.minecraftserver.empresas.sacar;


import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.sacar.PixelcoinsSacadasEmpresa;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.GOLD;

@EventHandler
@AllArgsConstructor
public final class OnPixelcoinsSacadas {
    private final EnviadorMensajes enviadorMensajes;
    private final EmpresasService empresasService;

    @EventListener
    public void on(PixelcoinsSacadasEmpresa e) {
        Empresa empresa = empresasService.getById(e.getEmpresaId());

        enviadorMensajes.enviarMensajeYSonido(empresa.getDirectorJugadorId(), Sound.ENTITY_PLAYER_LEVELUP, GOLD +
                "Has sacado " + formatPixelcoins(e.getPixelcoins()) + " en tu empresa: " + DARK_AQUA + empresa.getNombre());
    }
}
