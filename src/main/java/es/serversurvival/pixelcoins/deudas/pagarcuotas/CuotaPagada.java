package es.serversurvival.pixelcoins.deudas.pagarcuotas;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class CuotaPagada extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID deudaId;
    @Getter private final double cuota;
    @Getter private final UUID deudorJugadorId;
    @Getter private final UUID acredorJugadorId;
    @Getter private final boolean estaPendiente;
    @Getter private final int nInpagos;

    @Override
    public Map<UUID, RetoMapping> retosByJugadorId() {
        if(estaPendiente){
            return Map.of(acredorJugadorId, RetoMapping.DEUDAS_PRESTAR_COBRO_CUOTAS);
        }else if(estaPendiente && nInpagos == 0){
            return Map.of(deudorJugadorId, RetoMapping.DEUDAS_PAGADA_ENTERA_SIN_NINPAGOS);
        }

        return null;
    }
}
