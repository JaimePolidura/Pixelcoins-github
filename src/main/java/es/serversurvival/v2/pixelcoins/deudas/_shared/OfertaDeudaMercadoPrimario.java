package es.serversurvival.v2.pixelcoins.deudas._shared;

import es.serversurvival.v2.pixelcoins.deudas.emitir.EmitirDeudaUseCaseParametros;
import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

public final class OfertaDeudaMercadoPrimario extends Oferta {
    @Getter private final double interes;
    @Getter private final int numeroCuotasTotales;
    @Getter private final long periodoPagoCuota;

    public OfertaDeudaMercadoPrimario(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio,
                                      String objeto, TipoOferta tipoOferta, double interes, int numeroCuotasTotales, long periodoPagoCuota) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta);
        this.interes = interes;
        this.numeroCuotasTotales = numeroCuotasTotales;
        this.periodoPagoCuota = periodoPagoCuota;
    }

    public static OfertaDeudaMercadoPrimario fromParametosEmitirDeuda(EmitirDeudaUseCaseParametros parametros) {
        return new OfertaDeudaMercadoPrimario(
                UUID.randomUUID(), parametros.getJugadorId(), LocalDateTime.now(), 1, parametros.getNominal(), "", TipoOferta.DEUDA_MERCADO_PRIMARIO,
                parametros.getInteres(), parametros.getNumeroCuotasTotales(), parametros.getPeriodoPagoCuota()
        );
    }
}
