package es.serversurvival.v2.pixelcoins.deudas.prestar;

import es.serversurvival.v2.pixelcoins.deudas._shared.OfertaDeudaMercadoPrimario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class PrestarDeudaParametros {
    @Getter private final UUID acredorJugadorId;
    @Getter private final UUID deudorJugadorId;
    @Getter private final double nominal;
    @Getter private final double interes;
    @Getter private final int numeroCuotasTotales;
    @Getter private final long periodoPagoCuita;

    public static PrestarDeudaParametros fromOfertaDeudaMercadoPrimario(OfertaDeudaMercadoPrimario oferta, UUID compradorId) {
        return new PrestarDeudaParametros(
                compradorId,
                oferta.getVendedorId(),
                oferta.getPrecio(),
                oferta.getInteres(),
                oferta.getNumeroCuotasTotales(),
                oferta.getPeriodoPagoCuota()
        );
    }
}
