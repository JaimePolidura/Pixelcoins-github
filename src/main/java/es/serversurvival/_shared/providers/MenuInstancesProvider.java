package es.serversurvival._shared.providers;

import es.bukkitclassmapper._shared.utils.ClassMapperInstanceProvider;
import es.bukkitclassmapper.menus.MenuService;
import es.bukkitclassmapper.menus.modules.sync.SyncMenuService;
import es.dependencyinjector.annotations.Configuration;
import es.dependencyinjector.annotations.Provider;

@Configuration
public final class MenuInstancesProvider {
    @Provider
    public MenuService menuService() {
        return ClassMapperInstanceProvider.MENU_SERVICE;
    }

    @Provider
    public SyncMenuService syncMenuService() {
        return ClassMapperInstanceProvider.SYNC_MENU_SERVICE;
    }
}
