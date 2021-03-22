package es.serversurvival.objetos.menus;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public abstract class Menu {
    public static final Set<Menu> activeMenus = new HashSet<>();

    public abstract Player getPlayer();
    public abstract String titulo();
    public abstract void openMenu();
    public abstract void closeMenu();

    public static Menu getByPlayer(Player player){
        for(Menu menu : activeMenus){
            if(menu.getPlayer().getName().equalsIgnoreCase(player.getName())){
                return menu;
            }
        }
        return null;
    }
}