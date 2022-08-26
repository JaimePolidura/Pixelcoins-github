package es.serversurvival.mensajes._shared.infrastructure;

import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.annotations.Repository;
import es.jaimetruman.delete.Delete;
import es.jaimetruman.select.Select;
import es.serversurvival.mensajes._shared.domain.MensajesRepository;
import es.serversurvival.mensajes._shared.domain.Mensaje;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public final class MySQLMensajesRepository extends DataBaseRepository<Mensaje, UUID> implements MensajesRepository {
    private static final String TABLE_NAME = "mensajes";
    private static final String ID_FIELD_NAME = "mensajeId";

    public MySQLMensajesRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(Mensaje mensaje) {
        super.save(mensaje);
    }

    @Override
    public List<Mensaje> findMensajesByDestinatario(String destinatario) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("destinatario").equal(destinatario));
    }

    @Override
    public void deleteByDestinatario(String destinatario) {
        super.execute(Delete.from(TABLE_NAME).where("destinatario").equal(destinatario));
    }

    @Override
    protected EntityMapper<Mensaje> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .idField(ID_FIELD_NAME)
                .classesToMap(Mensaje.class)
                .build();
    }

    @Override
    public Mensaje buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Mensaje(UUID.fromString(rs.getString(ID_FIELD_NAME)),
                rs.getString("enviador"),
                rs.getString("destinatario"),
                rs.getString("mensaje"));
    }
}
