package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.menus.menus.BolsaCarteraMenu;
import org.bukkit.entity.Player;

public class CarteraBolsa extends BolsaSubCommand {
    private final String SCNombre = "cartera";
    private final String sintaxis = "/bolsa cartera";
    private final String ayuda = "ver todas las acciones que tienes en cartera";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        BolsaCarteraMenu menu = new BolsaCarteraMenu(player);
        menu.openMenu();
    }
}