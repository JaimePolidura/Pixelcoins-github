package es.serversurvival.pixelcoins.mercado._shared;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.ConditionalClassMapping;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.delete.Delete;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoPrimario;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoSecundario;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoEmision;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoJugador;
import es.serversurvival.pixelcoins.tienda._shared.OfertaTiendaItemMinecraft;

import java.util.*;
import java.util.stream.Collectors;

@MySQLRepository
public final class MySQLMercadoOfertasRepository extends Repository<Oferta, UUID, TipoOferta> implements MercadoOfertasRepository {
    private static final String TABLE_NAME = "mercado_ofertas";
    private static final String FIELD_ID = "ofertaId";

    public MySQLMercadoOfertasRepository(ConnectionManager connectionManager) {
        super(connectionManager);
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
                .map(string -> String.format("'%s'", string))
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
    public EntityMapper<Oferta, TipoOferta> entityMapper() {
        return EntityMapper.builder()
                .table(TABLE_NAME)
                .idField(FIELD_ID)
                .classesToMap(Oferta.class)
                .conditionalClassMapping(ConditionalClassMapping.<Oferta, TipoOferta>builder()
                        .typeValueAccessor(resultSet -> TipoOferta.valueOf(resultSet.getString("tipo")))
                        .typeClass(TipoOferta.class)
                        .entitiesTypeMapper(Map.of(
                                TipoOferta.DEUDA_MERCADO_SECUNDARIO, OfertaDeudaMercadoSecundario.class,
                                TipoOferta.ACCIONES_SERVER_EMISION, OfertaAccionMercadoEmision.class,
                                TipoOferta.ACCIONES_SERVER_JUGADOR, OfertaAccionMercadoJugador.class,
                                TipoOferta.DEUDA_MERCADO_PRIMARIO, OfertaDeudaMercadoPrimario.class,
                                TipoOferta.TIENDA_ITEM_MINECRAFT, OfertaTiendaItemMinecraft.class
                        ))
                        .build())
                .build();
    }
}
