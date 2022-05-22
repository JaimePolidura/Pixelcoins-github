package es.serversurvival.deudas.prestar;

import es.serversurvival.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.transacciones._shared.domain.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.*;

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
