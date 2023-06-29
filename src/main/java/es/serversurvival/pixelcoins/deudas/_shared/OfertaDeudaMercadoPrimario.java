package es.serversurvival.pixelcoins.deudas._shared;

import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
public final class OfertaDeudaMercadoPrimario extends OfertaDeudaMercado {
    @Getter private double interes;
    @Getter private int nCuotasTotales;
    @Getter private long periodoPagoCuotaMs;

    public OfertaDeudaMercadoPrimario(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio,
                                      String objeto, TipoOferta tipoOferta, double interes, int numeroCuotasTotales, long periodoPagoCuota) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta);
        this.interes = interes;
        this.nCuotasTotales = numeroCuotasTotales;
        this.periodoPagoCuotaMs = periodoPagoCuota;
    }

    public static OfertaDeudaMercadoPrimarioBuilder builder() {
        return new OfertaDeudaMercadoPrimarioBuilder();
    }

    public static class OfertaDeudaMercadoPrimarioBuilder extends AbstractOfertaBuilder<OfertaDeudaMercadoPrimarioBuilder> {
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
            return new OfertaDeudaMercadoPrimario(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, TipoOferta.DEUDA_MERCADO_PRIMARIO,
                    interes, numeroCuotasTotales, periodoPagoCuota);
        }
    }
}
