package es.serversurvival.pixelcoins.tienda._shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.jaime.deserializer.DatabaseTypeDeserializer;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class MySQLTiendaObjetoEncantamientosDeserializer implements DatabaseTypeDeserializer<TiendaItemMinecraftEncantamientos> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public TiendaItemMinecraftEncantamientos deserialize(String fieldName, ResultSet rs, Class<? extends TiendaItemMinecraftEncantamientos> typeClass) throws SQLException {
        String jsonString = rs.getString(fieldName);
        TiendaItemMinecraftEncantamiento[] encantamientos = objectMapper.readValue(jsonString, TiendaItemMinecraftEncantamiento[].class);

        return new TiendaItemMinecraftEncantamientos(Arrays.stream(encantamientos).collect(Collectors.toList()));
    }
}
