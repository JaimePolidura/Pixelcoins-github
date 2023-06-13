package es.serversurvival.pixelcoins.mensajes._shared.infrastructure;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.serversurvival.pixelcoins.mensajes._shared.domain.Mensaje;
import es.serversurvival.pixelcoins.mensajes._shared.domain.MensajesRepository;
import es.serversurvival.pixelcoins.mensajes._shared.domain.TipoMensaje;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public final class MySQLMensajesRepository extends DataBaseRepository<Mensaje, UUID> implements MensajesRepository {
    private static final String TABLE_NAME = "mensajes";
    private static final String ID_FIELD = "mensajeId";

    public MySQLMensajesRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(Mensaje mensaje) {
        super.save(mensaje);
    }

    @Override
    public List<Mensaje> findByJugadorIdNoVistos(UUID jugadorId) {
        return super.buildListFromQuery(String.format("SELECT * FROM %s WHERE jugadorId = '%s' AND fechaVisto IS NULL", TABLE_NAME, jugadorId.toString()));
    }

    @Override
    public void deleteByFechaVistaLessThan(LocalDateTime lessThan) {
        super.execute(String.format("DELETE FROM %s WHERE fechaVisto < %s", TABLE_NAME, lessThan));
    }

    @Override
    protected EntityMapper<Mensaje> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .classesToMap(Mensaje.class)
                .idField(ID_FIELD)
                .build();
    }

    @Override
    public Mensaje buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Mensaje(
                UUID.fromString(rs.getString("mensajeId")),
                UUID.fromString(rs.getString("destinatarioId")),
                TipoMensaje.valueOf(rs.getString("tipo")),
                rs.getTimestamp("fechaEnvio").toLocalDateTime(),
                rs.getString("fechaVisto") != null ? rs.getTimestamp("fechaVisto").toLocalDateTime() : null,
                rs.getString("mensaje")
        );
    }
}
