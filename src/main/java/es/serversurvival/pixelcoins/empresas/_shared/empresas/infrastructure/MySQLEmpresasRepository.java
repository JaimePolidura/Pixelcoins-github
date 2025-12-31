package es.serversurvival.pixelcoins.empresas._shared.empresas.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.EmpresasRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MySQLRepository
public final class MySQLEmpresasRepository extends Repository<Empresa, UUID, Object> implements EmpresasRepository {
    private static final String FIELD_ID = "empresaId";
    private static final String TABLE_NAME = "empresas";

    public MySQLEmpresasRepository(ConnectionManager connectionManager) {
        super(connectionManager);
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
    public List<Empresa> findByDirectorJugadorId(UUID directorJugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("directorJugadorId").equal(directorJugadorId));
    }

    @Override
    public List<Empresa> findAll() {
        return super.findAll();
    }

    @Override
    public EntityMapper<Empresa, Object> entityMapper() {
        return EntityMapper.builder()
                .table(TABLE_NAME)
                .idField(FIELD_ID)
                .classesToMap(Empresa.class)
                .build();
    }
}
