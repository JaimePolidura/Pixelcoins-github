package es.serversurvival.pixelcoins.bolsa._shared.activos.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsaRepository;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MySQLRepository
public final class MySQLActivoBolsaRepository extends Repository<ActivoBolsa, UUID, Object> implements ActivoBolsaRepository {
    private static final String TABLE_NAME = "bolsa_activos";
    private static final String FIELD_ID = "activoBolsaId";

    public MySQLActivoBolsaRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(ActivoBolsa activoBolsa) {
        super.save(activoBolsa);
    }

    @Override
    public Optional<ActivoBolsa> findById(UUID activoInfoId) {
        return super.findById(activoInfoId);
    }

    @Override
    public Optional<ActivoBolsa> findByNombreCortoAndTipoActivo(String nombreCorto, TipoActivoBolsa tipoActivo) {
        return super.buildObjectFromQuery(Select.from(TABLE_NAME)
                .where("nombreCorto").equal(nombreCorto)
                .and("tipoActivo").equal(tipoActivo));
    }

    @Override
    public List<ActivoBolsa> findByTipo(TipoActivoBolsa tipoActivoBolsa) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("tipoActivo").equal(tipoActivoBolsa));
    }

    @Override
    public EntityMapper<ActivoBolsa, Object> entityMapper() {
        return EntityMapper.builder()
                .classesToMap(ActivoBolsa.class)
                .idField(FIELD_ID)
                .table(TABLE_NAME)
                .build();
    }
}
