package es.serversurvival.pixelcoins.mensajes._shared.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.delete.Delete;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.mensajes._shared.domain.Mensaje;
import es.serversurvival.pixelcoins.mensajes._shared.domain.MensajesRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@MySQLRepository
public final class MySQLMensajesRepository extends Repository<Mensaje, UUID, Object> implements MensajesRepository {
    private static final String TABLE_NAME = "mensajes";
    private static final String ID_FIELD = "mensajeId";

    public MySQLMensajesRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(Mensaje mensaje) {
        super.save(mensaje);
    }

    @Override
    public List<Mensaje> findByJugadorIdNoVistos(UUID jugadorId) {
        return super.buildListFromQuery(String.format("SELECT * FROM %s WHERE destinatarioId = '%s' AND visto IS FALSE", TABLE_NAME, jugadorId.toString()));
    }

    @Override
    public void deleteByFechaVistaLessThan(LocalDateTime lessThan) {
        String query = Delete.from(TABLE_NAME).where("fechaVisto").smaller(lessThan).and("visto").toString();
        query += " AND visto IS TRUE";

        super.execute(query);
    }

    @Override
    public EntityMapper<Mensaje, Object> entityMapper() {
        return EntityMapper.builder()
                .classesToMap(Mensaje.class)
                .idField(ID_FIELD)
                .table(TABLE_NAME)
                .build();
    }
}
