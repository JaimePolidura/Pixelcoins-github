package es.serversurvival.pixelcoins.tienda._shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.jaime.deserializer.DatabaseTypeDeserializer;
import es.serversurvival._shared.items.ItemMinecraftEncantamiento;
import es.serversurvival._shared.items.ItemMinecraftEncantamientos;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class MySQLTiendaObjetoEncantamientosDeserializer implements DatabaseTypeDeserializer<ItemMinecraftEncantamientos> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public ItemMinecraftEncantamientos deserialize(String fieldName, ResultSet rs, Class<? extends ItemMinecraftEncantamientos> typeClass) throws SQLException {
        String jsonString = rs.getString(fieldName);
        ItemMinecraftEncantamiento[] encantamientos = objectMapper.readValue(jsonString, ItemMinecraftEncantamiento[].class);

        return new ItemMinecraftEncantamientos(Arrays.stream(encantamientos).collect(Collectors.toList()));
    }
}
