package es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio;

import java.util.UUID;

public interface TipoApuestaService {
    double getPixelcoinsAbrirPosicion(UUID activoBolsaId, int cantidad);
    double getPixelcoinsCerrarPosicion(UUID posicionId, int cantidad);
}
