package es.serversurvival.pixelcoins.empresas.crear;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class CrearEmpresaUseCase implements UseCaseHandler<CrearEmpresaParametros> {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final EmpresasValidador empresasValidador;
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    @Override
    public void handle(CrearEmpresaParametros parametros) {
        empresasValidador.descripccionCorrecta(parametros.getDescripccion());
        empresasValidador.nombreEmpresaCorrecta(parametros.getNombre());
        empresasValidador.validarIcono(parametros.getIcono());
        empresasValidador.empresaNoExiste(parametros.getNombre());

        Empresa empresa = Empresa.fromParametrosCrearEmpresa(parametros);

        empresasService.save(empresa);
        empleadosService.save(Empleado.fromDirectorEmpresa(empresa));
        accionistasEmpresasService.save(AccionistaEmpresa.fromFundadorEmpresa(empresa));

        eventBus.publish(new EmpresaCreada(empresa.getEmpresaId()));
    }
}
