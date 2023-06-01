package es.serversurvival.v2.pixelcoins.bolsa._shared;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v2.pixelcoins._shared.Validador;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.ActivosBolsaService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.TipoApuestaService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.TipoBolsaApuesta;
import es.serversurvival.v2.pixelcoins.bolsa.abrir.AbrirPosicionBolsaUseCase;
import es.serversurvival.v2.pixelcoins.bolsa.abrir.AbrirPosicoinBolsaParametros;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class BolsaValidator {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final DependenciesRepository dependenciesRepository;
    private final TransaccionesService transaccionesService;
    private final ActivosBolsaService activosBolsaService;
    private final Validador validador;

    public void suficientesPixelcoinsAbrir(AbrirPosicoinBolsaParametros parametros) {
        double precioBolsaTotal = getPixelcoinsAbrirPosicion(parametros.getActiboBolsaId(), parametros.getCantidad(),
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
        return tipoApuestaService.calcularImporteAbrirPosicoins(activoBolsaId, cantidad);
    }
}
