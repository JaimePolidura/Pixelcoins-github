package es.serversurvival.pixelcoins.bolsa._shared.posiciones.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoApuestaService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class LargoTipoApuestaService implements TipoApuestaService {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final PosicionesService posicionesService;

    @Override
    public double getPixelcoinsAbrirPosicion(UUID activoBolsaId, UUID jugadorId, int cantidad) {
        return activoBolsaUltimosPreciosService.getUltimoPrecio(activoBolsaId, jugadorId) * cantidad;
    }

    @Override
    public double getPixelcoinsCerrarPosicion(UUID posicionId, UUID jugadorId, int cantidad) {
        return activoBolsaUltimosPreciosService.getUltimoPrecio(posicionesService.getById(posicionId).getActivoBolsaId(), jugadorId) * cantidad;
    }

    @Override
    public double getPixelcoinsCerrarPosicion(UUID posicionId, int cantidad, double precioActual) {
        return precioActual * cantidad;
    }

    @Override
    public double calcularRentabilidad(double precioApertura, double precioCierre) {
        return (precioCierre / precioApertura) - 1;
    }

    @Override
    public double calcularBeneficiosOPerdidas(double precioApertura, double precioCierre, int cantidad) {
        return (precioCierre - precioApertura) * cantidad;
    }
}
