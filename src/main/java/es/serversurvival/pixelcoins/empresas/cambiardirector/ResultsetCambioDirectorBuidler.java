package es.serversurvival.pixelcoins.empresas.cambiardirector;

import es.dependencyinjector.dependencies.annotations.Component;
import es.serversurvival._shared.mysql.ResultsetObjetBuilder;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.EstadoVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.TipoVotacion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public final class ResultsetCambioDirectorBuidler implements ResultsetObjetBuilder<CambiarDirectorVotacion> {
    @Override
    public CambiarDirectorVotacion build(ResultSet rs) throws SQLException {
        return new CambiarDirectorVotacion(
                UUID.fromString(rs.getString("votacionId")),
                UUID.fromString(rs.getString("empresaId")),
                TipoVotacion.valueOf(rs.getString("tipo")),
                EstadoVotacion.valueOf(rs.getString("estado")),
                UUID.fromString(rs.getString("iniciadoPorJugadorId")),
                rs.getString("fechaFinalizacion") != null ? rs.getTimestamp("fechaFinalizacion").toLocalDateTime() : null,
                rs.getString("descripccion"),
                rs.getTimestamp("fechaInicio").toLocalDateTime(),
                UUID.fromString(rs.getString("nuevoDirectorJugadorId")),
                rs.getDouble("sueldo"),
                rs.getLong("periodoPagoMs")
        );
    }
}
