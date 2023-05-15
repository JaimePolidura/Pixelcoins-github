package es.serversurvival.v1.empresas.ofertasaccionesserver._shared.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfertasAccionesServerRepository {
    void save(OfertaAccionServer ofertaMercadoServer);

    Optional<OfertaAccionServer> findById(UUID id);

    List<OfertaAccionServer> findAll();

    List<OfertaAccionServer> findByEmpresa(String empresa);

    List<OfertaAccionServer> findByOfertanteNombre(String ofertanteNombre);

    void deleteById(UUID id);
}
