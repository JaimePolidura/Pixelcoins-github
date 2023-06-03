package es.serversurvival.v2.pixelcoins.tienda.comprar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.mercado._shared.accion.OfertaCompradaListener;
import es.serversurvival.v2.pixelcoins.tienda._shared.OfertaTiendaItemMinecraft;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@EventHandler
@AllArgsConstructor
public final class ComprarTiendaItemMinecraftListener implements OfertaCompradaListener<OfertaTiendaItemMinecraft> {
    private final EventBus eventBus;

    @Override
    public void on(OfertaTiendaItemMinecraft ofertaComprada, UUID compradorId) {
        ItemStack item = ofertaComprada.toItemStack();

        Player player = Bukkit.getPlayer(compradorId);
        if(player != null){
            player.getInventory().addItem(item);
        }

        eventBus.publish(new TiendaItemMinecraftVendido(compradorId, ofertaComprada.getVendedorId(), ofertaComprada.getPrecio()));
    }
}
