package es.serversurvival.pixelcoins.empresas._shared.accionistas.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.delete.Delete;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MySQLRepository
public final class MySQLAccionistasEmpresasRepository extends Repository<AccionistaEmpresa, UUID, Object> implements AccionistaEmpresaRepository {
    private final static String FIELD_ID = "accionistaId";
    private final static String TABLE_NAME = "empresas_accionistas";

    public MySQLAccionistasEmpresasRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(AccionistaEmpresa empresa) {
        super.save(empresa);
    }

    @Override
    public Optional<AccionistaEmpresa> findById(UUID accionistaId) {
        return super.findById(accionistaId);
    }

    @Override
    public List<AccionistaEmpresa> findByJugadorId(UUID accionisaJugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("accionisaJugadorId").equal(accionisaJugadorId));
    }

    @Override
    public List<AccionistaEmpresa> findByEmpresaId(UUID empresaId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("empresaId").equal(empresaId));
    }

    @Override
    public Optional<AccionistaEmpresa> findByEmpresaIdAndJugadorId(UUID empresaId, UUID accionisaJugadorId) {
        return super.buildObjectFromQuery(Select.from(TABLE_NAME).where("empresaId")
                .equal(empresaId).and("accionisaJugadorId").equal(accionisaJugadorId));
    }

    @Override
    public List<AccionistaEmpresa> findAll() {
        return super.findAll();
    }

    @Override
    public void deleteByEmpresaId(UUID empresaId) {
        super.execute(Delete.from(TABLE_NAME).where("empresaId").equal(empresaId));
    }

    @Override
    public void deleteById(UUID accionistaId) {
        super.deleteById(accionistaId);
    }

    @Override
    public EntityMapper<AccionistaEmpresa, Object> entityMapper() {
        return EntityMapper.builder()
                .table(TABLE_NAME)
                .classesToMap(AccionistaEmpresa.class)
                .idField(FIELD_ID)
                .build();
    }
}
