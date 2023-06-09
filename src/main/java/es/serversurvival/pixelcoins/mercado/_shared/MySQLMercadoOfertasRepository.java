package es.serversurvival.pixelcoins.mercado._shared;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.delete.Delete;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.ResultsetObjetBuilder;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoPrimario;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoSecundario;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoEmision;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoJugador;
import es.serversurvival.pixelcoins.tienda._shared.OfertaTiendaItemMinecraft;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public final class MySQLMercadoOfertasRepository extends DataBaseRepository<Oferta, UUID> implements MercadoOfertasRepository {
    private static final String TABLE_NAME = "mercados";
    private static final String FIELD_ID = "ofertaId";

    private final DependenciesRepository dependencies;

    protected MySQLMercadoOfertasRepository(DatabaseConfiguration databaseConnection, DependenciesRepository dependencies) {
        super(databaseConnection);
        this.dependencies = dependencies;
    }

    @Override
    public <T extends Oferta> void save(T oferta) {
        super.save(oferta);
    }

    @Override
    public Optional<Oferta> findById(UUID ofertaId) {
        return super.findById(ofertaId);
    }

    @Override
    public Optional<Oferta> findByObjetoAndTipo(String objeto, TipoOferta tipo) {
        return super.buildObjectFromQuery(Select.from(TABLE_NAME)
                .where("objeto").equal(objeto)
                .and("tipo").equal(tipo));
    }

    @Override
    public List<Oferta> findByTipo(TipoOferta... tipoOfertas) {
        return buildListFromQuery(String.format("SELECT * FROM %s WHERE tipo IN (%s)", TABLE_NAME, Arrays.stream(tipoOfertas)
                .map(TipoOferta::toString)
                .collect(Collectors.joining(","))));
    }

    @Override
    public void deleteById(UUID ofertaId) {
        super.deleteById(ofertaId);
    }

    @Override
    public void deleteByObjetoYTipo(String objeto, TipoOferta tipo) {
        super.execute(Delete.from(TABLE_NAME).where("objeto").equal(objeto).and("tipo").equal(tipo));
    }

    @Override
    protected EntityMapper<Oferta> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .idField(FIELD_ID)
                .classesToMap(
                        OfertaAccionMercadoEmision.class,
                        OfertaAccionMercadoJugador.class,
                        OfertaDeudaMercadoPrimario.class,
                        OfertaDeudaMercadoSecundario.class,
                        OfertaTiendaItemMinecraft.class)
                .build();
    }

    @Override
    public Oferta buildObjectFromResultSet(ResultSet rs) throws SQLException {
        TipoOferta tipoOferta = TipoOferta.valueOf(rs.getString("tipo"));

        return (Oferta) dependencies.filterByImplementsInterfaceWithGeneric(ResultsetObjetBuilder.class, tipoOferta.getOfertaClass())
                .orElseThrow(() -> new ResourceNotFound("ResultsetObjetBuilder no registrado para oferta " + tipoOferta))
                .build(rs);
    }
}
