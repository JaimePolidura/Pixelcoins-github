package es.serversurvival.pixelcoins.empresas.editarempresa;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class EditarEmpresaUseCase implements UseCaseHandler<EditarEmpresaParametros> {
    private final EmpresasValidador empresasValidador;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    @Override
    public void handle(EditarEmpresaParametros parametros) {
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        empresasValidador.descripccionCorrecta(parametros.getNuevaDescripccion());
        empresasValidador.nombreEmpresaCorrecta(parametros.getNuevoNombre());
        empresasValidador.validarIcono(parametros.getNuevoNombre());

        Empresa empresa = empresasService.getById(parametros.getEmpresaId());

        if(!empresa.getNombre().equalsIgnoreCase(parametros.getNuevoNombre()))
            empresasValidador.empresaNoExiste(parametros.getNuevoNombre());

        empresasService.save(empresa.editar(parametros));

        eventBus.publish(new EmpresaEditada(parametros.getEmpresaId()));
    }
}
