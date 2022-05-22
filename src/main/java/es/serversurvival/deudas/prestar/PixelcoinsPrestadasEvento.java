package es.serversurvival.deudas.prestar;

import es.serversurvival.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.transacciones._shared.domain.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class PixelcoinsPrestadasEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final double pixelcoins;
    @Getter private final int intereses;
    @Getter private final int dias;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), acredor, deudor, Funciones.aumentarPorcentaje(pixelcoins, intereses), "", TipoTransaccion.DEUDAS_PRIMERA_TRANSFERENCIA);
    }
}
