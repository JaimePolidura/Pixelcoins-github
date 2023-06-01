package es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class CortoTipoApuestaService implements TipoApuestaService {
    public static final double COMISION_ABRIR_CORTO = 0.3;

    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;

    @Override
    public double calcularImporteAbrirPosicoins(UUID activoBolsaId, int cantidad) {
        return (activoBolsaUltimosPreciosService.getUltimoPrecio(activoBolsaId) * cantidad) * COMISION_ABRIR_CORTO;
    }
}
