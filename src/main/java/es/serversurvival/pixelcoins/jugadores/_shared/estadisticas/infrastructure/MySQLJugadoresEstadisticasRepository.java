package es.serversurvival.pixelcoins.jugadores._shared.estadisticas.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Order;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.domain.JugadorEstadisticas;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.domain.JugadorTipoContadorEstadistica;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.domain.JugadoresEstadisticasRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MySQLRepository
public final class MySQLJugadoresEstadisticasRepository extends Repository<JugadorEstadisticas, UUID, Object> implements JugadoresEstadisticasRepository {
    private static final String TABLE_NAME = "jugadores_estadisticas";
    private static final String ID_FIELD = "jugadorId";

    public MySQLJugadoresEstadisticasRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(JugadorEstadisticas estadisticas) {
        super.save(estadisticas);
    }

    @Override
    public Optional<JugadorEstadisticas> findById(UUID jugadorId) {
        return super.findById(jugadorId);
    }

    @Override
    public List<JugadorEstadisticas> sortBy(JugadorTipoContadorEstadistica tipo, boolean creciente) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).orderBy(tipo.getNombreCampo(), creciente ? Order.ASC : Order.DESC));
    }

    @Override
    public EntityMapper<JugadorEstadisticas, Object> entityMapper() {
        return EntityMapper.builder()
                .classesToMap(JugadorEstadisticas.class)
                .idField(ID_FIELD)
                .table(TABLE_NAME)
                .build();
    }
}
