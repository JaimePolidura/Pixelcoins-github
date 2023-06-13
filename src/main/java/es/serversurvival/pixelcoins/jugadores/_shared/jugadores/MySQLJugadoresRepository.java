package es.serversurvival.pixelcoins.jugadores._shared.jugadores;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLJugadoresRepository extends DataBaseRepository<Jugador, UUID> implements JugadoresRepository {
    private static final String TABLE_NAME = "jugadores";
    private static final String ID_FIELD_NAME = "jugadorId";

    public MySQLJugadoresRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(Jugador jugador) {
        super.save(jugador);
    }

    @Override
    public Optional<Jugador> findById(UUID jugadorId) {
        return super.findById(jugadorId);
    }

    @Override
    public Optional<Jugador> findByNombre(String nombre) {
        return super.buildObjectFromQuery(Select.from(TABLE_NAME).where("nombre").equal(nombre));
    }

    @Override
    public List<Jugador> findAll() {
        return super.all();
    }

    @Override
    protected EntityMapper<Jugador> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .classesToMap(Jugador.class)
                .idField(ID_FIELD_NAME)
                .build();
    }

    @Override
    public Jugador buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Jugador(
                UUID.fromString(rs.getString("jugadorId")),
                rs.getString("nombre"),
                rs.getTimestamp("fechaCreacion").toLocalDateTime()
        );
    }
}
