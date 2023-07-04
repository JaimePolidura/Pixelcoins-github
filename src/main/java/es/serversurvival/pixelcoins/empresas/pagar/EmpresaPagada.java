package es.serversurvival.pixelcoins.empresas.pagar;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class EmpresaPagada extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID empresaId;
    @Getter private final UUID empresaDirectorJugadorId;
    @Getter private final UUID jugadorPagadorId;
    @Getter private final double pixelcoins;

    @Override
    public Map<UUID, List<RetoMapping>> retosByJugadorId() {
        return Map.of(
                empresaDirectorJugadorId, List.of(RetoMapping.EMPRESAS_PAGAR_PAGADO),
                jugadorPagadorId, List.of(RetoMapping.EMPRESAS_PAGAR_PAGADOR)
        );
    }

    @Override
    public Object otroDatoReto() {
        return this.empresaId;
    }
}
