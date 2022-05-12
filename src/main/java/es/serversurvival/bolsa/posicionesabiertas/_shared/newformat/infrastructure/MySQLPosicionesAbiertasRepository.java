package es.serversurvival.bolsa.posicionesabiertas._shared.newformat.infrastructure;

import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.other._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.bolsa.posicionesabiertas._shared.newformat.domain.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas._shared.newformat.domain.PosicionesAbiertasRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class MySQLPosicionesAbiertasRepository extends DataBaseRepository<PosicionAbierta, UUID> implements PosicionesAbiertasRepository {
    private static final String TABLE_NAME = "posicionesabiertas";
    private static final String ID_FIELD_NAME = "posicionAbiertId";

    public MySQLPosicionesAbiertasRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(PosicionAbierta posicionAbierta) {
        super.save(posicionAbierta);
    }

    @Override
    public Optional<PosicionAbierta> findById(UUID posicionAbiertaId) {
        return super.findById(posicionAbiertaId);
    }

    @Override
    public List<PosicionAbierta> findByJugador(String jugador) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("jugador").equal(jugador));
    }

    @Override
    public List<PosicionAbierta> findAll() {
        return super.all();
    }

    @Override
    public void deleteById(UUID posicionAbiertaId) {
        super.deleteById(posicionAbiertaId);
    }

    @Override
    protected EntityMapper<PosicionAbierta> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .classToMap(PosicionAbierta.class)
                .idField(ID_FIELD_NAME)
                .build();
    }

    @Override
    public PosicionAbierta buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new PosicionAbierta(
                UUID.fromString(rs.getString("posicionAbiertaId")),
                rs.getString("jugador"),
                TipoActivo.valueOf(rs.getString("tipoActivo")),
                rs.getString("nombreActivo"),
                rs.getInt("cantidad"),
                rs.getDouble("precioApertura"),
                rs.getString("fechaApertura"),
                TipoPosicion.valueOf(rs.getString("tipoPosicion"))
        );
    }
}
