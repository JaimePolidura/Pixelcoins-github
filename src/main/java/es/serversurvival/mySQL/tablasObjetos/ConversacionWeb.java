package es.serversurvival.mySQL.tablasObjetos;

public final class ConversacionWeb implements TablaObjeto{
    private final String web_nombre;
    private final String server_nombre;

    public ConversacionWeb(String web_nombre, String server_nombre) {
        this.web_nombre = web_nombre;
        this.server_nombre = server_nombre;
    }

    public String getWeb_nombre() {
        return web_nombre;
    }

    public String getServer_nombre() {
        return server_nombre;
    }
}
