package es.serversurvival.bolsa.ordenespremarket.abrirorden;

import es.jaime.javaddd.domain.exceptions.AlreadyExists;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;

import java.util.UUID;

public final class AbrirOrdenUseCase {
    private final OrdenesPremarketService ordenesPremarketService;

    public AbrirOrdenUseCase() {
        this.ordenesPremarketService = DependecyContainer.get(OrdenesPremarketService.class);
    }

    public void abrirOrden(AbrirOrdenPremarketCommand command) {
        this.ensureOrderNotAlreadyOpen(command.getJugador(), command.getPosicionAbiertaId());

        this.ordenesPremarketService.save(new OrdenPremarket(
                UUID.randomUUID(), command.getJugador(), command.getTicker(), command.getCantidad(),
                command.getTipoAccion(), command.getPosicionAbiertaId()
        ));

        Pixelcoin.publish(new OrdenAbiertaEvento(command.getJugador(), command.getTicker(), command.getCantidad(),
                command.getTipoAccion(), command.getPosicionAbiertaId()));
    }

    private void ensureOrderNotAlreadyOpen(String playerName, UUID idPosicionAbierta){
        if(this.ordenesPremarketService.isOrdenRegisteredFromPosicionAbierta(playerName, idPosicionAbierta))
            throw new AlreadyExists("Ya tienes una orden abierta para esa posicion");
    }
}
