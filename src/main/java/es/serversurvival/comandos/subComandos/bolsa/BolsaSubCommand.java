package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.comandos.SubComando;

public abstract class BolsaSubCommand extends SubComando {
    private final String CNombre = "bolsa";

    public String getCNombre() {
        return CNombre;
    }
}