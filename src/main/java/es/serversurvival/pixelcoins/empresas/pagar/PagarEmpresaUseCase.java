package es.serversurvival.pixelcoins.empresas.pagar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesSaver;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PagarEmpresaUseCase implements UseCaseHandler<PagarEmpresaParametros> {
    private final TransaccionesSaver transaccionesSaver;
    private final EmpresasValidador empresasValidador;
    private final Validador validador;
    private final EventBus eventBus;

    @Override
    public void handle(PagarEmpresaParametros parametros) {
        validador.numeroMayorQueCero(parametros.getPixelcoins(), "Pixelcoins");
        validador.jugadorTienePixelcoins(parametros.getJugadorId(), parametros.getPixelcoins());
        empresasValidador.noEmpleadoEmpresa(parametros.getEmpresaId(), parametros.getJugadorId(), "Eres empleado de la empresa, no puedes pagarla`");
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());

        transaccionesSaver.save(Transaccion.builder()
                .pagadorId(parametros.getJugadorId())
                .pagadoId(parametros.getEmpresaId())
                .pixelcoins(parametros.getPixelcoins())
                .tipo(TipoTransaccion.EMPRESAS_COMPRA_SERVICIO)
                .build());

        eventBus.publish(new EmpresaPagada(parametros.getEmpresaId(), parametros.getJugadorId(), parametros.getPixelcoins()));
    }
}
