package es.serversurvival.v2.pixelcoins.empresas.crear;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistaEmpresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.v2.pixelcoins.empresas._shared.empleados.Empleado;
import es.serversurvival.v2.pixelcoins.empresas._shared.empleados.EmpleadosService;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class CrearEmpresaUseCase {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final EmpresasValidador empresasValidador;
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    public void crear(CrearEmpresaParametros parametros) {
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
