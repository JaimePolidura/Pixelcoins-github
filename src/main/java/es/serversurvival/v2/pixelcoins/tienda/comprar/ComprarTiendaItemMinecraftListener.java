package es.serversurvival.v2.pixelcoins.tienda.comprar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.serversurvival.v2.pixelcoins.mercado._shared.accion.OfertaCompradaListener;
import es.serversurvival.v2.pixelcoins.tienda._shared.OfertaTiendaItemMinecraft;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@EventHandler
public final class ComprarTiendaItemMinecraftListener implements OfertaCompradaListener<OfertaTiendaItemMinecraft> {
    @Override
    public void on(OfertaTiendaItemMinecraft ofertaComprada, UUID compradorId) {
        ItemStack item = ofertaComprada.toItemStack();

        Player player = Bukkit.getPlayer(compradorId);
        if(player != null){
            player.getInventory().addItem(item);
        }
    }
}
