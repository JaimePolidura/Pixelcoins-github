package es.serversurvival.pixelcoins.jugadores._shared.jugadores;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MySQLRepository
public final class MySQLJugadoresRepository extends Repository<Jugador, UUID, Object> implements JugadoresRepository {
    private static final String TABLE_NAME = "jugadores";
    private static final String ID_FIELD_NAME = "jugadorId";

    public MySQLJugadoresRepository(ConnectionManager connectionManager) {
        super(connectionManager);
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
        return super.findAll();
    }

    @Override
    public EntityMapper<Jugador, Object> entityMapper() {
        return EntityMapper.builder()
                .table(TABLE_NAME)
                .idField(ID_FIELD_NAME)
                .classesToMap(Jugador.class)
                .build();
    }
}
