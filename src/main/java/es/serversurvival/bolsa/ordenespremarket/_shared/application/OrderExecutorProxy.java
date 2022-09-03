package es.serversurvival.bolsa.ordenespremarket._shared.application;

import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenPremarketCommand;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;

import javax.imageio.stream.FileImageInputStream;

public final class OrderExecutorProxy {
    private static final OrderExecutorProxy INSTANCE = new OrderExecutorProxy();
    private final AbrirOrdenUseCase abrirOrdenUseCase;

    public boolean run(AbrirOrdenPremarketCommand commandoOnMarketClose, Runnable onMarketOpen){
        if(Funciones.mercadoEstaAbierto())
            onMarketOpen.run();
        else
            this.abrirOrdenUseCase.abrirOrden(commandoOnMarketClose);

        return Funciones.mercadoEstaAbierto();
    }

    public static boolean execute(AbrirOrdenPremarketCommand commandoOnMarketClose, Runnable onMarketOpen){
        return INSTANCE.run(commandoOnMarketClose, onMarketOpen);
    }
}
