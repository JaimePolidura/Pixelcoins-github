package es.serversurvival.bolsa.posicionesabiertas.comprarlargo;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;

public final class ComprarLargoUseCase implements AllMySQLTablesInstances {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final JugadoresService jugadoresService;

    public ComprarLargoUseCase() {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public void comprarLargo(TipoActivo tipoActivo, String nombreActivo, int cantidad, String jugadorNombre) {
        Jugador jugador = jugadoresService.getByNombre(jugadorNombre);
        double precioUnidad = this.getPrice(tipoActivo, nombreActivo);

        double totalPrice = precioUnidad * cantidad;

        this.posicionesAbiertasSerivce.save(jugadorNombre, tipoActivo, nombreActivo, cantidad, precioUnidad, TipoPosicion.LARGO);
        this.jugadoresService.save(jugador.decrementPixelcoinsBy(totalPrice));

        Pixelcoin.publish(new PosicionCompraLargoEvento(jugadorNombre, precioUnidad, cantidad, cantidad*precioUnidad, nombreActivo, tipoActivo));
    }

    private double getPrice(TipoActivo tipoActivo, String nombreActivo){
        var llamdaApi = llamadasApiMySQL.getLlamadaAPI(nombreActivo);
        var precio = llamdaApi == null ?
                tipoActivo.getPrecio(nombreActivo) :
                llamdaApi.getPrecio();

        if(precio == -1)
            throw new ResourceNotFound("Valor no econtrado, solo se puden comprar valores en la bolsa americana");

        return precio;
    }
}
