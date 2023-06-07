package es.serversurvival.v2.pixelcoins.bolsa.abrir;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.bolsa._shared.BolsaValidator;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.PosicionAbiertaBuilder;
import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.PosicionesService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.application.AbridorOrdenesPremarket;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class AbrirPosicionBolsaUseCase {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final AbridorOrdenesPremarket abridorOrdenesPremarket;
    private final TransaccionesService transaccionesService;
    private final ActivosBolsaService activosBolsaService;
    private final PosicionesService posicionesService;
    private final BolsaValidator validator;
    private final EventBus eventBus;

    public boolean abrir(AbrirPosicoinBolsaParametros parametros) {
        validator.activoBolsaExsiste(parametros.getActivoBolsaId());
        validator.cantidadCorrecta(parametros.getCantidad());
        validator.suficientesPixelcoinsAbrir(parametros);

        if(!abridorOrdenesPremarket.estaElMercadoAbierto()){
            abridorOrdenesPremarket.abrirOrdenAbrir(parametros.toAbrirOrdenPremarketAbrirParametros());
            return false;
        }

        UUID posicionAAbrirId = UUID.randomUUID();
        double precioApertura = activoBolsaUltimosPreciosService.getUltimoPrecio(parametros.getActivoBolsaId());
        double totalPixelcoins = validator.getPixelcoinsAbrirPosicion(parametros.getActivoBolsaId(), parametros.getCantidad(),
                parametros.getTipoApuesta());

        activosBolsaService.incrementarNReferencias(parametros.getActivoBolsaId());

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

        eventBus.publish(new PosicionBolsaAbierta(posicionAAbrirId));

        return true;
    }
}
