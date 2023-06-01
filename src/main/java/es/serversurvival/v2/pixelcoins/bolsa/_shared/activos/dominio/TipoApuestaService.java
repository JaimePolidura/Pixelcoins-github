package es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio;

import java.util.UUID;

public interface TipoApuestaService {
    double calcularImporteAbrirPosicoins(UUID activoBolsaId, int cantidad);
}
