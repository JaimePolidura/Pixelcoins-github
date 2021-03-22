package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.comandos.SubComando;
import es.serversurvival.objetos.mySQL.Deudas;

public abstract class DeudasSubCommand extends SubComando {
    protected Deudas deudasMySQL = new Deudas();
    private final String cnombre = "deudas";

    public String getCNombre() {
        return cnombre;
    }
}