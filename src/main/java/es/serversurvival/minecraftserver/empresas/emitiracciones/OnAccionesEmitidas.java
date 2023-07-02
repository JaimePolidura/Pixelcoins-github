package es.serversurvival.minecraftserver.empresas.emitiracciones;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.emitiracciones.AccionesEmitidasEmpresa;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GOLD;

@EventHandler
@AllArgsConstructor
public final class OnAccionesEmitidas {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final EnviadorMensajes enviadorMensajes;
    private final EmpresasService empresasService;

    @EventListener
    public void on(AccionesEmitidasEmpresa e) {
        Empresa empresa = empresasService.getById(e.getEmpresaId());

        enviadorMensajes.enviarMensaje(empresa.getDirectorJugadorId(), GOLD + "Has emitido " + e.getNAccionesAEmitir() + " acciones por " +
                formatPixelcoins(e.getPrecioPorAccion()) + "Se diluira cuando alguien compre las acciones. Para ver las acciones " + AQUA + "/empresas mercado");

        accionistasEmpresasService.findByEmpresaId(e.getEmpresaId()).forEach(accionista -> {
            enviadorMensajes.enviarMensaje(accionista.getAccionisaJugadorId(), "En la empresa donde eres accionista "+empresa.getNombre()+" se han emitido "+e.getNAccionesAEmitir()+
                    " acciones por "+formatPixelcoins(e.getPrecioPorAccion())+"/ accion. Se diluira cuando alguien compre las acciones");
        });
    }
}
