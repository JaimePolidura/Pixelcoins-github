package es.serversurvival.v1.deudas._shared.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeudasRepository {
    void save(Deuda deuda);

    Optional<Deuda> findById(UUID id);

    List<Deuda> findByAcredor(String acredor);

    List<Deuda> findByDeudor(String deudor);

    List<Deuda> findAll();

    void deleteById(UUID id);
}
