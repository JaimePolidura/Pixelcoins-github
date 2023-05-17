package es.serversurvival.v2.pixelcoins.empresas.comprarservicio;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins._shared.Validador;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class ComprarServicioUseCase {
    private final TransaccionesService transaccionesService;
    private final EmpresasValidador empresasValidador;
    private final Validador validador;
    private final EventBus eventBus;

    public void comprarServicio(ComprarServicioUseCaseParametros parametros) {
        validador.numeroMayorQueCero(parametros.getPixelcoins(), "Pixelcoins");
        validador.jugadorTienePixelcoins(parametros.getCompradorJugadorId(), parametros.getPixelcoins());
        empresasValidador.noEmpleadoEmpresa(parametros.getEmpresaId(), parametros.getCompradorJugadorId());
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());

        this.transaccionesService.save(Transaccion.builder()
                        .pagadorId(parametros.getCompradorJugadorId())
                        .pagadoId(parametros.getEmpresaId())
                        .pixelcoins(parametros.getPixelcoins())
                        .tipo(TipoTransaccion.EMPRESAS_COMPRA_SERVICIO)
                .build());

        eventBus.publish(new EmpresaServicioComprado(parametros.getEmpresaId(), parametros.getCompradorJugadorId(), parametros.getPixelcoins()));
    }
}
