package es.serversurvival.pixelcoins.empresas._shared.empresas.infrastructure;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.EmpresasRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLEmpresasRepository extends DataBaseRepository<Empresa, UUID> implements EmpresasRepository {
    private static final String FIELD_ID = "empresaId";
    private static final String TABLE_NAME = "empresas";

    protected MySQLEmpresasRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(Empresa empresa) {
        super.save(empresa);
    }

    @Override
    public Optional<Empresa> findById(UUID empresaId) {
        return super.findById(empresaId);
    }

    @Override
    public Optional<Empresa> findByNombre(String nombre) {
        return super.buildObjectFromQuery(Select.from(TABLE_NAME).where("nombre").equal(nombre));
    }

    @Override
    public List<Empresa> findAll() {
        return super.all();
    }

    @Override
    protected EntityMapper<Empresa> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .idField(FIELD_ID)
                .classesToMap(Empresa.class)
                .build();
    }

    @Override
    public Empresa buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Empresa(
                UUID.fromString(rs.getString("empresaId")),
                rs.getString("nombre"),
                UUID.fromString(rs.getString("fundadorJugadorId")),
                UUID.fromString(rs.getString("directorJugadorId")),
                rs.getString("descripcion"),
                rs.getString("icono"),
                rs.getInt("nTotalAcciones"),
                rs.getTimestamp("fechaCreacion").toLocalDateTime(),
                rs.getBoolean("esCotizada"),
                rs.getBoolean("estaCerrado"),
                rs.getString("fechaCerrado") != null ? rs.getTimestamp("fechaCerrado").toLocalDateTime() : null
        );
    }
}
