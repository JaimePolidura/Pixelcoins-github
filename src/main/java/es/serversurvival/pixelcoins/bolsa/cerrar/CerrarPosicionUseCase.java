package es.serversurvival.pixelcoins.bolsa.cerrar;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.bolsa._shared.BolsaValidator;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.application.AbridorOrdenesPremarket;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.Posicion;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.application.PosicionesService;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.application.OrdenesPremarketService;
import es.serversurvival.pixelcoins.transacciones.Movimiento;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesSaver;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class CerrarPosicionUseCase implements UseCaseHandler<CerrarPosicionParametros> {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final AbridorOrdenesPremarket abridorOrdenesPremarket;
    private final OrdenesPremarketService ordenesPremarketService;
    private final DependenciesRepository dependenciesRepository;
    private final ActivosBolsaService activosBolsaService;
    private final TransaccionesSaver transaccionesSaver;
    private final PosicionesService posicionesService;
    private final BolsaValidator validator;
    private final EventBus eventBus;

    @Override
    public void handle(CerrarPosicionParametros parametros) {
        validator.jugaodrTienePosicion(parametros.getPosicionAbiertaId(), parametros.getJugadorId());
        validator.posicionTieneCantidad(parametros.getPosicionAbiertaId(), parametros.getCantidad());
        validator.posicionAbierta(parametros.getPosicionAbiertaId());

        if(!abridorOrdenesPremarket.estaElMercadoAbierto()){
            abridorOrdenesPremarket.abrirOrdenCerrar(parametros.toAbrirOrdenPremarketCerrarParametros());
            eventBus.publish(new PosicionBolsaCerrada(parametros.getJugadorId()));
            return;
        }

        Posicion posicionAbierta = posicionesService.getById(parametros.getPosicionAbiertaId());
        ActivoBolsa activo = activosBolsaService.getById(posicionAbierta.getActivoBolsaId());
        double precioPorUnidad = activoBolsaUltimosPreciosService.getUltimoPrecio(posicionAbierta.getActivoBolsaId(), null);
        double valorPosicion = getValorPosicion(parametros, posicionAbierta);
        double rentabilidad = calcularRentabilidad(posicionAbierta);

        Posicion posicionCerrada = posicionAbierta.cerrar(parametros.getCantidad(), precioPorUnidad, rentabilidad);
        posicionAbierta = posicionAbierta.decrementarCantidad(parametros.getCantidad());

        posicionesService.savePosicionAbiertaConNuevaCantidad(posicionAbierta);
        posicionesService.save(posicionCerrada);
        ordenesPremarketService.deletebyPosicionAbiertId(posicionAbierta.getPosicionId());
        transaccionesSaver.save(Transaccion.builder()
                .pagadoId(parametros.getJugadorId())
                .tipo(posicionCerrada.getTipoApuesta().getTipoTransaccionCerrar())
                .pixelcoins(valorPosicion)
                .objeto(posicionCerrada.getActivoBolsaId())
                .build());

        eventBus.publish(new PosicionBolsaCerrada(parametros.getJugadorId(), parametros.getCantidad(), posicionAbierta.getPrecioApertura(),
                precioPorUnidad, rentabilidad, activo, posicionAbierta.getTipoApuesta(), valorPosicion));
    }

    private double calcularRentabilidad(Posicion posicionAbierta) {
        return dependenciesRepository.get(posicionAbierta.getTipoApuesta().getTipoApuestaService())
                .calcularRentabilidad(posicionAbierta.getPrecioApertura(), posicionAbierta.getPrecioCierre());
    }

    private double getValorPosicion(CerrarPosicionParametros parametros, Posicion posicion) {
        return dependenciesRepository.get(posicion.getTipoApuesta().getTipoApuestaService())
                .getPixelcoinsCerrarPosicion(posicion.getPosicionId(), parametros.getJugadorId(), parametros.getCantidad());
    }
}
