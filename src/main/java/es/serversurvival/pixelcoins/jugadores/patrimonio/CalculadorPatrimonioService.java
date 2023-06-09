package es.serversurvival.pixelcoins.jugadores.patrimonio;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.application.utils.CollectionUtils;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.Jugador;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

import static es.jaime.javaddd.application.utils.CollectionUtils.getPositionOfKeyInMap;
import static es.jaime.javaddd.application.utils.CollectionUtils.sortMapByValue;

@Service
@AllArgsConstructor
public final class CalculadorPatrimonioService {
    private final DependenciesRepository dependencies;
    private final JugadoresService jugadoresService;

    public double calcular(UUID jugadorId) {
        return dependencies.filterByImplementsInterface(CalculadorPatrimonio.class).stream()
                .parallel()
                .mapToDouble(calculador -> calculador.calcular(jugadorId))
                .sum();
    }

    public double calcularCuenta(TipoCuentaPatrimonio tipoCuenta, UUID jugadorId) {
        return dependencies.filterByImplementsInterface(CalculadorPatrimonio.class).stream()
                .filter(a -> a.tipoCuenta() == tipoCuenta)
                .findFirst()
                .get()
                .calcular(jugadorId);
    }

    public Map<TipoCuentaPatrimonio, Double> calcularDesglosadoPorCuentas(UUID jugadorId) {
        Map<TipoCuentaPatrimonio, Double> collect = dependencies.filterByImplementsInterface(CalculadorPatrimonio.class).stream()
                .parallel()
                .collect(Collectors.toMap(CalculadorPatrimonio::tipoCuenta, c -> calcular(jugadorId)));

        return CollectionUtils.sortMapByValue(collect, (a, b) -> Double.compare(b, a));
    }

    public int getPosicionTopRicos(String jugadorNombre) {
        Map<String, Double> topJugadores = calcularTopJugadores(true, Integer.MAX_VALUE);

        return topJugadores.containsKey(jugadorNombre) ?
                getPositionOfKeyInMap(topJugadores, k -> k.equalsIgnoreCase(jugadorNombre)) :
                20;
    }

    public Map<String, Double> calcularTopJugadores (boolean creciente, int limit) {
        List<Jugador> allJugadordes = jugadoresService.findAll();

        HashMap<String, Double> toReturn = new HashMap<>();

        allJugadordes.forEach((jugador) -> {
            toReturn.put(jugador.getNombre(), calcular(jugador.getJugadorId()));
        });


        Map<String, Double> sortedByValueWithoutLimit = sortMapByValue(toReturn, creciente ? Comparator.naturalOrder() : Comparator.reverseOrder());
        Map<String, Double> sortedByValueWithLimit = new LinkedHashMap<>();
        int counter = 0;

        for (Map.Entry<String, Double> entry : sortedByValueWithoutLimit.entrySet()) {
            if(counter == limit) break;

            sortedByValueWithLimit.put(entry.getKey(), entry.getValue());
            counter++;
        }

        return sortedByValueWithLimit;
    }
}
