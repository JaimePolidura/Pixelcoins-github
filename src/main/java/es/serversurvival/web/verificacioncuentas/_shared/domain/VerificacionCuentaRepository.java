package es.serversurvival.web.verificacioncuentas._shared.domain;

import java.util.Optional;

public interface VerificacionCuentaRepository {
    void save(VerificacionCuenta verificacionCuenta);

    Optional<VerificacionCuenta> findByJugador(String jugaor);

    void deleteByJugador(String jugador);
}
