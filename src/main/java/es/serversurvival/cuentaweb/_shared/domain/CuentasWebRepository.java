package es.serversurvival.cuentaweb._shared.domain;

import java.util.Optional;
import java.util.UUID;

public interface CuentasWebRepository {
    void save(CuentaWeb cuenta);

    Optional<CuentaWeb> findByUsername(String username);

    Optional<CuentaWeb> findById(UUID id);
}
