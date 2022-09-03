package es.serversurvival.tienda._shared.infrastructure;

import com.fasterxml.jackson.core.type.TypeReference;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.dependencyinjector.annotations.Repository;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.tienda._shared.domain.EncantamientoObjecto;
import es.serversurvival.tienda._shared.domain.TiendaRepository;
import es.serversurvival.tienda._shared.domain.TiendaObjeto;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLTiendaRepository extends DataBaseRepository<TiendaObjeto, UUID> implements TiendaRepository {
    private static final String DB_NAME = "tienda";
    private static final String ID_FIELD_NAME = "tiendaObjetoId";

    public MySQLTiendaRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(TiendaObjeto tiendaObjeto) {
        super.save(tiendaObjeto);
    }

    @Override
    public Optional<TiendaObjeto> findById(UUID id) {
        return super.findById(id);
    }

    @Override
    public List<TiendaObjeto> findByJugador(String jugador) {
        return buildListFromQuery(Select.from(DB_NAME).where("nombreAccionista").equal(jugador));
    }

    @Override
    public List<TiendaObjeto> findAll() {
        return super.all();
    }

    @Override
    public void deleteById(UUID id) {
        super.deleteById(id);
    }

    @Override
    protected EntityMapper<TiendaObjeto> entityMapper() {
        return EntityMapper.table(DB_NAME)
                .classesToMap(TiendaObjeto.class)
                .idField(ID_FIELD_NAME)
                .build();
    }
    
    @Override
    @SneakyThrows
    public TiendaObjeto buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new TiendaObjeto(
                UUID.fromString(rs.getString(ID_FIELD_NAME)),
                rs.getString("jugador"),
                rs.getString("objeto"),
                rs.getInt("cantidad"),
                rs.getDouble("precio"),
                rs.getInt("durabilidad"),
                encantamientosStringToObject(rs)
        );
    }

    @SneakyThrows
    private List<EncantamientoObjecto> encantamientosStringToObject(ResultSet rs){
        String jsonRawString = rs.getString("encantamientos");

        return Funciones.MAPPER.readValue(jsonRawString, new TypeReference<List<EncantamientoObjecto>>(){});
    }
}
