package es.serversurvival._shared.providers;

import es.bukkitbettermenus.BetterMenusInstanceProvider;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.dependencyinjector.dependencies.annotations.Configuration;
import es.dependencyinjector.providers.Provider;

@Configuration
public final class MenuInstancesProvider {
    @Provider
    public MenuService menuService() {
        return BetterMenusInstanceProvider.MENU_SERVICE;
    }

    @Provider
    public SyncMenuService syncMenuService() {
        return BetterMenusInstanceProvider.SYNC_MENU_SERVICE;
    }
}
