package es.serversurvival.tienda._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
public final class TiendaObjeto {
    @Getter private final UUID tiendaObjetoId;
    @Getter private final String jugador;
    @Getter private final String objeto;
    @Getter private final int cantidad;
    @Getter private final double precio;
    @Getter private final int durabilidad;
    @Getter private final List<EncantamientoObjecto> encantamientos;

    public TiendaObjeto decrementCantidadByOne(){
        return new TiendaObjeto(tiendaObjetoId, jugador, objeto, cantidad - 1, precio, durabilidad, encantamientos);
    }

    public TiendaObjeto withJugador(String jugador){
        return new TiendaObjeto(tiendaObjetoId, jugador, objeto, cantidad, precio, durabilidad, encantamientos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TiendaObjeto that = (TiendaObjeto) o;
        return tiendaObjetoId.equals(that.tiendaObjetoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tiendaObjetoId);
    }
}
