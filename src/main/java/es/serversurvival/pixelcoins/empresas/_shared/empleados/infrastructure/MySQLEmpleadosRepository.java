package es.serversurvival.pixelcoins.empresas._shared.empleados.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.EmpleadosRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MySQLRepository
public final class MySQLEmpleadosRepository extends Repository<Empleado, UUID, Object> implements EmpleadosRepository {
    private static final String FIELD_ID = "empleadoId";
    private static final String TABLE_NAME = "empresas_empleados";

    public MySQLEmpleadosRepository(ConnectionManager connectionManager) {
        super(connectionManager);
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
    public EntityMapper<Empleado, Object> entityMapper() {
        return EntityMapper.builder()
                .idField(FIELD_ID)
                .classesToMap(Empleado.class)
                .table(TABLE_NAME)
                .build();
    }
}
