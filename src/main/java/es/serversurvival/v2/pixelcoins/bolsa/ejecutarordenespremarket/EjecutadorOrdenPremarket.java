package es.serversurvival.v2.pixelcoins.bolsa.ejecutarordenespremarket;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v1.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;
import es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.application.OrdenesPremarketService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.domain.OrdenPremarket;
import es.serversurvival.v2.pixelcoins.bolsa.abrir.AbrirPosicionBolsaUseCase;
import es.serversurvival.v2.pixelcoins.bolsa.cerrar.CerrarPosicionUseCase;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public final class EjecutadorOrdenPremarket {
    private final AbrirPosicionBolsaUseCase abrirPosicionBolsaUseCase;
    private final OrdenesPremarketService ordenesPremarketService;
    private final CerrarPosicionUseCase cerrarPosicionUseCase;

    public void ejecutar(OrdenPremarket orden){
        switch (orden.getTipoPosicion()) {
            case ABIERTO -> abrirPosicionBolsaUseCase.abrir(orden.toAbrirPosicoinBolsaParametros());
            case CERRADO -> cerrarPosicionUseCase.cerrar(orden.toCerrarPosicionParametros());
        }

        ordenesPremarketService.deleteById(orden.getOrdenPremarketId());
    }
}
