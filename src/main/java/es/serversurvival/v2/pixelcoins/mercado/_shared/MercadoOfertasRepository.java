package es.serversurvival.v2.pixelcoins.mercado._shared;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MercadoOfertasRepository {
    <T extends Oferta> void save(T oferta);

    Optional<Oferta> findById(UUID ofertaId);

    Optional<Oferta> findByObjetoAndTipo(String objeto, TipoOferta tipo);

    List<Oferta> findByTipo(TipoOferta tipoOferta);

    void deleteById(UUID ofertaId);

    void deleteByObjetoYTipo(String objeto, TipoOferta tipo);
}
