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
import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import es.serversurvival.pixelcoins.transacciones.domain.Transaccion;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesSaver;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class AbrirPosicionBolsaUseCase implements UseCaseHandler<AbrirPosicoinBolsaParametros> {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final AbridorOrdenesPremarket abridorOrdenesPremarket;
    private final ActivosBolsaService activosBolsaService;
    private final TransaccionesSaver transaccionesSaver;
    private final PosicionesService posicionesService;
    private final Configuration configuration;
    private final BolsaValidator validator;
    private final EventBus eventBus;

    @Override
    public void handle(AbrirPosicoinBolsaParametros parametros) {
        validator.activoBolsaExsiste(parametros.getActivoBolsaId());
        validator.cantidadCorrecta(parametros.getCantidad());
        validator.suficientesPixelcoinsAbrir(parametros);

        boolean premarketHabilitado = configuration.getBoolean(ConfigurationKey.BOLSA_PREMARKET_HABILITADO);

        if(!abridorOrdenesPremarket.estaElMercadoAbierto() && premarketHabilitado){
            abridorOrdenesPremarket.abrirOrdenAbrir(parametros.toAbrirOrdenPremarketAbrirParametros());
            eventBus.publish(new PosicionBolsaAbierta(parametros.getJugadorId(), true));
            return;
        }

        UUID posicionAAbrirId = UUID.randomUUID();
        ActivoBolsa activoBolsa = activosBolsaService.getById(parametros.getActivoBolsaId());
        double precioApertura = activoBolsaUltimosPreciosService.getUltimoPrecio(parametros.getActivoBolsaId(), null);
        double totalPixelcoins = validator.getPixelcoinsAbrirPosicion(parametros.getActivoBolsaId(), parametros.getCantidad(),
                parametros.getTipoApuesta(), parametros.getJugadorId());

        transaccionesSaver.save(Transaccion.builder()
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
