package es.serversurvival.bolsa.activosinfo._shared.infrastructure;

import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.delete.Delete;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfoRepository;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public final class MySQLActivoInfoRepository extends DataBaseRepository<ActivoInfo, String> implements ActivoInfoRepository{
    public static final String TABLE_NAME = "activosInfo";
    public static final String ID_FIELD_NAME = "nombreActivo";

    public MySQLActivoInfoRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(ActivoInfo activoApiCache) {
        super.save(activoApiCache);
    }

    @Override
    public Optional<ActivoInfo> findByNombreActivo(String nombreActivo) {
        return super.findById(nombreActivo);
    }

    @Override
    public List<ActivoInfo> findAll() {
        return super.all();
    }

    @Override
    public void deleteByNombreActivo(String nombreActivo) {
        super.execute(Delete.from(TABLE_NAME).where(ID_FIELD_NAME).equal(nombreActivo));
    }

    @Override
    protected EntityMapper<ActivoInfo> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .idField(ID_FIELD_NAME)
                .classToMap(ActivoInfo.class)
                .build();
    }

    @Override
    public ActivoInfo buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new ActivoInfo(
                rs.getString(ID_FIELD_NAME),
                rs.getDouble("precio"),
                SupportedTipoActivo.valueOf(rs.getString("tipoActivo")),
                rs.getString("nombreActivoLargo")
        );
    }
}
