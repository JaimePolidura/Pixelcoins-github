package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.comandos.SubComando;
import es.serversurvival.mySQL.LlamadasApi;
import es.serversurvival.mySQL.PosicionesAbiertas;
import es.serversurvival.mySQL.PosicionesCerradas;

public abstract class BolsaSubCommand extends SubComando {
    private final String CNombre = "bolsa";

    public String getCNombre() {
        return CNombre;
    }
}