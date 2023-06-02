package es.serversurvival.v2.pixelcoins.bolsa.abrir;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.bolsa._shared.BolsaValidator;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.ActivosBolsaService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.PosicionAbiertaBuilder;
import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.PosicionesService;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class AbrirPosicionBolsaUseCase {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final TransaccionesService transaccionesService;
    private final ActivosBolsaService activosBolsaService;
    private final PosicionesService posicionesService;
    private final BolsaValidator validator;
    private final EventBus eventBus;

    public void abrir(AbrirPosicoinBolsaParametros parametros) {
        validator.activoBolsaExsiste(parametros.getActiboBolsaId());
        validator.cantidadCorrecta(parametros.getCantidad());
        validator.suficientesPixelcoinsAbrir(parametros);

        UUID posicionAAbrirId = UUID.randomUUID();
        double precioApertura = activoBolsaUltimosPreciosService.getUltimoPrecio(parametros.getActiboBolsaId());
        double totalPixelcoins = validator.getPixelcoinsAbrirPosicion(parametros.getActiboBolsaId(), parametros.getCantidad(),
                parametros.getTipoApuesta());

        activosBolsaService.incrementarNReferencias(parametros.getActiboBolsaId());

        transaccionesService.save(Transaccion.builder()
                .pagadorId(parametros.getJugadorId())
                .objeto(parametros.getActiboBolsaId())
                .pixelcoins(totalPixelcoins)
                .tipo(parametros.getTipoApuesta().getTipoTransaccionAbrir())
                .build());

        posicionesService.save(PosicionAbiertaBuilder.builder()
                .posicionId(posicionAAbrirId)
                .tipoApuesta(parametros.getTipoApuesta())
                .activoBolsaId(parametros.getActiboBolsaId())
                .precioApertura(precioApertura)
                .cantidad(parametros.getCantidad())
                .jugadorId(parametros.getJugadorId())
                .build());

        eventBus.publish(new PosicionBolsaAbierta(posicionAAbrirId));
    }
}
