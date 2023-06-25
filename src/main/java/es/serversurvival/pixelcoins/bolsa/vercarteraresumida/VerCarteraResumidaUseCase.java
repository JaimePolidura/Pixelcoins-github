package es.serversurvival.pixelcoins.bolsa.vercarteraresumida;

import com.google.common.util.concurrent.AtomicDouble;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.Posicion;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.application.PosicionesService;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoBolsaApuesta;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@UseCase
@AllArgsConstructor
public final class VerCarteraResumidaUseCase {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final DependenciesRepository dependenciesRepository;
    private final PosicionesService posicionesService;

    public CarteraBolsaResumida ver(UUID jugadorId) {
        List<Posicion> posiciones = posicionesService.findPosicionesAbiertasByJugadorId(jugadorId);
        Map<CarteraResumidaItemIdentificacdor, CarteraResumidaItemBuilder> itemsCarteraResumida = new HashMap<>();
        AtomicDouble valorTotalCartera = new AtomicDouble(0);

        for (Posicion it : posiciones) {
            CarteraResumidaItemIdentificacdor identificadorIt = CarteraResumidaItemIdentificacdor.fromPosicion(it);
            double precioActual = activoBolsaUltimosPreciosService.getUltimoPrecio(it.getActivoBolsaId(), it.getJugadorId());
            double valorPosicion = getValorPosicion(it, precioActual);
            valorTotalCartera.set(valorTotalCartera.get() + valorPosicion);

            if(!itemsCarteraResumida.containsKey(identificadorIt)){
                itemsCarteraResumida.putIfAbsent(identificadorIt, CarteraResumidaItemBuilder.fromPosicion(it, precioActual, valorPosicion));
                continue;
            }

            CarteraResumidaItemBuilder carteraItemExistente = itemsCarteraResumida.get(identificadorIt);
            CarteraResumidaItemBuilder carteraItemMerged = carteraItemExistente.merge(it, precioActual, valorPosicion);
            itemsCarteraResumida.put(identificadorIt, carteraItemMerged);
        }

        List<CarteraResumidaItem> itemsCartera = itemsCarteraResumida.values().stream()
                .map(itemBuilder -> actualizarPesoEnCartera(itemBuilder, valorTotalCartera.get()))
                .map(CarteraResumidaItemBuilder::build)
                .sorted()
                .collect(Collectors.toList());

        return new CarteraBolsaResumida(itemsCartera, valorTotalCartera.get());
    }

    private CarteraResumidaItemBuilder actualizarPesoEnCartera(CarteraResumidaItemBuilder carteraItemBuilder, double valorTotalCartera) {


        return carteraItemBuilder.withPeso(carteraItemBuilder.getValorPosicion() / valorTotalCartera);
    }

    private double getValorPosicion(Posicion posicionActual, double precioActual) {
        return dependenciesRepository.get(posicionActual.getTipoApuesta().getTipoApuestaService())
                .getPixelcoinsCerrarPosicion(posicionActual.getPosicionId(), posicionActual.getCantidad(), precioActual);
    }

    @EqualsAndHashCode
    @AllArgsConstructor
    private static class CarteraResumidaItemIdentificacdor {
        @Getter private final UUID activoBolsaId;
        @Getter private final TipoBolsaApuesta tipoBolsaApuesta;

        public static CarteraResumidaItemIdentificacdor fromPosicion(Posicion posicion) {
            return new CarteraResumidaItemIdentificacdor(posicion.getActivoBolsaId(), posicion.getTipoApuesta());
        }
    }
}
