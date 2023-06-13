package es.serversurvival.pixelcoins.empresas._shared.empleados.infrastructure;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.EmpleadosRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLEmpleadosRepository extends DataBaseRepository<Empleado, UUID> implements EmpleadosRepository {
    private static final String FIELD_ID = "empleadoId";
    private static final String TABLE_NAME = "empresas_empleados";

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
    public List<Empleado> findByEmpleadoJugadorId(UUID empleadoJugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("empleadoJugadorId").equal(empleadoJugadorId));
    }

    @Override
    public List<Empleado> findByEmpresaIdAndEmpleadoJugadorId(UUID empresaId, UUID empleadoJugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME)
                .where("empleadoJugadorId").equal(empleadoJugadorId)
                .and("empresaId").equal(empresaId));
    }

    @Override
    public List<Empleado> findByEmpresaId(UUID empresaId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("empresaId").equal(empresaId));
    }

    @Override
    public void deleteById(UUID empleadoId) {
        super.deleteById(empleadoId);
    }

    @Override
    protected EntityMapper<Empleado> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .idField(FIELD_ID)
                .classesToMap(Empleado.class)
                .build();
    }

    @Override
    public Empleado buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Empleado(
                UUID.fromString(rs.getString("empleadoId")),
                UUID.fromString(rs.getString("empleadoJugadorId")),
                UUID.fromString(rs.getString("empresaId")),
                rs.getString("descripccion"),
                rs.getDouble("sueldo"),
                rs.getLong("periodoPagoMs"),
                rs.getString("fechaUltimoPago") == null ? rs.getTimestamp("fechaUltimoPago").toLocalDateTime() : null,
                rs.getTimestamp("fechaContratacion").toLocalDateTime(),
                rs.getBoolean("estaContratado"),
                rs.getString("fechaDespido") == null ? rs.getTimestamp("fechaDespido").toLocalDateTime() : null,
                rs.getString("causaDespido")
        );
    }
}
