package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.comandos.SubComando;
import es.serversurvival.objetos.mySQL.LlamadasApi;
import es.serversurvival.objetos.mySQL.PosicionesAbiertas;
import es.serversurvival.objetos.mySQL.PosicionesCerradas;

public abstract class BolsaSubCommand extends SubComando {
    protected PosicionesAbiertas posicionesAbiertasMySQL = new PosicionesAbiertas();
    protected PosicionesCerradas posicionesCerradasMySQL = new PosicionesCerradas();
    protected LlamadasApi llamadasApiMySQL = new LlamadasApi();

    private final String CNombre = "bolsa";

    public String getCNombre() {
        return CNombre;
    }
}