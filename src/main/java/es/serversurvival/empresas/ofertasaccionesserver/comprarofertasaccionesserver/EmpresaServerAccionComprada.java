package es.serversurvival.empresas.ofertasaccionesserver.comprarofertasaccionesserver;

import es.serversurvival.empresas.accionistasserver._shared.domain.TipoAccionista;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.transacciones._shared.domain.TipoTransaccion;
import es.serversurvival.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class EmpresaServerAccionComprada extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String comprador;
    @Getter private final String vendedor;
    @Getter private final TipoAccionista tipoAccionistaComprador;
    @Getter private final String empresaNombre;
    @Getter private final double pixelcoins;
    @Getter private final int cantidad;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), comprador, empresaNombre,
                (int) pixelcoins, "", TipoTransaccion.EMPRESA_COMPRA_ACCION_JUGADOR);
    }

    public static EmpresaServerAccionComprada of(String comprador, String vendedor, TipoAccionista tipoAccionistaComprador,
                                                 String empresaNombre, double pixelcoins, int cantidad){
        return new EmpresaServerAccionComprada(comprador,vendedor, tipoAccionistaComprador, empresaNombre, pixelcoins, cantidad);
    }
}
