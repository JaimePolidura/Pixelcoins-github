package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.menus.menus.PerfilMenu;
import org.bukkit.entity.Player;

public class Perfil extends Comando {
    private final String cnombre = "perfil";
    private final String sintaxis = "/perfil";
    private final String ayuda = "ver todos tus datos";

    @Override
    public String getCNombre() {
        return cnombre;
    }

    @Override
    public String getSintaxis() {
        return sintaxis;
    }

    @Override
    public String getAyuda() {
        return ayuda;
    }

    @Override
    public void execute(Player player, String[] args) {
        PerfilMenu perfilMenu = new PerfilMenu(player);
        perfilMenu.openMenu();
    }
}