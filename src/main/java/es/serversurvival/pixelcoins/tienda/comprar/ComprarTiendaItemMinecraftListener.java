package es.serversurvival.pixelcoins.tienda.comprar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival._shared.ConfigurationVariables;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.pixelcoins.mercado._shared.custom.accion.OfertaCompradaListener;
import es.serversurvival.pixelcoins.tienda._shared.OfertaTiendaItemMinecraft;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

@EventHandler
@AllArgsConstructor
public final class ComprarTiendaItemMinecraftListener implements OfertaCompradaListener<OfertaTiendaItemMinecraft> {
    private final EventBus eventBus;

    @Override
    public void on(OfertaTiendaItemMinecraft ofertaComprada, UUID compradorId) {
        Player comprador = Bukkit.getPlayer(compradorId);
        ItemStack item = ofertaComprada.toItemStack();
        item.setAmount(1);
        item = setMarcaDeAguaItem(item);

        MinecraftUtils.darItem(comprador, item);

        eventBus.publish(new TiendaItemMinecraftVendido(compradorId, ofertaComprada.getVendedorId(), ofertaComprada.getPrecio()));
    }

    private ItemStack setMarcaDeAguaItem(ItemStack item) {
        return MinecraftUtils.setLore(item, List.of(ConfigurationVariables.TIENDA_MARCA_DE_AGUA));
    }
}
