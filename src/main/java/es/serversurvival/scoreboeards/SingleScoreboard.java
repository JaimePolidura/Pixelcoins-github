package es.serversurvival.scoreboeards;

import es.serversurvival.mySQL.*;
import es.serversurvival.util.Funciones;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;

public interface SingleScoreboard extends ServerScoreboard{
    Empresas empresasMySQL = Empresas.INSTANCE;
    Jugadores jugadoresMySQL = Jugadores.INSTANCE;
    PosicionesAbiertas posicionesAbiertasMySQL = PosicionesAbiertas.INSTANCE;
    LlamadasApi llamadasApiMySQL = LlamadasApi.INSTANCE;
    Deudas deudasMySQ = Deudas.INSTANCE;
    DecimalFormat formatea = Funciones.FORMATEA;

    Scoreboard createScoreborad(String player);
}
