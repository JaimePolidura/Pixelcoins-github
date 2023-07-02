package es.serversurvival.minecraftserver.empresas.misacciones;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.ponerventa.AccionPuestaVenta;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GOLD;

@EventHandler
@AllArgsConstructor
public final class OnAccionEmpresaPuestaVenta {
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;

    @EventListener
    public void on(AccionPuestaVenta e) {
        Empresa empresa = empresasService.getById(e.getPonerVentaAccionesParametros().getEmpresaId());
        UUID vendedorId = e.getPonerVentaAccionesParametros().getJugadorId();
        String vendedorNombre = jugadoresService.getNombreById(vendedorId);

        enviadorMensajes.enviarMensaje(vendedorId, GOLD + "Se ha puesto a la venta la accion de la empresa. " +
                "Para consultarlo: " + AQUA + "/empresas mercado");

        MinecraftUtils.broadcastExcept(vendedorId, GOLD + vendedorNombre + " ha subido cantidad de la empresa del servidor: " +
                empresa.getNombre() + AQUA + " /empresas mercado");
    }
}
