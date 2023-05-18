package es.serversurvival.v2.pixelcoins.empresas.sacar;

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
public final class SacarPixelcoinsEmpresaUseCase {
    private final TransaccionesService transaccionesService;
    private final EmpresasValidador empresasValidador;
    private final Validador validador;
    private final EventBus eventBus;

    public void sacar(SacarPixelcoinsEmpresaUseCaseParametros parametros) {
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
