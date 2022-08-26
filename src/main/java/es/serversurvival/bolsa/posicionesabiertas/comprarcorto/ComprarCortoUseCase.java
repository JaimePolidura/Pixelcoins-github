package es.serversurvival.bolsa.posicionesabiertas.comprarcorto;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaimetruman.annotations.UseCase;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo.ACCIONES;

@AllArgsConstructor
@UseCase
public class ComprarCortoUseCase {
    private final JugadoresService jugadoresService;
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final ActivosInfoService activoInfoService;
    private final EventBus eventBus;

    public ComprarCortoUseCase() {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.activoInfoService = DependecyContainer.get(ActivosInfoService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void comprarPosicionCorto (UUID posicoinAbiertaIdComprarCorto, int cantidad, String jugadorNombre) {
        var poscionAComprarCorto = this.posicionesAbiertasSerivce.getById(posicoinAbiertaIdComprarCorto);
        this.ensureJugadorOwnerOfPosicion(poscionAComprarCorto, jugadorNombre);
        this.ensureCantidadCorrectFormat(poscionAComprarCorto, cantidad);

        ActivoInfo activoInfo = this.activoInfoService.getByNombreActivo(poscionAComprarCorto.getNombreActivo(),
                poscionAComprarCorto.getTipoActivo());
        String ticker = poscionAComprarCorto.getNombreActivo();
        Jugador jugador = this.jugadoresService.getByNombre(poscionAComprarCorto.getJugador());
        double precioApertura = poscionAComprarCorto.getPrecioApertura();
        double revalorizacionTotal = (poscionAComprarCorto.getPrecioApertura() - activoInfo.getPrecio()) * cantidad;
        String fechaApertura = poscionAComprarCorto.getFechaApertura();
        double pixelcoinsJugador = jugador.getPixelcoins();

        this.modifyPosicionAbierta(poscionAComprarCorto, cantidad);
        this.addPixelcoinsToJugaodor(jugador, revalorizacionTotal, pixelcoinsJugador);

        this.eventBus.publish(new PosicionCompraCortoEvento(poscionAComprarCorto.getJugador(), ticker, activoInfo.getNombreActivoLargo(),
                precioApertura, fechaApertura, activoInfo.getPrecio(), cantidad, ACCIONES));
    }

    private void ensureCantidadCorrectFormat(PosicionAbierta posicionAbierta, int cantidad){
        if(cantidad <= 0 || cantidad > posicionAbierta.getCantidad())
            throw new IllegalQuantity("La cantidad a comprar en corto tiene que ser mayor que 0 y menor o igual que la cantidad de la posicion");
    }

    private void ensureJugadorOwnerOfPosicion(PosicionAbierta posicionAbierta, String jugadorNombre){
        if(!posicionAbierta.getJugador().equalsIgnoreCase(jugadorNombre))
            throw new NotTheOwner("No tinenes esa posicion en cartera");
    }

    private void addPixelcoinsToJugaodor(Jugador jugador, double revalorizacionTotal, double pixelcoinsJugador) {
        jugadoresService.save(jugador.incrementPixelcoinsBy(revalorizacionTotal).incrementGastosBy(revalorizacionTotal));
    }

    private void modifyPosicionAbierta(PosicionAbierta posicionAComprar, int cantidadAComprar) {
        if (cantidadAComprar == posicionAComprar.getCantidad())
            posicionesAbiertasSerivce.deleteById(posicionAComprar.getPosicionAbiertaId());
        else
            posicionesAbiertasSerivce.save(posicionAComprar.withCantidad(posicionAComprar.getCantidad() - cantidadAComprar));
    }
}
