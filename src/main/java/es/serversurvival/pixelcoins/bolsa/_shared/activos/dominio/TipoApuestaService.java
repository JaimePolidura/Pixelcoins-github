package es.serversurvival.pixelcoins.bolsa._shared.activos.dominio;

import java.util.UUID;

public interface TipoApuestaService {
    double getPixelcoinsAbrirPosicion(UUID activoBolsaId, UUID jugadorId, int cantidad);

    double getPixelcoinsCerrarPosicion(UUID posicionId, UUID jugadorId, int cantidad);
    double getPixelcoinsCerrarPosicion(UUID posicionId, int cantidad, double precioActual);

    double calcularRentabilidad(double precioApertura, double precioCierre);
    double calcularBeneficiosOPerdidas(double precioApertura, double precioCierre, int cantidad);
}
