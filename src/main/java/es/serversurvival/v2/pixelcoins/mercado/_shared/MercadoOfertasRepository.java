package es.serversurvival.v2.pixelcoins.mercado._shared;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MercadoOfertasRepository {
    void save(Oferta oferta);

    Optional<Oferta> findById(UUID ofertaId);

    List<Oferta> findByTipo(TipoOferta tipoOferta);

    Optional<Oferta> findByObjetoAndTipo(String objeto, TipoOferta tipo);

    void deleteById(UUID ofertaId);

    void deleteByObjetoYTipo(String objeto, TipoOferta tipo);
}
