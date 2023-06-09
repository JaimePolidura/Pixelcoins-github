package es.serversurvival.pixelcoins.tienda._shared;

import com.fasterxml.jackson.core.type.TypeReference;
import es.dependencyinjector.dependencies.annotations.Component;
import es.serversurvival._shared.mysql.ResultsetObjetBuilder;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Component
public final class OfertaTiendaItemMinecraftResultsetBuilder implements ResultsetObjetBuilder<OfertaTiendaItemMinecraft> {
    @Override
    public OfertaTiendaItemMinecraft build(ResultSet rs) throws SQLException {
        return new OfertaTiendaItemMinecraft(
                UUID.fromString(rs.getString("ofertaId")),
                UUID.fromString(rs.getString("vendedorId")),
                rs.getTimestamp("fechaSubida").toLocalDateTime(),
                rs.getInt("cantidad"),
                rs.getDouble("precio"),
                rs.getString("objeto"),
                TipoOferta.valueOf(rs.getString("tipoOferta")),
                encantamientosStringToObject(rs),
                rs.getShort("durabilidad"),
                rs.getBoolean("tieneNombre"),
                rs.getString("Nombre")
        );
    }

    @SneakyThrows
    private List<TiendaItemMinecraftEncantamientos> encantamientosStringToObject(ResultSet rs){
        String jsonRawString = rs.getString("encantamientos");

        return Funciones.MAPPER.readValue(jsonRawString, new TypeReference<>() {});
    }
}
