package es.serversurvival.bolsa.posicionescerradas._shared.infrastructure;

import es.dependencyinjector.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerrada;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionesCerradasRepository;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public final class MySQLPosicionesCerradasRepository extends DataBaseRepository<PosicionCerrada, UUID> implements PosicionesCerradasRepository {
    private static final String TABLE_NAME = "posicionesCerradas";
    private static final String ID_FIELD_NAME = "posicionCerradaId";

    public MySQLPosicionesCerradasRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(PosicionCerrada posicionCerrada) {
        super.save(posicionCerrada);
    }

    @Override
    public List<PosicionCerrada> findByJugador(String jugador) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("nombreAccionista").equal(jugador));
    }

    @Override
    public List<PosicionCerrada> findAll() {
        return super.all();
    }

    @Override
    protected EntityMapper<PosicionCerrada> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .idField(ID_FIELD_NAME)
                .classesToMap(PosicionCerrada.class)
                .build();
    }

    @Override
    public PosicionCerrada buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new PosicionCerrada(
                UUID.fromString(rs.getString(ID_FIELD_NAME)),
                rs.getString("nombreAccionista"),
                TipoActivo.valueOf(rs.getString("tipoActivo")),
                rs.getString("nombreActivo"),
                rs.getInt("cantidad"),
                rs.getDouble("precioApertura"),
                rs.getString("fechaApertura"),
                rs.getDouble("precioCierre"),
                rs.getString("fechaCierre"),
                TipoPosicion.valueOf(rs.getString("tipoPosicion"))
        );
    }
}
