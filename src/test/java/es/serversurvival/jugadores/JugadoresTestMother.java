package es.serversurvival.jugadores;

import es.serversurvival.jugadores._shared.domain.Jugador;

import java.util.UUID;

public final class JugadoresTestMother {
    public static Jugador createJugador(String nombre){
        return new Jugador(UUID.randomUUID(), nombre, 0, 0, 0, 0, 0, 0, 0);
    }

    public static Jugador createJugador(String nombre, double pixelcoins){
        return new Jugador(UUID.randomUUID(), nombre, pixelcoins, 0, 0, 0, 0, 0, 0);
    }
}
