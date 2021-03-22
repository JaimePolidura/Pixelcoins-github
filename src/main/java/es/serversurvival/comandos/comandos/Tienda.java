package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.menus.menus.OfertasMenu;
import org.bukkit.entity.Player;

public class Tienda extends Comando {
    private final String CNombre = "tienda";
    private final String sintaxis = "/tienda";
    private final String ayuda = "mostrar la tienda de objetos";

    public String getCNombre() {
        return CNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player p, String[] args){
        OfertasMenu ofertasMenu = new OfertasMenu(p);
        ofertasMenu.openMenu();
    }
}