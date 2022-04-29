package es.serversurvival.jugadores.setupjugadorunido;

import es.serversurvival.Pixelcoin;
import es.serversurvival.jugadores._shared.mySQL.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import org.bukkit.entity.Player;

public final class SetUpJugadorUseCase implements AllMySQLTablesInstances {
    public static final SetUpJugadorUseCase INSTANCE = new SetUpJugadorUseCase();

    private SetUpJugadorUseCase () {}

    public void setUpJugadorUnido (Player player) {
        Jugador jugadorPorUUID = jugadoresMySQL.getJugadorUUID(player.getUniqueId().toString());

        if(jugadorPorUUID == null){
            Jugador jugadorPorNombre = jugadoresMySQL.getJugador(player.getName());

            if(jugadorPorNombre == null){
                jugadoresMySQL.nuevoJugador(player.getName(), 0, 0, 0, 0, 0, 0, player.getUniqueId().toString());
            }else{
                jugadoresMySQL.setUuid(player.getName(), player.getUniqueId().toString());
            }
        }else{
            if(!player.getName().equalsIgnoreCase(jugadorPorUUID.getNombre())){
                jugadoresMySQL.cambiarNombreJugador(jugadorPorUUID.getNombre(), player.getName());

                Pixelcoin.publish(new JugadorCambiadoDeNombreEvento(jugadorPorUUID.getNombre(), player.getName()));
            }

            if(jugadorPorUUID.getNumeroVerificacionCuenta() == 0){
                jugadoresMySQL.setNumeroCuenta(player.getName(), jugadoresMySQL.generearNumeroCuenta());
            }
        }
    }
}
