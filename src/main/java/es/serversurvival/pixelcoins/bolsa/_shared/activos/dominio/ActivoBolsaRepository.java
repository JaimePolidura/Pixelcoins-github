package es.serversurvival.pixelcoins.bolsa._shared.activos.dominio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActivoBolsaRepository {
    void save(ActivoBolsa activoBolsa);

    Optional<ActivoBolsa> findById(UUID activoInfoId);

    Optional<ActivoBolsa> findByNombreCortoAndTipoActivo(String nombreCorto, TipoActivoBolsa tipoActivo);

    List<ActivoBolsa> findByTipo(TipoActivoBolsa tipoActivoBolsa);

    List<ActivoBolsa> findAllNReferenciasMayorQue0();
}
