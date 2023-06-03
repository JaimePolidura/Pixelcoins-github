package es.serversurvival.v2.pixelcoins.bolsa.cerrar;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.bolsa._shared.BolsaValidator;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.Posicion;
import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.PosicionesService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.application.AbridorOrdenesPremarket;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class CerrarPosicionUseCase {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final AbridorOrdenesPremarket abridorOrdenesPremarket;
    private final DependenciesRepository dependenciesRepository;
    private final TransaccionesService transaccionesService;
    private final ActivosBolsaService activosBolsaService;
    private final PosicionesService posicionesService;
    private final BolsaValidator validator;
    private final EventBus eventBus;

    public void cerrar(CerrarPosicionParametros parametros) {
        validator.jugaodrTienePosicion(parametros.getPosicionAbiertaId(), parametros.getJugadorId());
        validator.posicionTieneCantidad(parametros.getPosicionAbiertaId(), parametros.getCantidad());
        validator.posicionAbierta(parametros.getPosicionAbiertaId());

        if(!abridorOrdenesPremarket.estaElMercadoAbierto()){
            abridorOrdenesPremarket.abrirOrdenCerrar(parametros.toAbrirOrdenPremarketCerrarParametros());
            return;
        }

        Posicion posicionAbierta = posicionesService.getById(parametros.getPosicionAbiertaId());
        double precioPorUnidad = activoBolsaUltimosPreciosService.getUltimoPrecio(posicionAbierta.getActivoBolsaId());
        double valorPosicion = getValorPosicion(parametros, posicionAbierta);

        Posicion posicionCerrada = posicionAbierta.cerrar(parametros.getCantidad(), precioPorUnidad);
        posicionAbierta = posicionAbierta.decrementarCantidad(parametros.getCantidad());

        posicionesService.savePosicionAbiertaConNuevaCantidad(posicionAbierta);
        posicionesService.save(posicionCerrada);
        activosBolsaService.decrementarNReferencias(posicionCerrada.getActivoBolsaId());
        transaccionesService.save(Transaccion.builder()
                .pagadoId(parametros.getJugadorId())
                .tipo(posicionCerrada.getTipoApuesta().getTipoTransaccionCerrar())
                .pixelcoins(valorPosicion)
                .objeto(posicionCerrada.getActivoBolsaId())
                .build());

        eventBus.publish(new PosicionBolsaCerrada(posicionCerrada.getPosicionId()));
    }

    private double getValorPosicion(CerrarPosicionParametros parametros, Posicion posicion) {
        return dependenciesRepository.get(posicion.getTipoApuesta().getTipoApuestaService())
                .getPixelcoinsCerrarPosicion(posicion.getPosicionId(), parametros.getCantidad());
    }
}
