package es.serversurvival.deudas.prestar;

import es.serversurvival._shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones.mySQL.Transaccion;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.transacciones.mySQL.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class PixelcoinsPrestadasEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final int pixelcoins;
    @Getter private final int intereses;
    @Getter private final int dias;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), acredor, deudor, Funciones.aumentarPorcentaje(pixelcoins, intereses), "", TipoTransaccion.DEUDAS_PRIMERA_TRANSFERENCIA);
    }
}
