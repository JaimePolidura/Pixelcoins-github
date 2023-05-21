package es.serversurvival.v2.pixelcoins.deudas._shared;

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

    public static OfertaDeudaMercadoPrimarioBuilder builder() {
        return new OfertaDeudaMercadoPrimarioBuilder();
    }

    public static class OfertaDeudaMercadoPrimarioBuilder extends Oferta.AbstractOfertaBuilder<OfertaDeudaMercadoPrimarioBuilder> {
        private double interes;
        private int numeroCuotasTotales;
        private long periodoPagoCuota;

        public OfertaDeudaMercadoPrimarioBuilder() {
            this.ofertaId = UUID.randomUUID();
            this.fechaSubida = LocalDateTime.now();
            this.cantidad = 1;
            this.objeto = "";
            this.tipoOferta = TipoOferta.DEUDA_MERCADO_PRIMARIO;
        }

        public OfertaDeudaMercadoPrimarioBuilder interes(double interes) {
            this.interes = interes;
            return this;
        }

        public OfertaDeudaMercadoPrimarioBuilder periodoPagoCuota(long periodoPagoCuota) {
            this.periodoPagoCuota = periodoPagoCuota;
            return this;
        }


        public OfertaDeudaMercadoPrimarioBuilder numeroCuotasTotales(int numeroCuotasTotales) {
            this.numeroCuotasTotales = numeroCuotasTotales;
            return this;
        }

        @Override
        public OfertaDeudaMercadoPrimario build() {
            return new OfertaDeudaMercadoPrimario(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta,
                    interes, numeroCuotasTotales, periodoPagoCuota);
        }
    }
}
