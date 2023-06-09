package es.serversurvival.pixelcoins.bolsa.ejecutarordenespremarket;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.bolsa.abrir.AbrirPosicionBolsaUseCase;
import es.serversurvival.pixelcoins.bolsa.cerrar.CerrarPosicionUseCase;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.application.OrdenesPremarketService;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.domain.OrdenPremarket;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public final class EjecutadorOrdenPremarket {
    private final OrdenesPremarketService ordenesPremarketService;
    private final UseCaseBus useCaseBus;

    public void ejecutar(OrdenPremarket orden){
        switch (orden.getTipoPosicion()) {
            case ABIERTO -> useCaseBus.handle(orden.toAbrirPosicoinBolsaParametros());
            case CERRADO -> useCaseBus.handle(orden.toCerrarPosicionParametros());
        }

        ordenesPremarketService.deleteById(orden.getOrdenPremarketId());
    }
}
