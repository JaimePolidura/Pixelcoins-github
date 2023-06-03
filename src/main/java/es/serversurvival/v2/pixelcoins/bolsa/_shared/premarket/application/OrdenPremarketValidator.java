package es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.PosicionesService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.domain.OrdenPremarket;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class OrdenPremarketValidator {
    private final OrdenesPremarketService ordenesPremarketService;
    private final PosicionesService posicionesService;

    public void ordenesCerrarCantidadNoMayorPosicion(UUID posicionId, int cantidadNuevaACerrar) {
        int cantidadEnOdenesPremarket = ordenesPremarketService.findByPosicionAbiertaId(posicionId).stream()
                .mapToInt(OrdenPremarket::getCantidad)
                .sum();
        int cantidadEnPosicion = posicionesService.getById(posicionId).getCantidad();

        if(cantidadEnOdenesPremarket + cantidadNuevaACerrar > cantidadEnPosicion){
            throw new IllegalQuantity("No puedes poner una orden premarket para vender mas de lo que tieens");
        }
    }
}
