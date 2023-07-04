package es.serversurvival.pixelcoins.empresas.crear;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class EmpresaCreada extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final Empresa empresa;

    @Override
    public Map<UUID, List<RetoMapping>> retosByJugadorId() {
        return Map.of(empresa.getFundadorJugadorId(), List.of(RetoMapping.EMPRESAS_CREAR));
    }
}
