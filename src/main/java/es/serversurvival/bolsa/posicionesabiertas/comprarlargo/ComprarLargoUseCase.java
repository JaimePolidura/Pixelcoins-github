package es.serversurvival.bolsa.posicionesabiertas.comprarlargo;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeNull;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbiertaEvento;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;

import static es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion.*;

@AllArgsConstructor
public class ComprarLargoUseCase {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final JugadoresService jugadoresService;
    private final ActivosInfoService activoInfoService;
    private final EventBus eventBus;

    public ComprarLargoUseCase() {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.activoInfoService = DependecyContainer.get(ActivosInfoService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void comprarLargo(String jugadorNombre, SupportedTipoActivo tipoActivo, String nombreActivo, int cantidad) {
        var activoToComprar = this.activoInfoService.getByNombreActivo(nombreActivo,tipoActivo);
        this.ensureActivoInfoNotNull(activoToComprar);
        var jugador = jugadoresService.getByNombre(jugadorNombre);
        double totalPrice = activoToComprar.getPrecio() * cantidad;
        double precioUnidad = activoToComprar.getPrecio();
        this.ensureEnoughPixelcoins(jugador, totalPrice);

        this.posicionesAbiertasSerivce.save(jugadorNombre, activoToComprar.getTipoActivo(), activoToComprar.getNombreActivo(), cantidad,
                precioUnidad, LARGO);
        this.jugadoresService.save(jugador.decrementPixelcoinsBy(totalPrice).incrementGastosBy(totalPrice));

        this.eventBus.publish(PosicionAbiertaEvento.of(jugadorNombre, activoToComprar.getNombreActivo(), cantidad, precioUnidad, activoToComprar.getTipoActivo(),
                precioUnidad * cantidad, activoToComprar.getNombreActivoLargo(), LARGO));
    }

    private void ensureEnoughPixelcoins(Jugador jugador, double precioTotal){
        if(jugador.getPixelcoins() < precioTotal)
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins para realizar el pago");
    }

    private void ensureActivoInfoNotNull(ActivoInfo activoInfo){
        if(activoInfo == null)
            throw new ResourceNotFound("Activo a comprar no encontrado, recuerda que solo se pueden en la bolsa americana");
    }
}
