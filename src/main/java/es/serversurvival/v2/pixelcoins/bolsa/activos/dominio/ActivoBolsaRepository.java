package es.serversurvival.v2.pixelcoins.bolsa.activos.dominio;

import java.util.Optional;

public interface ActivoBolsaRepository {
    void save(ActivoBolsa activoBolsa);

    Optional<ActivoBolsa> findById(int activoInfoId);

    Optional<ActivoBolsa> findByNombreCortoAndTipoActivo(String nombreCorto, TipoActivoBolsa tipoActivo);
}
