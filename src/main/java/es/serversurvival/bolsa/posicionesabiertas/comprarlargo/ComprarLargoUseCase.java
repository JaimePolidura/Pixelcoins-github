package es.serversurvival.bolsa.posicionesabiertas.comprarlargo;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbiertaEvento;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;

import static es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion.*;

public class ComprarLargoUseCase {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final JugadoresService jugadoresService;
    private final ActivosInfoService activoInfoService;

    public ComprarLargoUseCase() {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.activoInfoService = DependecyContainer.get(ActivosInfoService.class);
    }

    public void comprarLargo(SupportedTipoActivo tipoActivo, String nombreActivo, int cantidad, String jugadorNombre) {
        Jugador jugador = jugadoresService.getByNombre(jugadorNombre);
        double precioUnidad = this.getPrice(tipoActivo, nombreActivo);

        double totalPrice = precioUnidad * cantidad;

        this.posicionesAbiertasSerivce.save(jugadorNombre, tipoActivo, nombreActivo, cantidad, precioUnidad, LARGO);
        this.jugadoresService.save(jugador.decrementPixelcoinsBy(totalPrice));

        Pixelcoin.publish(PosicionAbiertaEvento.of(jugadorNombre, nombreActivo, cantidad, precioUnidad, tipoActivo,
                precioUnidad * cantidad, nombreActivo, LARGO));
    }

    private double getPrice(SupportedTipoActivo tipoActivo, String nombreActivo){
        var activoInfo = activoInfoService.getByNombreActivo(nombreActivo, tipoActivo);

        if(activoInfo.getPrecio() == -1)
            throw new ResourceNotFound("Valor no econtrado, solo se puden comprar valores en la bolsa americana");

        return activoInfo.getPrecio();
    }
}
