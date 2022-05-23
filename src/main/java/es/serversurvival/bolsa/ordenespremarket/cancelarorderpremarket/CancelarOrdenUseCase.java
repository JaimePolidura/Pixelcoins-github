package es.serversurvival.bolsa.ordenespremarket.cancelarorderpremarket;

import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public final class CancelarOrdenUseCase {
    private final OrdenesPremarketService ordenesPremarketService;

    public CancelarOrdenUseCase(){
        this.ordenesPremarketService = DependecyContainer.get(OrdenesPremarketService .class);
    }

    public void cancelar (String jugador, UUID id) {
        var ordenPremarket = this.ordenesPremarketService.getById(id);
        this.ensureOwnerOfPosicionAbierta(jugador, ordenPremarket);

        this.ordenesPremarketService.deleteById(id);
    }

    private void ensureOwnerOfPosicionAbierta(String jugador, OrdenPremarket ordenPremarket) {
        if(!ordenPremarket.getJugador().equalsIgnoreCase(jugador))
            throw new NotTheOwner("No eres el owner de la orden");
    }
}
