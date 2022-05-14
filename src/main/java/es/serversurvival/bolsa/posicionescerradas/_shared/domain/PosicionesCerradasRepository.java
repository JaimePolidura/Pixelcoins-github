package es.serversurvival.bolsa.posicionescerradas._shared.domain;

import java.util.List;

public interface PosicionesCerradasRepository {
    void save(PosicionCerrada posicionCerrada);

    List<PosicionCerrada> findByJugador(String jugador);

    List<PosicionCerrada> findAll();
}
