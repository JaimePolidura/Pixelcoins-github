package es.serversurvival.bolsa.posicionesabiertas.venderlargo;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.Pixelcoin;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;

public final class VenderLargoUseCase implements AllMySQLTablesInstances {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final JugadoresService jugadoresService;

    public VenderLargoUseCase() {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }
    
    public void venderPosicion(PosicionAbierta posicionAVender, int cantidad, String nombreJugador) {
        double precioActual = llamadasApiMySQL.getLlamadaAPI(posicionAVender.getNombreActivo()).getPrecio();
        Jugador vendedor = jugadoresService.getByNombre(posicionAVender.getJugador());
        String ticker = posicionAVender.getNombreActivo();
        int nAccionesTotlaesEnCartera = posicionAVender.getCantidad();
        double precioApertura = posicionAVender.getPrecioApertura();
        double beneficiosPerdidas = (precioActual - precioApertura) * cantidad;
        double valorTotalAVender = precioActual * cantidad;
        String fechaApertura = posicionAVender.getFechaApertura();
        String nombreValor = llamadasApiMySQL.getLlamadaAPI(ticker).getNombreActivoLargo();

        if (cantidad == nAccionesTotlaesEnCartera)
            posicionesAbiertasSerivce.deleteById(posicionAVender.getPosicionAbiertaId());
        else
            posicionesAbiertasSerivce.save(posicionAVender.withCantidad(nAccionesTotlaesEnCartera - cantidad));

        if(beneficiosPerdidas >= 0)
            this.jugadoresService.save(vendedor.incrementPixelcoinsBy(valorTotalAVender).incrementIngresosBy(beneficiosPerdidas));
        else
            this.jugadoresService.save(vendedor.incrementPixelcoinsBy(valorTotalAVender).incrementGastosBy(beneficiosPerdidas));


        Pixelcoin.publish(new PosicionVentaLargoEvento(nombreJugador, ticker, nombreValor, precioApertura, fechaApertura,
                precioActual, cantidad, posicionAVender.getTipoActivo()));
    }
}
