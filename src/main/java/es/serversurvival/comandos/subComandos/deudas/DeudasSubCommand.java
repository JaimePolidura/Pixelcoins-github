package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.comandos.SubComando;
import es.serversurvival.mySQL.Deudas;

public abstract class DeudasSubCommand extends SubComando {
    private final String cnombre = "deudas";

    public String getCNombre() {
        return cnombre;
    }
}