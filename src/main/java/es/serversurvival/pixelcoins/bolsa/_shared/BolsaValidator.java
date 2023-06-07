package es.serversurvival.pixelcoins.bolsa._shared;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.IllegalState;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoApuestaService;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.PosicionesService;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.TipoBolsaApuesta;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.TipoPosicion;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.application.OrdenesPremarketService;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.domain.OrdenPremarket;
import es.serversurvival.pixelcoins.bolsa.abrir.AbrirPosicoinBolsaParametros;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class BolsaValidator {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final OrdenesPremarketService ordenesPremarketService;
    private final DependenciesRepository dependenciesRepository;
    private final TransaccionesService transaccionesService;
    private final ActivosBolsaService activosBolsaService;
    private final PosicionesService posicionesService;
    private final Validador validador;

    public void posicionAbierta(UUID posicionAbiertaId) {
        if(posicionesService.getById(posicionAbiertaId).getTipoPosicion() != TipoPosicion.ABIERTO) {
            throw new IllegalState("La posicion esta cerrada");
        }
    }

    public void posicionTieneCantidad(UUID posicionId, int cantidad) {
        if(posicionesService.getById(posicionId).getCantidad() < cantidad){
            throw new IllegalQuantity("Esa posicion no tiene esa cantidad");
        }
    }

    public void jugaodrTienePosicion(UUID posicionId, UUID jugadorId) {
        if(!posicionesService.getById(posicionId).getJugadorId().equals(jugadorId)){
            throw new NotTheOwner("No tienes esa posicion");
        }
    }

    public void suficientesPixelcoinsAbrir(AbrirPosicoinBolsaParametros parametros) {
        double precioBolsaTotal = getPixelcoinsAbrirPosicion(parametros.getActivoBolsaId(), parametros.getCantidad(),
                parametros.getTipoApuesta());
        double pixelcoinsJugador = transaccionesService.getBalancePixelcions(parametros.getJugadorId());

        if(precioBolsaTotal > pixelcoinsJugador){
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins");
        }
    }

    public void cantidadCorrecta(int cantidad) {
        validador.numeroMayorQueCero(cantidad, "La cantidad");
    }

    public void activoBolsaExsiste(UUID activoBolsaId) {
        activosBolsaService.getById(activoBolsaId);
    }

    public double getPixelcoinsAbrirPosicion(UUID activoBolsaId, int cantidad, TipoBolsaApuesta tipoBolsaApuesta) {
        TipoApuestaService tipoApuestaService = dependenciesRepository.get(tipoBolsaApuesta.getTipoApuestaService());
        return tipoApuestaService.getPixelcoinsAbrirPosicion(activoBolsaId, cantidad);
    }

    public void jugadorTieneOrden(UUID ordenPremarketId, UUID jugadorId) {
        if(!ordenesPremarketService.getById(ordenPremarketId).getJugadorId().equals(jugadorId)){
            throw new NotTheOwner("La orden premarket no te pertenece");
        }
    }

    public void ordenesCerrarCantidadNoMayorPosicion(UUID posicionId, int cantidadNuevaACerrar) {
        int cantidadEnOdenesPremarket = ordenesPremarketService.findByPosicionAbiertaId(posicionId).stream()
                .mapToInt(OrdenPremarket::getCantidad)
                .sum();
        int cantidadEnPosicion = posicionesService.getById(posicionId).getCantidad();

        if(cantidadEnOdenesPremarket + cantidadNuevaACerrar > cantidadEnPosicion){
            throw new IllegalQuantity("No puedes poner una orden premarket para vender mas de lo que tieens");
        }
    }
}
