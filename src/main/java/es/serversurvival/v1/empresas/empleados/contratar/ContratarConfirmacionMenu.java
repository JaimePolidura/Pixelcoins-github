package es.serversurvival.v1.empresas.empleados.contratar;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v1._shared.menus.ConfirmacionMenu;
import es.serversurvival.v1._shared.utils.Funciones;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public final class ContratarConfirmacionMenu extends ConfirmacionMenu<ContratarConfirmacionMenuState> {
    private final ContratarUseCase contratarUseCase;

    @Override
    public void onAceptar(Player player, InventoryClickEvent event, ContratarConfirmacionMenuState contratacionInfo) {
        Player enviadorPlayer = Bukkit.getPlayer(contratacionInfo.enviadorJugadorNombre());
        Player destinatarioPlayer = Bukkit.getPlayer(contratacionInfo.destinatarioJugadorNombre());

        this.contratarUseCase.contratar(enviadorPlayer.getName(), contratacionInfo.destinatarioJugadorNombre(), contratacionInfo.empresa(),
                contratacionInfo.sueldo(), contratacionInfo.tipoSueldo(), contratacionInfo.cargo());

        destinatarioPlayer.sendMessage(ChatColor.GOLD + "Ahora trabajas para " + contratacionInfo.enviadorJugadorNombre() + ChatColor.AQUA + " /empleos irse /empleos misempleos");
        enviadorPlayer.sendMessage(ChatColor.GOLD + "Has contratado a " + contratacionInfo.destinatarioJugadorNombre() + ChatColor.AQUA + " /empresas despedir /empresas editarempleado");
    }

    @Override
    public String titulo() {
        return ChatColor.DARK_RED + "" + ChatColor.BOLD + "    Solicitud Contrato";
    }

    @Override
    public ItemStack aceptarItem() {
        String descStrinAceptar = ChatColor.GOLD + "Solicitud de contrato de la empresa " + getState().empresa() + " para trabajar como " +
                getState().cargo() + " , con un sueldo de " + ChatColor.GREEN + getState().sueldo() + " PC" + ChatColor.GOLD + "/" + getState().tipoSueldo();
        List<String> lore = Funciones.dividirDesc(descStrinAceptar, 40);
        lore = lore.stream().map(line -> ChatColor.GOLD + line).collect(Collectors.toList());

        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(ChatColor.GREEN + "" + ChatColor.BOLD + "ACEPTAR")
                .lore(lore)
                .build();
    }
}
