package es.serversurvival.v1.tienda.retirar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.v1._shared.utils.ItemsUtils;
import es.serversurvival.v1.tienda._shared.application.TiendaService;
import es.serversurvival.v1.tienda._shared.domain.TiendaObjeto;
import lombok.AllArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class RetirarOfertaUseCase {
    private final TiendaService tiendaService;

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
