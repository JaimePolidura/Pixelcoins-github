package es.serversurvival.pixelcoins.empresas.depositar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.transacciones.Movimiento;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesSaver;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class DepositarPixelcoinsEmpresaUseCase implements UseCaseHandler<DepositarPixelcoinsEmpresaParametros> {
    private final TransaccionesSaver transaccionesSaver;
    private final EmpresasValidador empresasValidador;
    private final Validador validador;
    private final EventBus eventBus;

    @Override
    public void handle(DepositarPixelcoinsEmpresaParametros parametros) {
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.empresaNoCotizada(parametros.getEmpresaId());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        validador.jugadorTienePixelcoins(parametros.getJugadorId(), parametros.getPixelcoins());
        validador.numeroMayorQueCero(parametros.getPixelcoins(), "Las Pixelcoins");

        transaccionesSaver.save(Transaccion.builder()
                .pagadorId(parametros.getJugadorId())
                .pagadoId(parametros.getEmpresaId())
                .pixelcoins(parametros.getPixelcoins())
                .tipo(TipoTransaccion.EMPRESAS_DEPOSITAR)
                .build());

        eventBus.publish(new PixelcoinsDepositadas(parametros.getEmpresaId(), parametros.getPixelcoins()));
    }
}
