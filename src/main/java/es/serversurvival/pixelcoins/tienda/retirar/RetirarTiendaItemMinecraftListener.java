package es.serversurvival.pixelcoins.tienda.retirar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.serversurvival.pixelcoins.mercado._shared.custom.accion.OfertaRetiradaListener;
import es.serversurvival.pixelcoins.tienda._shared.OfertaTiendaItemMinecraft;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@EventHandler
@AllArgsConstructor
public final class RetirarTiendaItemMinecraftListener implements OfertaRetiradaListener<OfertaTiendaItemMinecraft> {
    @Override
    public void on(OfertaTiendaItemMinecraft ofertaRetirada, UUID retiradorJugadorId) {
        ItemStack itemOferta = ofertaRetirada.toItemStack();
        Player player = Bukkit.getPlayer(retiradorJugadorId);

        if(player != null){
            player.getInventory().addItem(itemOferta);
        }
    }
}
