package es.serversurvival.bolsa.posicionesabiertas.vendercorto;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbiertaEvento;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo.*;
import static es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce.PORCENTAJE_CORTO;
import static es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion.*;

@AllArgsConstructor
public class VenderCortoUseCase {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final JugadoresService jugadoresService;
    private final ActivosInfoService activosInfoService;
    private final EventBus eventBus;

    public VenderCortoUseCase () {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.jugadoresService= DependecyContainer.get(JugadoresService.class);
        this.activosInfoService = DependecyContainer.get(ActivosInfoService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void venderEnCortoBolsa (String jugadorNombre, String nombreActivo, int cantidad) {
        var activoInfo = this.activosInfoService.getByNombreActivo(nombreActivo, ACCIONES);
        this.ensureActivoInfoNotNull(activoInfo);
        var jugador = this.jugadoresService.getByNombre(jugadorNombre);
        double valorTotal = activoInfo.getPrecio() * cantidad;
        double comision = redondeoDecimales(reducirPorcentaje(valorTotal, 100 - PORCENTAJE_CORTO), 2);
        this.ensureEnoughPixelcoins(jugador, comision);

        this.posicionesAbiertasSerivce.save(jugadorNombre, ACCIONES, nombreActivo, cantidad, activoInfo.getPrecio(), CORTO);

        this.jugadoresService.save(jugador.decrementPixelcoinsBy(comision).incrementGastosBy(comision));

        this.eventBus.publish(PosicionAbiertaEvento.of(jugadorNombre, nombreActivo, cantidad, activoInfo.getPrecio(), ACCIONES,
                comision, activoInfo.getNombreActivoLargo(), CORTO));
    }

    private void ensureEnoughPixelcoins(Jugador jugador, double comision) {
        if(jugador.getPixelcoins() < comision)
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins para vender en corto");
    }

    private void ensureActivoInfoNotNull(ActivoInfo activoInfo) {
        if(activoInfo == null)
            throw new ResourceNotFound("Accion no encontrada, recuerdo que solo se pueden cantidad que cotizen en la bolsa americana");
    }
}
