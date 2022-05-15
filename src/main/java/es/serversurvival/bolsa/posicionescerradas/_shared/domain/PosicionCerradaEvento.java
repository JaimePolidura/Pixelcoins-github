package es.serversurvival.bolsa.posicionescerradas._shared.domain;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.transacciones._shared.domain.EventoTipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class PosicionCerradaEvento extends PixelcoinsEvento implements EventoTipoTransaccion, EventoTipoPosicionCerrada {
    @Getter protected final String vendedor;
    @Getter protected final String ticker;
    @Getter protected final String nombreAcitvo;
    @Getter protected final double precioApertura;
    @Getter protected final String fechaApertura;
    @Getter protected final double precioCierre;
    @Getter protected final int cantidad;
    @Getter protected final SupportedTipoActivo tipoActivo;
}
