package es.serversurvival.pixelcoins._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.*;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.pixelcoins.transacciones.TransaccionesBalanceService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class Validador {
    private final TransaccionesBalanceService transaccionesBalanceService;

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

    public void notEqual(UUID a, UUID b, String message) {
        if(a.equals(b)){
            throw new CannotBeYourself(message);
        }
    }

    public void noNull(Object object, String nombre) {
        if(object == null){
            throw new IllegalArgumentException(String.format("%s no puede ser null", nombre));
        }
    }

    public void numeroIgualA(double numero, double numeroASerIgual) {
        if(numero != numeroASerIgual){
            throw new IllegalQuantity(String.format("%s tiene que ser %s", numero, numeroASerIgual));
        }
    }

    public void numeroMayorQueCero(double numero, String nombre) {
        if(numero <= 0){
            throw new IllegalQuantity(String.format("%s tiene que ser un numero positivo mayor que 0", nombre));
        }
    }

    public void jugadorTienePixelcoins(UUID jugadorId, double pixelcoinsMinimo) {
        if(transaccionesBalanceService.get(jugadorId) < pixelcoinsMinimo){
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins");
        }
    }
}
