package es.serversurvival.tienda._shared.newformat.domain;

import es.jaime.javaddd.domain.Aggregate;
import es.serversurvival._shared.utils.Funciones;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

@AllArgsConstructor
public final class EncantamientoObjecto extends Aggregate {
    @Getter private final String nombre;
    @Getter private final int nivel;

    @Override
    @SneakyThrows
    public String toString() {
        return Funciones.MAPPER.writeValueAsString(this);
    }
}
