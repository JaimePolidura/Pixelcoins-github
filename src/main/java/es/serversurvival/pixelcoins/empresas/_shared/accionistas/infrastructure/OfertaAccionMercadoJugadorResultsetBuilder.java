package es.serversurvival.pixelcoins.empresas._shared.accionistas.infrastructure;

import es.dependencyinjector.dependencies.annotations.Component;
import es.serversurvival._shared.mysql.ResultsetObjetBuilder;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoJugador;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public final class OfertaAccionMercadoJugadorResultsetBuilder implements ResultsetObjetBuilder<OfertaAccionMercadoJugador> {
    @Override
    public OfertaAccionMercadoJugador build(ResultSet rs) throws SQLException {
        return new OfertaAccionMercadoJugador(
                UUID.fromString(rs.getString("ofertaId")),
                UUID.fromString(rs.getString("vendedorId")),
                rs.getTimestamp("fechaSubida").toLocalDateTime(),
                rs.getInt("cantidad"),
                rs.getDouble("precio"),
                rs.getString("objeto"),
                TipoOferta.valueOf(rs.getString("tipoOferta")),
                UUID.fromString(rs.getString("empresaId"))
        );
    }
}
