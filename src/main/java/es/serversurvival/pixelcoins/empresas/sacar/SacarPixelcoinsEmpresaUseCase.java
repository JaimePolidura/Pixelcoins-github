package es.serversurvival.pixelcoins.empresas.sacar;

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
public final class SacarPixelcoinsEmpresaUseCase implements UseCaseHandler<SacarPixelcoinsEmpresaParametros> {
    private final TransaccionesService transaccionesService;
    private final EmpresasValidador empresasValidador;
    private final Validador validador;
    private final EventBus eventBus;

    @Override
    public void handle(SacarPixelcoinsEmpresaParametros parametros) {
        validador.numeroMayorQueCero(parametros.getPixelcoins(), "Las pixelcoins a sacar");
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.empresaNoCotizada(parametros.getEmpresaId());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        empresasValidador.tienePixelcoinsSuficientes(parametros.getEmpresaId(), parametros.getPixelcoins());

        transaccionesService.save(Transaccion.builder()
                .pagadorId(parametros.getEmpresaId())
                .pagadoId(parametros.getJugadorId())
                .pixelcoins(parametros.getPixelcoins())
                .tipo(TipoTransaccion.EMPRESAS_SACAR)
                .build());

        eventBus.publish(new PixelcoinsSacadasEmpresa(parametros.getEmpresaId(), parametros.getPixelcoins()));
    }
}
