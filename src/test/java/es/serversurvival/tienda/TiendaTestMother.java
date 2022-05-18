package es.serversurvival.tienda;

import es.serversurvival.tienda._shared.domain.TiendaObjeto;
import org.bukkit.Material;

import java.util.UUID;

public final class TiendaTestMother {
    public static TiendaObjeto createTiendaObjeto(String jugador){
        return new TiendaObjeto(UUID.randomUUID(), jugador, Material.GREEN_WOOL.toString(), 1, 1, 1, null);
    }

    public static TiendaObjeto createTiendaObjeto(String jugador, double pixelcions){
        return new TiendaObjeto(UUID.randomUUID(), jugador, Material.GREEN_WOOL.toString(), 1, 5, 1, null);
    }
}
