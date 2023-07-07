package es.serversurvival.pixelcoins.tienda._shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.jaime.javaddd.domain.database.DatabaseTypeSerializer;
import es.serversurvival._shared.items.ItemMinecraftEncantamientos;
import lombok.SneakyThrows;

public final class MySQLTiendaObjetoEncantamientosSerializer implements DatabaseTypeSerializer<ItemMinecraftEncantamientos> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public String serialize(ItemMinecraftEncantamientos encantamientos) {
        return !encantamientos.getEncantamientos().isEmpty() ?
                String.format("'%s'", objectMapper.writeValueAsString(encantamientos.getEncantamientos())) :
                "'[]'";
    }
}
