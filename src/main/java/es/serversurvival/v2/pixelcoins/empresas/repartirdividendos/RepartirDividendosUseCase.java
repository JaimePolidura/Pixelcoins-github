package es.serversurvival.v2.pixelcoins.empresas.repartirdividendos;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins._shared.Validador;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistaEmpresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class RepartirDividendosUseCase {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final TransaccionesService transaccionesService;
    private final EmpresasValidador empresasValidador;
    private final EmpresasService empresasService;
    private final Validador validador;
    private final EventBus eventBus;

    public void repartirDividendos(RepartirDividendosParametros parametros) {
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
