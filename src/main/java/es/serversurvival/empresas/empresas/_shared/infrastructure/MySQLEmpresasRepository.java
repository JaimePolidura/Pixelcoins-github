package es.serversurvival.empresas.empresas._shared.infrastructure;

import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.empresas._shared.domain.EmpresasRepostiory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class MySQLEmpresasRepository extends DataBaseRepository<Empresa, UUID> implements EmpresasRepostiory {
    private static final String TABLE_NAME = "empresas";
    private static final String ID_TABLE_NAME = "empresaId";

    public MySQLEmpresasRepository(DatabaseConfiguration databaseConnection) {
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
        return buildObjectFromQuery(Select.from(TABLE_NAME).where("nombre").equal(nombre));
    }

    @Override
    public List<Empresa> findByOwner(String owner) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("owner").equal(owner));
    }

    @Override
    public List<Empresa> findAll() {
        return super.all();
    }

    @Override
    public void deleteById(UUID empresaId) {
        super.deleteById(empresaId);
    }

    @Override
    protected EntityMapper<Empresa> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .idField(ID_TABLE_NAME)
                .classToMap(Empresa.class)
                .build();
    }

    @Override
    public Empresa buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Empresa(
                UUID.fromString(rs.getString(ID_TABLE_NAME)),
                rs.getString("nombre"),
                rs.getString("owner"),
                rs.getDouble("pixelcoins"),
                rs.getDouble("ingresos"),
                rs.getDouble("gastos"),
                rs.getString("icono"),
                rs.getString("descripcion"),
                rs.getBoolean("cotizada"),
                rs.getInt("accionesTotales")
        );}
}
