package es.serversurvival.bolsa.ordenespremarket._shared.application;

import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenPremarketCommand;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;

public final class OrderExecutorProxy {
    private static final OrderExecutorProxy INSTANCE = new OrderExecutorProxy();
    private final AbrirOrdenUseCase abrirOrdenUseCase;

    public OrderExecutorProxy() {
        this.abrirOrdenUseCase = new AbrirOrdenUseCase();
    }

    public void run(AbrirOrdenPremarketCommand commandoOnMarketClose, Runnable onMarketOpen){
        if(Funciones.mercadoEstaAbierto())
            onMarketOpen.run();
        else
            this.abrirOrdenUseCase.abrirOrden(commandoOnMarketClose);
    }

    public static void execute(AbrirOrdenPremarketCommand commandoOnMarketClose, Runnable onMarketOpen){
        INSTANCE.run(commandoOnMarketClose, onMarketOpen);
    }
}
