package es.serversurvival.bolsa.posicionesabiertas.venderlargo;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.Pixelcoin;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class VenderLargoUseCase {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final JugadoresService jugadoresService;
    private final ActivosInfoService activoInfoService;
    private final EventBus eventBus;

    public VenderLargoUseCase() {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.activoInfoService = DependecyContainer.get(ActivosInfoService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }
    
    public void venderPosicion(UUID posicionAbiertaIdAVender, int cantidad, String nombreJugador) {
        var posicionAVender = this.posicionesAbiertasSerivce.getById(posicionAbiertaIdAVender);
        this.ensureOwnerOfPosicionAbierta(posicionAVender, nombreJugador);
        this.ensureCantidadCorrectFormat(posicionAVender, cantidad);
        var activoInfo = this.activoInfoService.getByNombreActivo(posicionAVender.getNombreActivo(), posicionAVender.getTipoActivo());
        var vendedor = jugadoresService.getByNombre(posicionAVender.getJugador());

        double precioActual = activoInfo.getPrecio();
        String nombreValor = activoInfo.getNombreActivoLargo();
        String ticker = posicionAVender.getNombreActivo();
        int nAccionesTotlaesEnCartera = posicionAVender.getCantidad();
        double precioApertura = posicionAVender.getPrecioApertura();
        double valorTotalAVender = precioActual * cantidad;
        String fechaApertura = posicionAVender.getFechaApertura();

        if (cantidad == nAccionesTotlaesEnCartera)
            posicionesAbiertasSerivce.deleteById(posicionAVender.getPosicionAbiertaId());
        else
            posicionesAbiertasSerivce.save(posicionAVender.withCantidad(nAccionesTotlaesEnCartera - cantidad));

        this.jugadoresService.save(vendedor.incrementPixelcoinsBy(valorTotalAVender).incrementIngresosBy(valorTotalAVender));

        this.eventBus.publish(new PosicionVentaLargoEvento(nombreJugador, ticker, nombreValor, precioApertura, fechaApertura,
                precioActual, cantidad, posicionAVender.getTipoActivo()));
    }

    private void ensureCantidadCorrectFormat(PosicionAbierta posicionAVender, int cantidad) {
        if(cantidad <= 0 || posicionAVender.getCantidad() < cantidad)
            throw new IllegalQuantity("No puedes vender mas de lo que tiens");
    }

    private void ensureOwnerOfPosicionAbierta(PosicionAbierta posicionAVender, String nombreJugador) {
        if(!posicionAVender.getJugador().equalsIgnoreCase(nombreJugador))
            throw new NotTheOwner("No eres el owner de la posicion abierta");
    }
}
