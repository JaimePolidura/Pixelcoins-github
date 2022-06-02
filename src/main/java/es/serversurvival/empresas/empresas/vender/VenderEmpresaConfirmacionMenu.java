package es.serversurvival.empresas.empresas.vender;

import es.jaimetruman.ItemBuilder;
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
public final class VenderEmpresaConfirmacionMenu extends ConfirmacionMenu {
    private final VenderEmpresaUseCase useCase;
    private final String enviador;
    private final String destinatario;
    private final String empresa;
    private final double precio;

    @Override
    public void onAceptar(Player player, InventoryClickEvent event) {
        useCase.vender(enviador, destinatario, precio, empresa);

        player.sendMessage(ChatColor.GOLD + "Ahora eres dueño de " + ChatColor.DARK_AQUA + empresa + ChatColor.GOLD + " ," +
                "la has comprado por " + ChatColor.GREEN + precio + " PC");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Player vendedor = Bukkit.getPlayer(enviador);

        vendedor.sendMessage(ChatColor.GOLD + player.getName() + " te ha comprado " + ChatColor.DARK_AQUA + empresa + ChatColor.GOLD + " por " +
                ChatColor.GREEN + precio + " PC " + ChatColor.GOLD + ", ahora ya no eres dueño");
        vendedor.playSound(vendedor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    @Override
    public String titulo() {
        return DARK_RED + "" + BOLD + "solicitud compra empresa";
    }

    @Override
    public ItemStack aceptarItem() {
        String desc = GOLD + "Solicitud para comprar a " + enviador + " su empresa " + empresa + " a " + GREEN + "" + precio + " PC";
        List<String> loreAceptar = Funciones.dividirDesc(desc, 40);

        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(GREEN + "" + BOLD + "ACEPTAR")
                .lore(loreAceptar)
                .build();
    }
}
