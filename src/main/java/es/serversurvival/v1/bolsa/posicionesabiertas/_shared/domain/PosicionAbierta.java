package es.serversurvival.v1.bolsa.posicionesabiertas._shared.domain;

import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.v1.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;

import java.util.UUID;


@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class PosicionAbierta {
    @Getter private final UUID posicionAbiertaId;
    @Getter private final String jugador;
    @Getter private final TipoActivo tipoActivo;
    @Getter private final String nombreActivo;
    @Getter private final int cantidad;
    @Getter private final double precioApertura;
    @Getter private final String fechaApertura;
    @Getter private final TipoPosicion tipoPosicion;

    public PosicionAbierta withCantidad(int cantidad){
        return new PosicionAbierta(posicionAbiertaId, jugador, tipoActivo, nombreActivo,
                cantidad, precioApertura, fechaApertura, tipoPosicion);
    }

    public PosicionAbierta withPrecioApertura(double newPrecioApertura){
        return new PosicionAbierta(posicionAbiertaId, jugador, tipoActivo, nombreActivo,
                cantidad, newPrecioApertura, fechaApertura, tipoPosicion);
    }

    public PosicionAbierta withJugador(String jugador){
        return new PosicionAbierta(posicionAbiertaId, jugador, tipoActivo, nombreActivo,
                cantidad, precioApertura, fechaApertura, tipoPosicion);
    }

    public boolean esLargo () {
        return tipoPosicion == TipoPosicion.LARGO;
    }

    public boolean esCorto () {
        return tipoPosicion == TipoPosicion.CORTO;
    }

    public boolean esTipoAccion () {
        return tipoActivo == TipoActivo.ACCIONES;
    }

    public Material getMaterial(){
        return this.esCorto() ? Material.REDSTONE_TORCH :
                switch (this.tipoActivo){
                    case MATERIAS_PRIMAS -> Material.COAL;
                    case ACCIONES -> Material.NAME_TAG;
                    case CRIPTOMONEDAS -> Material.GOLD_INGOT;
        };
    }

}
