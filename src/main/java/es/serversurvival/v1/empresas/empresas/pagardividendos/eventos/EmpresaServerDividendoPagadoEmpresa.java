package es.serversurvival.v1.empresas.empresas.pagardividendos.eventos;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v1.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival.v1.transacciones._shared.domain.Transaccion;
import es.serversurvival.v1.transacciones._shared.domain.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class EmpresaServerDividendoPagadoEmpresa extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String empresa;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), empresa, empresa, (int) pixelcoins, "", TipoTransaccion.EMPRESA_DIVIDENDO_ACCION);
    }

    public static EmpresaServerDividendoPagadoEmpresa of(String empresa, double pixelcoins){
        return new EmpresaServerDividendoPagadoEmpresa(empresa, pixelcoins);
    }
}
