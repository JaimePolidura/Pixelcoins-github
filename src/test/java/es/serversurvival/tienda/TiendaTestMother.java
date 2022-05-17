package es.serversurvival.tienda;

import es.serversurvival.tienda._shared.domain.TiendaObjeto;
import org.bukkit.Material;

import java.util.UUID;

public class TiendaTestMother {
    public TiendaObjeto createTiendaObjeto(String jugador){
        return new TiendaObjeto(UUID.randomUUID(), jugador, Material.GREEN_WOOL.toString(), 1, 1, 1, null);
    }
}
