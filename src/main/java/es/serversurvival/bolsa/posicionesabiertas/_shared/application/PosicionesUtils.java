package es.serversurvival.bolsa.posicionesabiertas._shared.application;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.LlamadaApi;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.LlamadasApi;
import es.serversurvival.bolsa.other._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static es.serversurvival._shared.mysql.AllMySQLTablesInstances.llamadasApiMySQL;
import static es.serversurvival._shared.utils.Funciones.*;
import static java.lang.Math.abs;

public final class PosicionesUtils {

    private PosicionesUtils () {}

    public static double getAllPixeloinsEnValores(String jugador) {
        var posicionesAbiertasService = DependecyContainer.get(PosicionesAbiertasSerivce.class);

        var posLargas = posicionesAbiertasService.findByJugador(jugador, PosicionAbierta::noEsTipoAccionServerYLargo);
        var posCortas = posicionesAbiertasService.findByJugador(jugador, PosicionAbierta::esCorto);

        Map<String, LlamadaApi> llamadasApiMap = llamadasApiMySQL.getMapOfAllLlamadasApi();

        double pixelcoinsEnLargos =
                getSumaTotalListDouble(posLargas, pos -> llamadasApiMap.get(pos.getNombreActivo()).getPrecio() * pos.getCantidad());
        double pixelcoinsEnCortos =
                getSumaTotalListDouble(posCortas, pos -> (pos.getPrecioApertura() - llamadasApiMap.get(pos.getNombreActivo()).getPrecio()) * pos.getCantidad());

        return pixelcoinsEnLargos + pixelcoinsEnCortos;
    }

     public static Map<PosicionAbierta, Integer> getPosicionesAbiertasConPesoJugador(String jugador, double totalInverito) {
         var posicionesAbiertasService = DependecyContainer.get(PosicionesAbiertasSerivce.class);

        List<PosicionAbierta> posicionAbiertasJugador = posicionesAbiertasService.findByJugador(jugador, PosicionAbierta::noEsTipoAccionServer);

        Map<PosicionAbierta, Integer> posicionesAbiertasConPeso = new HashMap<>();

        posicionAbiertasJugador.forEach( (posicion) -> {
            posicionesAbiertasConPeso.put(posicion, (int)
                    rentabilidad(totalInverito, posicion.getCantidad() * llamadasApiMySQL.getLlamadaAPI(posicion.getNombreActivo()).getPrecio()));
        });

        return posicionesAbiertasConPeso;
    }

    public static Map<PosicionAbierta, Double> calcularTopPosicionesAbiertas (String jugador) {
        var posicionesAbiertasService = DependecyContainer.get(PosicionesAbiertasSerivce.class);

        List<PosicionAbierta> posicionAbiertas = posicionesAbiertasService.findByJugador(jugador, PosicionAbierta::noEsTipoAccionServerYLargo);
        Map<PosicionAbierta, Double> posicionAbiertasConRentabilidad = new HashMap<>();

        for (PosicionAbierta posicion : posicionAbiertas) {
            double precioInicial = posicion.getPrecioApertura();
            double precioActual = LlamadasApi.INSTANCE.getLlamadaAPI(posicion.getNombreActivo()).getPrecio();
            double rentabildad = posicion.getTipoPosicion() == TipoPosicion.LARGO ?
                    redondeoDecimales(diferenciaPorcntual(precioInicial, precioActual), 2) :
                    abs(redondeoDecimales(diferenciaPorcntual(precioActual, precioInicial), 2));

            posicionAbiertasConRentabilidad.put(posicion, rentabildad);
        }

        return sortMapByValueDecre(posicionAbiertasConRentabilidad);
    }


    public static Map<String, List<PosicionAbierta>> getAllPosicionesAbiertasMap (Predicate<PosicionAbierta> condition) {
        var posicionesAbiertasService = DependecyContainer.get(PosicionesAbiertasSerivce.class);

        List<PosicionAbierta> posicionAbiertas = posicionesAbiertasService.findAll(condition);

        return mergeMapList(posicionAbiertas, PosicionAbierta::getJugador);
    }
}
