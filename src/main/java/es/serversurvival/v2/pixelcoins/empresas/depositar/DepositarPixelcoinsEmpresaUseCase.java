package es.serversurvival.v2.pixelcoins.empresas.depositar;

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
public final class DepositarPixelcoinsEmpresaUseCase {
    private final TransaccionesService transaccionesService;
    private final EmpresasValidador empresasValidador;
    private final Validador validador;
    private final EventBus eventBus;

    public void depositar(DepositarPixelcoinsEmpresaParametros parametros) {
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.empresaNoCotizada(parametros.getEmpresaId());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        validador.jugadorTienePixelcoins(parametros.getJugadorId(), parametros.getPixelcoins());
        validador.numeroMayorQueCero(parametros.getPixelcoins(), "Las Pixelcoins");

        transaccionesService.save(Transaccion.builder()
                        .pagadorId(parametros.getJugadorId())
                        .pagadoId(parametros.getEmpresaId())
                        .pixelcoins(parametros.getPixelcoins())
                        .tipo(TipoTransaccion.EMPRESAS_DEPOSITAR)
                .build());

        eventBus.publish(new PixelcoinsDepositadas(parametros.getEmpresaId(), parametros.getPixelcoins()));
    }
}
