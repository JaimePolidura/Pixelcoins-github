package es.serversurvival.v2.pixelcoins._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.IllegalLength;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class Validador {
    private final TransaccionesService transaccionesService;

    public void stringLongitudEntre(String texto, int min, int max, String nombre) {
        if(texto.length() < min || texto.length() > max){
            throw new IllegalLength(String.format("El tamaño de %s tiene que estar entre %s y %s", nombre, min, max ));
        }
    }

    public void stringNoVacio(String texto, String nombre) {
        if(texto == null || texto.length() == 0) {
            throw new IllegalLength(String.format("%s no puede estar vacio", nombre));
        }
    }

    public void numeroMayorQueCero(double numero, String nombre) {
        if(numero <= 0){
            throw new IllegalQuantity(String.format("%s tiene que ser un numero positivo mayor que 0", nombre));
        }
    }

    public void jugadorTienePixelcoins(UUID jugadorId, double pixelcoinsMinimo) {
        if(transaccionesService.getBalancePixelcions(jugadorId) < pixelcoinsMinimo){
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins");
        }
    }
}
