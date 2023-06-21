package es.serversurvival.pixelcoins.tienda._shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
public final class TiendaItemMinecraftEncantamientos {
    @Getter private final List<TiendaItemMinecraftEncantamiento> encantamientos;
}
