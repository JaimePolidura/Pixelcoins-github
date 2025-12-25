package es.serversurvival.pixelcoins.empresas.adquirir;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.RazonCierre;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.application.VotacionesService;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.EstadoVotacion;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.transacciones.application.MovimientosService;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesSaver;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.domain.Transaccion;
import lombok.AllArgsConstructor;

import java.util.List;

import static es.serversurvival.pixelcoins.mercado._shared.TipoOferta.*;

@UseCase
@AllArgsConstructor
public class AdquirirEmpresaUseCase implements UseCaseHandler<AdquirirEmpresaParametros> {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final TransaccionesSaver transaccionesSaver;
    private final MovimientosService movimientosService;
    private final EmpresasValidador empresasValidador;
    private final VotacionesService votacionesService;
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;
    private final OfertasService ofertasService;
    private final Validador validador;

    @Override
    public void handle(AdquirirEmpresaParametros parametros) {
        validar(parametros);

        Empresa empresaCompradora = empresasService.getById(parametros.getEmpresaIdCompradora());
        Empresa empresaAdquirida = empresasService.getById(parametros.getEmpresaIdAdquirida());

        empresasService.save(empresaAdquirida.cerrar(RazonCierre.ADQUIRIDA));

        double pixelcoinsEmpresaAdquirida = movimientosService.getBalance(empresaAdquirida.getEmpresaId());
        transaccionesSaver.save(Transaccion.builder()
                .tipo(TipoTransaccion.EMPRESAS_ADQUISICION_ACTIVOS)
                .pixelcoins(pixelcoinsEmpresaAdquirida)
                .pagadorId(empresaAdquirida.getEmpresaId())
                .pagadoId(empresaCompradora.getEmpresaId())
                .otro(parametros.getJugadorId())
                .build());

        List<AccionistaEmpresa> accionistasEmpresaAdquirida = accionistasEmpresasService.findByEmpresaId(empresaAdquirida.getEmpresaId());
        double precioPorAccion = parametros.getPrecioTotal() / empresaAdquirida.getNTotalAcciones();
        for (AccionistaEmpresa accionistaEmpresa : accionistasEmpresaAdquirida) {
            transaccionesSaver.save(Transaccion.builder()
                    .tipo(TipoTransaccion.EMPRESAS_ADQUISICION_PAGO_ACCIONISTAS)
                    .pixelcoins(precioPorAccion * accionistaEmpresa.getNAcciones())
                    .pagadorId(empresaCompradora.getEmpresaId())
                    .pagadoId(accionistaEmpresa.getAccionisaJugadorId())
                    .otro(parametros.getJugadorId())
                    .build());
        }
        accionistasEmpresasService.deleteByEmpresaId(empresaAdquirida.getEmpresaId());

        empleadosService.findEmpleoActivoByEmpresaId(empresaAdquirida.getEmpresaId()).forEach(empleadoEmpresaAdquirida -> {
            empleadosService.save(empleadoEmpresaAdquirida.despedir("Empresa adquirda por " + empresaCompradora.getNombre()));
            empleadosService.save(empleadoEmpresaAdquirida.moverAEmpresa(empresaCompradora.getEmpresaId()));
        });

        ofertasService.deleteByObjetoYTipo(empresaAdquirida.getEmpresaId(), ACCIONES_SERVER_IPO);
        ofertasService.deleteByObjetoYTipo(empresaAdquirida.getEmpresaId(), ACCIONES_SERVER_EMISION);
        ofertasService.deleteByObjetoYTipo(empresaAdquirida.getEmpresaId(), ACCIONES_SERVER_JUGADOR);

        votacionesService.findByEmpresaId(empresaAdquirida.getEmpresaId()).stream()
                .filter(voto -> voto.getVotacionId() != parametros.getVotacionId().get())
                .forEach(voto -> {
                    votacionesService.save(voto.finalizar(EstadoVotacion.RECHAZADO));
                });
    }

    public void validar(AdquirirEmpresaParametros parametros) {
        validador.numeroMayorQueCero(parametros.getPrecioTotal(), "Las pixelcoins");
        validador.notEqual(parametros.getEmpresaIdAdquirida(), parametros.getEmpresaIdCompradora(), "No puedes comprar tu misma empresa");
        empresasValidador.directorEmpresa(parametros.getEmpresaIdCompradora(), parametros.getJugadorId());
        empresasValidador.empresaNoCerrada(parametros.getEmpresaIdAdquirida());
        empresasValidador.empresaNoCerrada(parametros.getEmpresaIdCompradora());
        empresasValidador.tienePixelcoinsSuficientes(parametros.getEmpresaIdCompradora(), parametros.getPrecioTotal());

        Empresa empresa = empresasService.getById(parametros.getEmpresaIdAdquirida());
        if (empresa.isEsCotizada()) {
            validador.isPresent(parametros.getVotacionId(), "Si la empresa es cotizada, debe de haber una votacion abierta");
            votacionesService.getById(parametros.getVotacionId().get());
        }
    }
}
