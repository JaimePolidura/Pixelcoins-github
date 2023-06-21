package es.serversurvival.pixelcoins.tienda._shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.jaime.javaddd.domain.database.DatabaseTypeSerializer;
import lombok.SneakyThrows;

public final class MySQLTiendaObjetoEncantamientosSerializer implements DatabaseTypeSerializer<TiendaItemMinecraftEncantamientos> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public String serialize(TiendaItemMinecraftEncantamientos encantamientos) {
        return !encantamientos.getEncantamientos().isEmpty() ?
                objectMapper.writeValueAsString(encantamientos.getEncantamientos()) :
                "'[]'";
    }
}
