package es.serversurvival.minecraftserver.lootbox.comprar;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.utils.ItemBuilder;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.menus.ConfirmacionMenu;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver.lootbox.AbrirLootboxMenu;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxTier;
import es.serversurvival.pixelcoins.lootbox.comprar.ComprarLootboxParametros;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class ConfirmacionComprarLootboxMenu extends ConfirmacionMenu<LootboxTier> {
    private final Configuration configuration;
    private final MenuService menuService;
    private final UseCaseBus useCaseBus;

    @Override
    public void onAceptar(Player jugador, InventoryClickEvent event, LootboxTier state) {
        UUID lootboxEnPropiedadId = UUID.randomUUID();

        useCaseBus.handle(ComprarLootboxParametros.builder()
                .lootboxTier(getState())
                .lootboxPropiedadId(lootboxEnPropiedadId)
                .jugadorId(jugador.getUniqueId())
                .build());

        menuService.open(jugador, AbrirLootboxMenu.class, lootboxEnPropiedadId);
    }

    @Override
    public ItemStack aceptarItem() {
        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(MenuItems.CLICKEABLE + "Comprar")
                .lore(List.of(
                        GOLD + "Comprar lootbox por " + Funciones.formatPixelcoins(configuration.getDouble(getState().getConfigurationKey()))
                ))
                .build();
    }

    @Override
    protected boolean closeOnAction() {
        return false;
    }
}
