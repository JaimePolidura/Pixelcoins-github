package es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.TipoApuestaService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class LargoTipoApuestaService implements TipoApuestaService {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final PosicionesService posicionesService;

    @Override
    public double getPixelcoinsAbrirPosicion(UUID activoBolsaId, int cantidad) {
        return activoBolsaUltimosPreciosService.getUltimoPrecio(activoBolsaId) * cantidad;
    }

    @Override
    public double getPixelcoinsCerrarPosicion(UUID posicionId, int cantidad) {
        return activoBolsaUltimosPreciosService.getUltimoPrecio(posicionesService.getById(posicionId).getActivoBolsaId()) * cantidad;
    }
}
