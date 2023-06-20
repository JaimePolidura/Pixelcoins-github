package es.serversurvival.minecraftserver._shared.menus;

import es.bukkitbettermenus.BukkitBetterMenus;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.dependencyinjector.dependencies.annotations.Configuration;
import es.dependencyinjector.providers.Provider;

@Configuration
public final class MenuInstancesProvider {
    @Provider
    public MenuService menuService() {
        return BukkitBetterMenus.MENU_SERVICE;
    }

    @Provider
    public SyncMenuService syncMenuService() {
        return BukkitBetterMenus.SYNC_MENU_SERVICE;
    }
}
