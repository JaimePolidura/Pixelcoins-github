package es.serversurvival._shared.menus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class MenuManager {
    private MenuManager () {}

    private static final Map<String, Menu> menus = new HashMap<>();

    public static void nuevoMenu (String jugador, Menu menu) {
        menus.put(jugador, menu);
    }

    public static Menu getByPlayer(String nombreJugador) {
        return menus.get(nombreJugador);
    }

    public static void borrarMenu (String nombreJugador) {
        menus.remove(nombreJugador);
    }

    public static Map<String, Menu> getCopyOfAllMenus() {
        return new HashMap<>(menus);
    }

    public static void forEach (BiConsumer<? super String, ? super Menu> actionToPerform) {
        getCopyOfAllMenus().forEach(actionToPerform);
    }
}
