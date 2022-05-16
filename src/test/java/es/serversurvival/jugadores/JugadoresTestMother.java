package es.serversurvival.jugadores;

import es.serversurvival.jugadores._shared.domain.Jugador;

import java.util.UUID;

public class JugadoresTestMother {
    public Jugador createJugador(String nombre){
        return new Jugador(UUID.randomUUID(), nombre, 1, 1, 1, 1, 1, 1, 1);
    }

    public Jugador createJugador(String nombre, double pixelcoins){
        return new Jugador(UUID.randomUUID(), nombre, pixelcoins, 1, 1, 1, 1, 1, 1);
    }
}
