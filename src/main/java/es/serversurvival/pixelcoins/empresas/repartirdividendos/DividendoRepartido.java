package es.serversurvival.pixelcoins.empresas.repartirdividendos;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class DividendoRepartido extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID accionistaJugadorId;
    @Getter private final UUID directorEmpresaId;
    @Getter private final UUID empresaId;
    @Getter private final double dividendoPorAccion;
    @Getter private final int nAcciones;

    @Override
    public Map<UUID, List<RetoMapping>> retosByJugadorId() {
        if(directorEmpresaId.equals(accionistaJugadorId)){
            return Map.of(accionistaJugadorId, List.of(RetoMapping.EMPRESAS_ACCIONISTAS_RECIBIR_DIVIDENDO));
        }

        return null;
    }
}
