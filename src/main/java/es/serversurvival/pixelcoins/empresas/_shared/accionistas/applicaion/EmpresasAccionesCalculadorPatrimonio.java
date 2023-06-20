package es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresa;
import es.serversurvival.pixelcoins.jugadores.patrimonio.CalculadorPatrimonio;
import es.serversurvival.pixelcoins.jugadores.patrimonio.TipoCuentaPatrimonio;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class EmpresasAccionesCalculadorPatrimonio implements CalculadorPatrimonio {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final TransaccionesService transaccionesService;
    private final EmpresasService empresasService;

    @Override
    public double calcular(UUID jugadorId) {
        List<AccionistaEmpresa> acciones = accionistasEmpresasService.findByJugadorId(jugadorId);
        double valorTotal = 0;

        for (AccionistaEmpresa accion : acciones) {
            Empresa empresa = empresasService.getById(accion.getEmpresaId());
            double pixelcoinsEmpresa = transaccionesService.getBalancePixelcoins(empresa.getEmpresaId());
            double porcentajeEmpresa = (double) accion.getNAcciones() / empresa.getNTotalAcciones();

            valorTotal += porcentajeEmpresa * pixelcoinsEmpresa;
        }

        return valorTotal;
    }

    @Override
    public TipoCuentaPatrimonio tipoCuenta() {
        return TipoCuentaPatrimonio.EMPRESAS_ACCIONES;
    }
}
