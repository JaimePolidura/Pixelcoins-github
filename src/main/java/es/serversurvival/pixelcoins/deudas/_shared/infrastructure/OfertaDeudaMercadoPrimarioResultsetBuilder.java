package es.serversurvival.pixelcoins.deudas._shared.infrastructure;

import es.dependencyinjector.dependencies.annotations.Component;
import es.serversurvival._shared.mysql.ResultsetObjetBuilder;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoPrimario;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public final class OfertaDeudaMercadoPrimarioResultsetBuilder implements ResultsetObjetBuilder<OfertaDeudaMercadoPrimario> {
    @Override
    public OfertaDeudaMercadoPrimario build(ResultSet rs) throws SQLException {
        return new OfertaDeudaMercadoPrimario(
                UUID.fromString(rs.getString("ofertaId")),
                UUID.fromString(rs.getString("vendedorId")),
                rs.getTimestamp("fechaSubida").toLocalDateTime(),
                rs.getInt("cantidad"),
                rs.getDouble("precio"),
                rs.getString("objeto"),
                TipoOferta.valueOf(rs.getString("tipoOferta")),
                rs.getDouble("interes"),
                rs.getInt("nCuotasTotales"),
                rs.getLong("periodoPagoCuotaMs")
        );
    }
}
