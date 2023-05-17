package es.serversurvival.v2.pixelcoins.empresas._shared.accionistas;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccionistaEmpresaRepository {
    void save(AccionistaEmpresa empresa);

    Optional<AccionistaEmpresa> findById(UUID accionistaId);

    List<AccionistaEmpresa> findByJugadorId(UUID jugadorId);

    List<AccionistaEmpresa> findByEmpresaId(UUID empresaId);

    List<AccionistaEmpresa> findAll();

    void deleteByEmpresaId(UUID empresaId);
}
