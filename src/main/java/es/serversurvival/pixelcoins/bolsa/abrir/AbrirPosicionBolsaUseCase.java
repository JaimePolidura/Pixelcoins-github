package es.serversurvival.pixelcoins.bolsa.abrir;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.bolsa._shared.BolsaValidator;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.PosicionAbiertaBuilder;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.application.PosicionesService;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.application.AbridorOrdenesPremarket;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class AbrirPosicionBolsaUseCase implements UseCaseHandler<AbrirPosicoinBolsaParametros> {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final AbridorOrdenesPremarket abridorOrdenesPremarket;
    private final TransaccionesService transaccionesService;
    private final ActivosBolsaService activosBolsaService;
    private final PosicionesService posicionesService;
    private final BolsaValidator validator;
    private final EventBus eventBus;

    @Override
    public void handle(AbrirPosicoinBolsaParametros parametros) {
        validator.activoBolsaExsiste(parametros.getActivoBolsaId());
        validator.cantidadCorrecta(parametros.getCantidad());
        validator.suficientesPixelcoinsAbrir(parametros);

        if(!abridorOrdenesPremarket.estaElMercadoAbierto()){
            abridorOrdenesPremarket.abrirOrdenAbrir(parametros.toAbrirOrdenPremarketAbrirParametros());
            eventBus.publish(new PosicionBolsaAbierta(parametros.getJugadorId(), true));
            return;
        }

        UUID posicionAAbrirId = UUID.randomUUID();
        ActivoBolsa activoBolsa = activosBolsaService.getById(parametros.getActivoBolsaId());
        double precioApertura = activoBolsaUltimosPreciosService.getUltimoPrecio(parametros.getActivoBolsaId(), null);
        double totalPixelcoins = validator.getPixelcoinsAbrirPosicion(parametros.getActivoBolsaId(), parametros.getCantidad(),
                parametros.getTipoApuesta(), parametros.getJugadorId());

        transaccionesService.save(Transaccion.builder()
                .pagadorId(parametros.getJugadorId())
                .objeto(parametros.getActivoBolsaId())
                .pixelcoins(totalPixelcoins)
                .tipo(parametros.getTipoApuesta().getTipoTransaccionAbrir())
                .build());

        posicionesService.save(PosicionAbiertaBuilder.builder()
                .posicionId(posicionAAbrirId)
                .tipoApuesta(parametros.getTipoApuesta())
                .activoBolsaId(parametros.getActivoBolsaId())
                .precioApertura(precioApertura)
                .cantidad(parametros.getCantidad())
                .jugadorId(parametros.getJugadorId())
                .build());

        eventBus.publish(new PosicionBolsaAbierta(parametros.getJugadorId(), parametros.getCantidad(), false,
                activoBolsa, precioApertura, parametros.getTipoApuesta(), totalPixelcoins));
    }
}
