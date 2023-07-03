package es.serversurvival.pixelcoins.empresas.crear;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.retos._shared.retos.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

import static es.serversurvival.pixelcoins.retos._shared.retos.RetoMapping.EMPRESAS_CREAR;

@AllArgsConstructor
public final class EmpresaCreada extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final Empresa empresa;

    @Override
    public Map<UUID, RetoMapping> retosByJugadorId() {
        return Map.of(empresa.getFundadorJugadorId(), RetoMapping.EMPRESAS_CREAR);
    }
}
