package es.serversurvival.jugadores._shared.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.jugadores._shared.domain.JugadoresRepository;
import es.serversurvival.jugadores._shared.domain.Jugador;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class MySQLJugadoresRepository extends DataBaseRepository<Jugador, UUID> implements JugadoresRepository {
    private static final String TABLE_NAME = "jugadores";
    private static final String ID_FIELD_NAME = "jugadorId";

    public MySQLJugadoresRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    public static void main(String[] args) {
        Jugador jugador = new Jugador(UUID.randomUUID(), "jaime", 0, 0, 0,
                0, 0, 0, 0);

        var mapper = new ObjectMapper();

        var map =  mapper.convertValue(jugador, Map.class);

        System.out.println(map);
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
        return buildObjectFromQuery(Select.from(TABLE_NAME).where("nombre").equal(nombre));
    }

    @Override
    public List<Jugador> findAll() {
        return super.all();
    }

    @Override
    protected EntityMapper<Jugador> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .classToMap(Jugador.class)
                .idField(ID_FIELD_NAME)
                .build();
    }

    @Override
    public Jugador buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Jugador(
                UUID.fromString(rs.getString(ID_FIELD_NAME)),
                rs.getString("nombre"),
                rs.getDouble("pixelcoins"),
                rs.getInt("nventas"),
                rs.getDouble("ingresos"),
                rs.getDouble("gastos"),
                rs.getInt("ninpagosDeuda"),
                rs.getInt("npagosDeuda"),
                rs.getInt("numeroVerificacionCuenta")
        );
    }

}
