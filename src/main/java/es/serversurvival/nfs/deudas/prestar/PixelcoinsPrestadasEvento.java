package es.serversurvival.nfs.deudas.prestar;

import es.serversurvival.legacy.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;
import es.serversurvival.nfs.utils.Funciones;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.legacy.mySQL.enums.TipoTransaccion.*;

@AllArgsConstructor
public final class PixelcoinsPrestadasEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final int pixelcoins;
    @Getter private final int intereses;
    @Getter private final int dias;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), acredor, deudor, Funciones.aumentarPorcentaje(pixelcoins, intereses), "", DEUDAS_PRIMERA_TRANSFERENCIA);
    }
}
