package es.serversurvival.empresas.empresas.vender;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival._shared.menus.ConfirmacionMenu;
import es.serversurvival._shared.utils.Funciones;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class VenderEmpresaConfirmacionMenu extends ConfirmacionMenu<VenderEmpresaConfirmacionMenuState> {
    private final VenderEmpresaUseCase venderEmpresaUseCase;

    @Override
    public void onAceptar(Player player, InventoryClickEvent event, VenderEmpresaConfirmacionMenuState state) {
        this.venderEmpresaUseCase.vender(getState().enviador(), getState().destinatario(), getState().precio(), getState().empresa());

        player.sendMessage(ChatColor.GOLD + "Ahora eres dueño de " + ChatColor.DARK_AQUA + getState().empresa() + ChatColor.GOLD + " ," +
                "la has comprado por " + ChatColor.GREEN + getState().precio() + " PC");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Player vendedor = Bukkit.getPlayer(getState().enviador());

        vendedor.sendMessage(ChatColor.GOLD + player.getName() + " te ha comprado " + ChatColor.DARK_AQUA + getState().empresa() + ChatColor.GOLD + " por " +
                ChatColor.GREEN + getState().precio() + " PC " + ChatColor.GOLD + ", ahora ya no eres dueño");
        vendedor.playSound(vendedor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    @Override
    public ItemStack aceptarItem() {
        String desc = GOLD + "Solicitud para comprar a " + getState().enviador() + " su empresa " + getState().empresa() + " a " + GREEN + "" + getState().precio() + " PC";
        List<String> loreAceptar = Funciones.dividirDesc(desc, 40);

        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(GREEN + "" + BOLD + "ACEPTAR")
                .lore(loreAceptar)
                .build();
    }

    @Override
    public String titulo() {
        return DARK_RED + "" + BOLD + "Solicitud venta empresa";
    }
}
