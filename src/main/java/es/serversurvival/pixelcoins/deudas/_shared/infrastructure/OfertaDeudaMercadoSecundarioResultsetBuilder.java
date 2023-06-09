package es.serversurvival.pixelcoins.deudas._shared.infrastructure;

import es.dependencyinjector.dependencies.annotations.Component;
import es.serversurvival._shared.mysql.ResultsetObjetBuilder;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoSecundario;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public final class OfertaDeudaMercadoSecundarioResultsetBuilder implements ResultsetObjetBuilder<OfertaDeudaMercadoSecundario> {
    @Override
    public OfertaDeudaMercadoSecundario build(ResultSet rs) throws SQLException {
        return new OfertaDeudaMercadoSecundario(
                UUID.fromString(rs.getString("ofertaId")),
                UUID.fromString(rs.getString("vendedorId")),
                rs.getTimestamp("fechaSubida").toLocalDateTime(),
                rs.getInt("cantidad"),
                rs.getDouble("precio"),
                rs.getString("objeto"),
                TipoOferta.valueOf(rs.getString("tipoOferta"))
        );
    }
}
