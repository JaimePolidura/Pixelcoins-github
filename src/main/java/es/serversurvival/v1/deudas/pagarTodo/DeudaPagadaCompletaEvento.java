package es.serversurvival.v1.deudas.pagarTodo;

import es.serversurvival.v1.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v1.transacciones._shared.domain.Transaccion;
import es.serversurvival.v1.transacciones._shared.domain.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class DeudaPagadaCompletaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), deudor, acredor, (int) pixelcoins, "", TipoTransaccion.DEUDAS_PAGAR_TODADEUDA);
    }

    public static DeudaPagadaCompletaEvento of(String acredor, String deudor, double pixelcoins){
        return new DeudaPagadaCompletaEvento(acredor, deudor, pixelcoins);
    }
}
