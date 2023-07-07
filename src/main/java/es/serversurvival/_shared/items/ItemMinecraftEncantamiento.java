package es.serversurvival._shared.items;

import es.serversurvival._shared.utils.Funciones;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor
@AllArgsConstructor
public final class ItemMinecraftEncantamiento {
    @Getter private String nombre;
    @Getter private int nivel;

    @Override
    @SneakyThrows
    public String toString() {
        return Funciones.MAPPER.writeValueAsString(this);
    }
}
