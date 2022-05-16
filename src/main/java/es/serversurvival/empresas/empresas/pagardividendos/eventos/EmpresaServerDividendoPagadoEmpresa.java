package es.serversurvival.empresas.empresas.pagardividendos.eventos;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.EMPRESA_DIVIDENDO_ACCION;

@AllArgsConstructor
public final class EmpresaServerDividendoPagadoEmpresa extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String empresa;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), empresa, empresa, (int) pixelcoins, "", EMPRESA_DIVIDENDO_ACCION);
    }
}
