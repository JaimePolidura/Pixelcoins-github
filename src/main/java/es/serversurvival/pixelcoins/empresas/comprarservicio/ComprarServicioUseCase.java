package es.serversurvival.pixelcoins.empresas.comprarservicio;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class ComprarServicioUseCase implements UseCaseHandler<ComprarServicioParametros> {
    private final TransaccionesService transaccionesService;
    private final EmpresasValidador empresasValidador;
    private final Validador validador;
    private final EventBus eventBus;

    @Override
    public void handle(ComprarServicioParametros parametros) throws Exception {
        validador.numeroMayorQueCero(parametros.getPixelcoins(), "Pixelcoins");
        validador.jugadorTienePixelcoins(parametros.getJugadorId(), parametros.getPixelcoins());
        empresasValidador.noEmpleadoEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());

        this.transaccionesService.save(Transaccion.builder()
                .pagadorId(parametros.getJugadorId())
                .pagadoId(parametros.getEmpresaId())
                .pixelcoins(parametros.getPixelcoins())
                .tipo(TipoTransaccion.EMPRESAS_COMPRA_SERVICIO)
                .build());

        eventBus.publish(new EmpresaServicioComprado(parametros.getEmpresaId(), parametros.getJugadorId(), parametros.getPixelcoins()));
    }
}
