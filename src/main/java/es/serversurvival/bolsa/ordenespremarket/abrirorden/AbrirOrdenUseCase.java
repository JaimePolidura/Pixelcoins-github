package es.serversurvival.bolsa.ordenespremarket.abrirorden;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.AlreadyExists;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public final class AbrirOrdenUseCase {
    private final OrdenesPremarketService ordenesPremarketService;
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final EventBus eventBus;

    public AbrirOrdenUseCase() {
        this.ordenesPremarketService = DependecyContainer.get(OrdenesPremarketService.class);
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void abrirOrden(AbrirOrdenPremarketCommand command) {
        this.ensureOwnsThatPosicion(command.getJugador(), command.getPosicionAbiertaId());
        this.ensureOrderNotAlreadyOpen(command.getJugador(), command.getPosicionAbiertaId());

        this.ordenesPremarketService.save(command.getJugador(), command.getTicker(), command.getCantidad(),
                command.getTipoAccion(), command.getPosicionAbiertaId());

        this.eventBus.publish(new OrdenAbiertaEvento(command.getJugador(), command.getTicker(), command.getCantidad(),
                command.getTipoAccion(), command.getPosicionAbiertaId()));
    }

    private void ensureOwnsThatPosicion(String playerName, UUID posicionAbiertaId){
        var posicionAbierta = this.posicionesAbiertasSerivce.getById(posicionAbiertaId);

        if(!posicionAbierta.getJugador().equalsIgnoreCase(playerName))
            throw new NotTheOwner("No eres el owner de la posicion abierta");
    }

    private void ensureOrderNotAlreadyOpen(String playerName, UUID idPosicionAbierta){
        if(this.ordenesPremarketService.isOrdenRegisteredFromPosicionAbierta(playerName, idPosicionAbierta))
            throw new AlreadyExists("Ya tienes una orden abierta para esa posicion");
    }
}
