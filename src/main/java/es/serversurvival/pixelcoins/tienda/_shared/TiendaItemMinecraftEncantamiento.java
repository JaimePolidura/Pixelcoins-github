package es.serversurvival.pixelcoins.tienda._shared;

import es.serversurvival._shared.utils.Funciones;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor
@AllArgsConstructor
public final class TiendaItemMinecraftEncantamiento {
    @Getter private String nombre;
    @Getter private int nivel;

    @Override
    @SneakyThrows
    public String toString() {
        return Funciones.MAPPER.writeValueAsString(this);
    }
}
