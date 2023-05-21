package es.serversurvival.v2.pixelcoins.tienda.retirar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.v2.pixelcoins.mercado._shared.accion.CustomOfertaRetiradaListener;
import es.serversurvival.v2.pixelcoins.tienda._shared.OfertaTiendaItemMinecraft;
import es.serversurvival.v2.pixelcoins.tienda._shared.TiendaItemMinecraftValidator;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@EventHandler
@AllArgsConstructor
public final class RetirarTiendaItemMinecraftListener implements CustomOfertaRetiradaListener<OfertaTiendaItemMinecraft> {
    @Override
    public void on(OfertaTiendaItemMinecraft ofertaRetirada, UUID retiradorJugadorId) {
        ItemStack itemOferta = ofertaRetirada.toItemStack();
        Player player = Bukkit.getPlayer(retiradorJugadorId);

        if(player != null){
            player.getInventory().addItem(itemOferta);
        }
    }
}
