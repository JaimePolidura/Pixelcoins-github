package es.serversurvival.pixelcoins.empresas.repartirdividendos;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class RepartirDividendosUseCase implements UseCaseHandler<RepartirDividendosParametros> {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final TransaccionesService transaccionesService;
    private final EmpresasValidador empresasValidador;
    private final EmpresasService empresasService;
    private final Validador validador;
    private final EventBus eventBus;

    @Override
    public void handle(RepartirDividendosParametros parametros) {
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.empresaCotizada(parametros.getEmpresaId());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        validador.numeroMayorQueCero(parametros.getDividendoPorAccion(), "El dividendo");

        Empresa empresa = empresasService.getById(parametros.getEmpresaId());
        double gastosTotales = empresa.getNTotalAcciones() * parametros.getDividendoPorAccion();
        empresasValidador.tienePixelcoinsSuficientes(parametros.getEmpresaId(), gastosTotales);

        accionistasEmpresasService.findByEmpresaId(parametros.getEmpresaId()).forEach(accionistaEmpresa -> {
            repartirDividendo(accionistaEmpresa, parametros.getDividendoPorAccion());
        });

        eventBus.publish(new DividendosEmpresaRepartido(parametros.getEmpresaId(), parametros.getDividendoPorAccion()));
    }

    private void repartirDividendo(AccionistaEmpresa accionista, double dividendoPorAccion) {
        this.transaccionesService.save(Transaccion.builder()
                        .pagadorId(accionista.getEmpresaId())
                        .pagadoId(accionista.getAccionisaJugadorId())
                        .tipo(TipoTransaccion.EMPRESAS_DIVIDENDO)
                        .pixelcoins(dividendoPorAccion * accionista.getNAcciones())
                .build());
    }
}
