package es.serversurvival.pixelcoins.empresas.cambiardirector.votacionfinalizada;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.VotacionFinalizadaListener;
import es.serversurvival.pixelcoins.empresas._shared.empleados.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.ResultadoVotacion;
import es.serversurvival.pixelcoins.empresas.cambiardirector.CambiarDirectorVotacion;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnVotacionCambiarDirectorFinalizada implements VotacionFinalizadaListener<CambiarDirectorVotacion> {
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    @Override
    public void on(CambiarDirectorVotacion votacion, ResultadoVotacion resultado) {
        if(!resultado.aceptado())
            return;

        Empresa empresa = empresasService.getById(votacion.getEmpresaId());
        Empleado nuevoDirector = Empleado.fromVotacionCambiarDirector(votacion);
        Empleado antiguoDirector = empleadosService.getEmpleoActivoByEmpresaIdAndEmpleadoJugadorId(empresa.getEmpresaId(),
                empresa.getDirectorJugadorId());

        empresasService.save(empresa.nuevoDirector(nuevoDirector.getEmpleadoJugadorId()));
        empleadosService.save(antiguoDirector.despedir(votacion.getDescripccion()));
        empleadosService.save(nuevoDirector);

        eventBus.publish(new DirectorCambiado(empresa.getEmpresaId(), antiguoDirector.getEmpleadoJugadorId(), nuevoDirector.getEmpleadoJugadorId()));
    }
}
