package es.serversurvival.pixelcoins.bolsa._shared.posiciones.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Order;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.Posicion;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.PosicionesRepository;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoPosicion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MySQLRepository
public final class MySQLPosicionesRepository extends Repository<Posicion, UUID, Object> implements PosicionesRepository {
    private static final String TABLE_NAME = "posiciones";
    private static final String FIELD_ID = "posicionId";

    public MySQLPosicionesRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(Posicion posicion) {
        super.save(posicion);
    }

    @Override
    public List<Posicion> findByJugadorId(UUID jugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("jugadorId").equal(jugadorId));
    }

    @Override
    public Optional<Posicion> findById(UUID posicionId) {
        return super.findById(posicionId);
    }

    @Override
    public void deleteById(UUID posicionId) {
        super.deleteById(posicionId);
    }

    @Override
    public List<Posicion> findPosicionesCerradasSortByRentabilidad() {
        return super.buildListFromQuery(Select.from(TABLE_NAME)
                .where("tipoPosicion").equal(TipoPosicion.CERRADO)
                .orderBy("rentabilidad", Order.DESC));
    }

    @Override
    public EntityMapper<Posicion, Object> entityMapper() {
        return EntityMapper.builder()
                .table(TABLE_NAME)
                .idField(FIELD_ID)
                .classesToMap(Posicion.class)
                .build();
    }
}
