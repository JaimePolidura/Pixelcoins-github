package es.serversurvival.bolsa.posicionesabiertas._shared.application;

import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static es.serversurvival._shared.utils.CollectionUtils.*;
import static es.serversurvival._shared.utils.Funciones.*;
import static java.lang.Math.abs;

@AllArgsConstructor
public final class PosicionesUtils {
    private final PosicionesAbiertasSerivce posicionesAbiertasService;
    private final ActivosInfoService activosInfoService;

    public double getAllPixeloinsEnValores(String jugador) {
        var posLargas = posicionesAbiertasService.findByJugador(jugador, PosicionAbierta::esLargo);
        var posCortas = posicionesAbiertasService.findByJugador(jugador, PosicionAbierta::esCorto);

        Map<String, ActivoInfo> mapActivos = activosInfoService.findAllToMap();

        double pixelcoinsEnLargos =
                getSumaTotalListDouble(posLargas, pos -> mapActivos.get(pos.getNombreActivo()).getPrecio() * pos.getCantidad());
        double pixelcoinsEnCortos =
                getSumaTotalListDouble(posCortas, pos -> (pos.getPrecioApertura() - mapActivos.get(pos.getNombreActivo()).getPrecio()) * pos.getCantidad());

        return pixelcoinsEnLargos + pixelcoinsEnCortos;
    }

    public Map<PosicionAbierta, Integer> getPosicionesAbiertasConPesoJugador(String jugador, double totalInverito) {
        List<PosicionAbierta> posicionAbiertasJugador = posicionesAbiertasService.findByJugador(jugador);

        Map<PosicionAbierta, Integer> posicionesAbiertasConPeso = new HashMap<>();

        for (PosicionAbierta posicion : posicionAbiertasJugador) {
            var nombreActivo = posicion.getNombreActivo();
            var tipoActivo = posicion.getTipoActivo();
            int cantidad = posicion.getCantidad();
            double precioApertura = posicion.getPrecioApertura();

            double valorTotalPosicion = posicion.esLargo() ?
                    cantidad * activosInfoService.getByNombreActivo(nombreActivo, tipoActivo).getPrecio() :
                    (precioApertura - activosInfoService.getByNombreActivo(nombreActivo, tipoActivo).getPrecio()) * cantidad;

            posicionesAbiertasConPeso.put(posicion, (int) rentabilidad(
                    Math.abs(totalInverito), Math.abs(valorTotalPosicion)
            ));
        }

        return posicionesAbiertasConPeso;
    }

    public Map<PosicionAbierta, Double> calcularTopPosicionesAbiertas (String jugador) {
        List<PosicionAbierta> posicionAbiertas = posicionesAbiertasService.findByJugador(jugador, PosicionAbierta::esLargo);
        Map<PosicionAbierta, Double> posicionAbiertasConRentabilidad = new HashMap<>();

        for (PosicionAbierta posicion : posicionAbiertas) {
            double precioInicial = posicion.getPrecioApertura();
            double precioActual = activosInfoService.getByNombreActivo(posicion.getNombreActivo(), posicion.getTipoActivo()).getPrecio();
            double rentabildad = posicion.getTipoPosicion() == TipoPosicion.LARGO ?
                    redondeoDecimales(diferenciaPorcntual(precioInicial, precioActual), 2) :
                    abs(redondeoDecimales(diferenciaPorcntual(precioActual, precioInicial), 2));

            posicionAbiertasConRentabilidad.put(posicion, rentabildad);
        }

        return sortMapByValueDecre(posicionAbiertasConRentabilidad);
    }

    public Map<String, List<PosicionAbierta>> getAllPosicionesAbiertasMap (Predicate<PosicionAbierta> condition) {
        List<PosicionAbierta> posicionAbiertas = posicionesAbiertasService.findAll(condition);

        return mergeMapList(posicionAbiertas, PosicionAbierta::getJugador);
    }
}
