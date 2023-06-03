package es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.TipoApuestaService;
import es.serversurvival.v2.pixelcoins.jugadores.patrimonio.CalculadorPatrimonio;
import es.serversurvival.v2.pixelcoins.jugadores.patrimonio.TipoCuentaPatrimonio;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class BolsaCalculadorPatrimonio implements CalculadorPatrimonio {
    private final DependenciesRepository dependenciesRepository;
    private final PosicionesService posicionesService;

    @Override
    public double calcular(UUID jugadorId) {
        List<Posicion> posicionesAbiertas = posicionesService.findPosicionesAbiertasByJugadorId(jugadorId);
        double valorTotal = 0;

        for (Posicion posicionesAbierta : posicionesAbiertas) {
            Class<? extends TipoApuestaService> tipoApuestaServiceClass = posicionesAbierta.getTipoApuesta().getTipoApuestaService();
            TipoApuestaService tipoApuestaService = dependenciesRepository.get(tipoApuestaServiceClass);

            valorTotal += tipoApuestaService.getPixelcoinsCerrarPosicion(posicionesAbierta.getPosicionId(), posicionesAbierta.getCantidad());
        }

        return valorTotal;
    }

    @Override
    public TipoCuentaPatrimonio tipoCuenta() {
        return TipoCuentaPatrimonio.BOLSA;
    }
}
