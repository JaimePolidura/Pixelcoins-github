package es.serversurvival.v1.bolsa.ordenespremarket._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1.bolsa.ordenespremarket.abrirorden.AbrirOrdenPremarketCommand;
import es.serversurvival.v1.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public final class OrderExecutorProxy {
    private final AbrirOrdenUseCase abrirOrdenUseCase;

    public boolean execute(AbrirOrdenPremarketCommand commandoOnMarketClose, Runnable onMarketOpen){
        if(Funciones.mercadoEstaAbierto())
            onMarketOpen.run();
        else
            this.abrirOrdenUseCase.abrirOrden(commandoOnMarketClose);

        return Funciones.mercadoEstaAbierto();
    }
}
