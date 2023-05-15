package es.serversurvival.v1.empresas.empresas.crear;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class EmpresaCreadaEvento extends PixelcoinsEvento {
    @Getter private final String jugador;
    @Getter private final String empresa;

    public static EmpresaCreadaEvento of(String jugador, String comprador){
        return new EmpresaCreadaEvento(jugador, comprador);
    }
}
