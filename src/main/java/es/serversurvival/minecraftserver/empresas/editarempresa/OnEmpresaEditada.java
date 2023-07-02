package es.serversurvival.minecraftserver.empresas.editarempresa;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import es.serversurvival.pixelcoins.empresas.editarempresa.EmpresaEditada;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import static org.bukkit.ChatColor.*;

@EventHandler
@AllArgsConstructor
public final class OnEmpresaEditada {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final EnviadorMensajes enviadorMensajes;
    private final EmpleadosService empleadosService;

    @EventListener
    public void onEmpresaEditada(EmpresaEditada empresaEditada) {
        enviadorMensajes.enviarMensaje(empresaEditada.getDirectorId(), GOLD + "Has editado la empresa " +
                empresaEditada.getEmpresaAntigua().getNombre());

        empleadosService.findEmpleoActivoByEmpresaId(empresaEditada.getEmpresaId()).forEach(empleado -> {
            enviadorMensajes.enviarMensaje(empleado.getEmpleadoJugadorId(), GOLD +  "Se ha editado la empresa "+
                    empresaEditada.getEmpresaAntigua().getNombre()+" donde trabajas. Ahora su nombre es " + empresaEditada.getEmpresaAhora().getNombre()
                    + " con la descripcion " + empresaEditada.getEmpresaAhora().getDescripcion() + " y logitpo " + empresaEditada.getEmpresaAhora().getLogotipo());
        });
        accionistasEmpresasService.findByEmpresaId(empresaEditada.getEmpresaId()).forEach(accionista -> {
            enviadorMensajes.enviarMensaje(accionista.getAccionisaJugadorId(), GOLD +  "Se ha editado la empresa "+
                    empresaEditada.getEmpresaAntigua().getNombre()+" donde eres accionista. Ahora su nombre es " + empresaEditada.getEmpresaAhora().getNombre()
                    + " con la descripcion " + empresaEditada.getEmpresaAhora().getDescripcion() + " y logitpo " + empresaEditada.getEmpresaAhora().getLogotipo());
        });
    }
}
