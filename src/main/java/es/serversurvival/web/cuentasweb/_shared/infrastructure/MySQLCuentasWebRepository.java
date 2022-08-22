package es.serversurvival.web.cuentasweb._shared.infrastructure;

import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.web.cuentasweb._shared.domain.CuentaWeb;
import es.serversurvival.web.cuentasweb._shared.domain.CuentasWebRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public final class MySQLCuentasWebRepository extends DataBaseRepository<CuentaWeb, UUID> implements CuentasWebRepository {
    private static final String TABLE_NAME = "cuentasWeb";
    private static final String ID_FIELD_NAME = "cuentaWebId";

    public MySQLCuentasWebRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(CuentaWeb cuenta) {
        super.save(cuenta);
    }

    @Override
    public Optional<CuentaWeb> findById(UUID id) {
        return super.findById(id);
    }

    @Override
    public Optional<CuentaWeb> findByUsername(String username) {
        return super.buildObjectFromQuery(Select.from(TABLE_NAME).where("username").equal(username));
    }

    @Override
    protected EntityMapper<CuentaWeb> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .idField(ID_FIELD_NAME)
                .classesToMap(CuentaWeb.class)
                .build();
    }

    @Override
    public CuentaWeb buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new CuentaWeb(
                UUID.fromString(rs.getString(ID_FIELD_NAME)),
                rs.getString("username"),
                rs.getString("password")
        );
    }
}
