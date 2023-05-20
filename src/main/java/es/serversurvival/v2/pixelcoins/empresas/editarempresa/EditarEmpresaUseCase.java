package es.serversurvival.v2.pixelcoins.empresas.editarempresa;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class EditarEmpresaUseCase {
    private final EmpresasValidador empresasValidador;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    public void editar(EditarEmpresaParametros parametros) {
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        empresasValidador.descripccionCorrecta(parametros.getNuevaDescripccion());
        empresasValidador.nombreEmpresaCorrecta(parametros.getNuevoNombre());
        empresasValidador.validarIcono(parametros.getNuevoNombre());

        Empresa empresa = empresasService.getById(parametros.getEmpresaId());
        empresasService.save(empresa.editar(parametros));

        eventBus.publish(new EmpresaEditada(parametros.getEmpresaId()));
    }
}
