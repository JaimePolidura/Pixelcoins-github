package es.serversurvival.pixelcoins.bolsa._shared.posiciones.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoApuestaService;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.Posicion;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class CortoTipoApuestaService implements TipoApuestaService {
    public static final double COMISION_ABRIR_CORTO = 0.5;

    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final PosicionesService posicionesService;

    @Override
    public double getPixelcoinsAbrirPosicion(UUID activoBolsaId, int cantidad) {
        return activoBolsaUltimosPreciosService.getUltimoPrecio(activoBolsaId) * cantidad * COMISION_ABRIR_CORTO;
    }

    @Override
    public double getPixelcoinsCerrarPosicion(UUID posicionId, int cantidad) {
        Posicion posicion = posicionesService.getById(posicionId);
        double ultimoPrecio = activoBolsaUltimosPreciosService.getUltimoPrecio(posicion.getActivoBolsaId());

        return (posicion.getPrecioApertura() - ultimoPrecio) * cantidad;
    }

    @Override
    public double getPixelcoinsCerrarPosicion(UUID posicionId, int cantidad, double precioActual) {
        Posicion posicion = posicionesService.getById(posicionId);

        return (posicion.getPrecioApertura() - precioActual) * cantidad;
    }

    @Override
    public double calcularRentabilidad(double precioApertura, double precioCierre) {
        return 1 - (precioCierre / precioApertura);
    }

    @Override
    public double calcularBeneficiosOPerdidas(double precioApertura, double precioCierre, int cantidad) {
        return (precioApertura - precioCierre) * cantidad;
    }
}
