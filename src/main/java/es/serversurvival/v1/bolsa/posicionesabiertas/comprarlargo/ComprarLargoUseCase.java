package es.serversurvival.v1.bolsa.posicionesabiertas.comprarlargo;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v1.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.v1.bolsa.posicionesabiertas._shared.domain.PosicionAbiertaEvento;
import es.serversurvival.v1.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import es.serversurvival.v1.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class ComprarLargoUseCase {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final JugadoresService jugadoresService;
    private final ActivosInfoService activoInfoService;
    private final EventBus eventBus;

    public void comprarLargo(String jugadorNombre, TipoActivo tipoActivo, String nombreActivo, int cantidad) {
        var activoToComprar = this.activoInfoService.getByNombreActivo(nombreActivo, tipoActivo);
        this.ensureActivoInfoNotNull(activoToComprar);
        var jugador = jugadoresService.getByNombre(jugadorNombre);
        double totalPrice = activoToComprar.getPrecio() * cantidad;
        double precioUnidad = activoToComprar.getPrecio();
        this.ensureEnoughPixelcoins(jugador, totalPrice);

        this.posicionesAbiertasSerivce.save(jugadorNombre, activoToComprar.getTipoActivo(), activoToComprar.getNombreActivo(), cantidad,
                precioUnidad, TipoPosicion.LARGO);
        this.jugadoresService.save(jugador.decrementPixelcoinsBy(totalPrice).incrementGastosBy(totalPrice));

        this.eventBus.publish(PosicionAbiertaEvento.of(jugadorNombre, activoToComprar.getNombreActivo(), cantidad, precioUnidad, activoToComprar.getTipoActivo(),
                precioUnidad * cantidad, activoToComprar.getNombreActivoLargo(), TipoPosicion.LARGO));
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
