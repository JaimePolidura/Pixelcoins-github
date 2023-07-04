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
        Map<TipoCuentaPatrimonio, Double> desglosadoPorTipoCuenta = dependencies.filterByImplementsInterface(CalculadorPatrimonio.class).stream()
                .collect(Collectors.toMap(CalculadorPatrimonio::tipoCuenta, c -> calcular(c, jugadorId)));

        TreeMap<TipoCuentaPatrimonio, Double> desglosadoPorTipoCuentaSinOrdenar = new TreeMap<>(Comparator.comparingInt(TipoCuentaPatrimonio::getShowPrioriy)
                .reversed());
        desglosadoPorTipoCuentaSinOrdenar.putAll(desglosadoPorTipoCuenta);

        return desglosadoPorTipoCuentaSinOrdenar;
    }

    private Double calcular(CalculadorPatrimonio calculador, UUID jugadorId) {
        return calculador.calcular(jugadorId);
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
