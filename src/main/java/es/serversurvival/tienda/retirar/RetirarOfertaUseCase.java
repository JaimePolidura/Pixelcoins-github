package es.serversurvival.tienda.retirar;

import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.ItemsUtils;
import es.serversurvival.tienda._shared.application.TiendaService;
import es.serversurvival.tienda._shared.domain.TiendaObjeto;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public final class RetirarOfertaUseCase {
    private final TiendaService tiendaService;

    public RetirarOfertaUseCase() {
        this.tiendaService = DependecyContainer.get(TiendaService.class);
    }

    public ItemStack retirarOferta(String jugador, UUID objetoTiendaIdARetirar) {
        TiendaObjeto ofertaARetirar = this.tiendaService.getById(objetoTiendaIdARetirar);
        this.ensureOwnerOfItemTienda(ofertaARetirar, jugador);

        ItemStack itemARetirar = ItemsUtils.getItemStakcByTiendaObjeto(ofertaARetirar);

        this.tiendaService.deleteById(objetoTiendaIdARetirar);

        return itemARetirar;
    }

    private void ensureOwnerOfItemTienda(TiendaObjeto itemTienda, String jugadorNombre){
        if(!itemTienda.getJugador().equals(jugadorNombre))
            throw new NotTheOwner("No eres el owner de ese objeto de la tienda");
    }
}
