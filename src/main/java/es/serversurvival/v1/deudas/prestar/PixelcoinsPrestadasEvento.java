package es.serversurvival.v1.deudas.prestar;

import es.serversurvival.v1.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v1.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

import static es.serversurvival.v1._shared.utils.Funciones.*;
import static es.serversurvival.v1.transacciones._shared.domain.TipoTransaccion.*;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class PixelcoinsPrestadasEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final double pixelcoins;
    @Getter private final int intereses;
    @Getter private final int dias;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), acredor, deudor, aumentarPorcentaje(pixelcoins, intereses), "",
                DEUDAS_PRIMERA_TRANSFERENCIA);
    }

    public static PixelcoinsPrestadasEvento of(String acredor, String deudor, double pixelcoins, int intereses, int dias){
        return new PixelcoinsPrestadasEvento(acredor, deudor, pixelcoins, intereses, dias);
    }
}
