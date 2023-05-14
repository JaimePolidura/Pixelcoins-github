package es.serversurvival.jugadores._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesUtils;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static es.jaime.javaddd.application.utils.CollectionUtils.*;

@Service
@AllArgsConstructor
public final class CalculadorPatrimonio {
    private final JugadoresService jugadoresService;
    private final PosicionesUtils posicionesUtils;
    private final EmpresasService empresasService;
    private final DeudasService deudasService;

    public int getPosicionTopRicos(String jugadorNombre) {
        Map<String, Double> topJugadores = calcularTopJugadores(true, 25);

        return topJugadores.containsKey(jugadorNombre) ?
                getPositionOfKeyInMap(topJugadores, k -> k.equalsIgnoreCase(jugadorNombre)) :
                20;
    }

    public Map<String, Double> calcularTopJugadores (boolean creciente, int limit) {
        List<Jugador> allJugadordes = jugadoresService.findAll().stream()
                .limit(limit)
                .toList();

        HashMap<String, Double> toReturn = new HashMap<>();

        allJugadordes.forEach((jugador) -> {
            toReturn.put(jugador.getNombre(), calcular(jugador.getNombre()));
        });


        return sortMapByValue(toReturn, creciente ? Comparator.naturalOrder() : Comparator.reverseOrder());
    }

    public double calcular(String jugadorNombre) {
        double activos = 0;

        activos += jugadoresService.getByNombre(jugadorNombre).getPixelcoins();
        activos += deudasService.getAllPixelcoinsDeudasAcredor(jugadorNombre);
        activos += deudasService.getAllPixelcoinsDeudasDeudor(jugadorNombre);
        activos += empresasService.getAllPixelcoinsEnEmpresas(jugadorNombre);
        activos += posicionesUtils.getAllPixeloinsEnValores(jugadorNombre);

        return activos;
    }
}
