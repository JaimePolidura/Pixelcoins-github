package es.serversurvival.deudas._shared.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeudasRepository {
    void save(Deuda deuda);

    Optional<Deuda> findByDeudaId(UUID id);

    List<Deuda> findDeudasByAcredor(String acredor);

    List<Deuda> findDeudasByDeudor(String deudor);

    List<Deuda> findAll();

    void deleteById(UUID id);
}
