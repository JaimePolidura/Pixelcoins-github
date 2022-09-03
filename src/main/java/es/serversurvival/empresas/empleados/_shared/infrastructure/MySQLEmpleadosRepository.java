package es.serversurvival.empresas.empleados._shared.infrastructure;

import es.dependencyinjector.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.empresas.empleados._shared.domain.EmpleadosRepository;
import es.serversurvival.empresas.empleados._shared.domain.TipoSueldo;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLEmpleadosRepository extends DataBaseRepository<Empleado, UUID> implements EmpleadosRepository {
    private final String TABLE_NAME = "empleados";
    private final String ID_FIELD_NAME = "empleadoId";

    public MySQLEmpleadosRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(Empleado empleado) {
        super.save(empleado);
    }

    @Override
    public Optional<Empleado> findById(UUID empleadoId) {
        return super.findById(empleadoId);
    }

    @Override
    public List<Empleado> findByJugador(String nombre) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("nombre").equal(nombre));
    }

    @Override
    public List<Empleado> findByEmpresa(String empresa) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("empresa").equal(empresa));
    }

    @Override
    public List<Empleado> findAll() {
        return super.all();
    }

    @Override
    public void deleteById(UUID id) {
        super.deleteById(id);
    }

    @Override
    protected EntityMapper<Empleado> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .classesToMap(Empleado.class)
                .idField(ID_FIELD_NAME)
                .build();
    }

    @Override
    public Empleado buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Empleado(
                UUID.fromString(rs.getString(ID_FIELD_NAME)),
                rs.getString("nombre"),
                rs.getString("empresa"),
                rs.getDouble("sueldo"),
                rs.getString("cargo"),
                TipoSueldo.valueOf(rs.getString("tipoSueldo")),
                rs.getString("fechaUltimapaga")
        );
    }
}
