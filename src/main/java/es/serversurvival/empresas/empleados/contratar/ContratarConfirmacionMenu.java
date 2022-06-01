package es.serversurvival.empresas.empleados.contratar;

import es.jaimetruman.ItemBuilder;
import es.serversurvival._shared.menus2.ConfirmacionMenu;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.empresas.empleados._shared.domain.TipoSueldo;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
public final class ContratarConfirmacionMenu extends ConfirmacionMenu {
    private final String enviadorJugadorNombre;
    private final String destinatarioJugadorNombre;
    private final String empresa;
    private final String cargo;
    private final double sueldo;
    private final TipoSueldo tipoSueldo;

    @Override
    public void onAceptar(Player player, InventoryClickEvent event) {
        Player enviadorPlayer = Bukkit.getPlayer(enviadorJugadorNombre);
        Player destinatarioPlayer = Bukkit.getPlayer(destinatarioJugadorNombre);

        ContratarUseCase.INSTANCE.contratar(enviadorPlayer.getName(), destinatarioJugadorNombre, empresa, sueldo, tipoSueldo, cargo);

        destinatarioPlayer.sendMessage(ChatColor.GOLD + "Ahora trabajas para " + enviadorJugadorNombre + ChatColor.AQUA + " /empleos irse /empleos misempleos");
        if (enviadorPlayer != null)
            enviadorPlayer.sendMessage(ChatColor.GOLD + "Has contratado a " + destinatarioJugadorNombre + ChatColor.AQUA + " /empresas despedir /empresas editarempleado");

    }

    @Override
    public String titulo() {
        return ChatColor.DARK_RED + "" + ChatColor.BOLD + "    Solicitud Contrato";
    }

    @Override
    public ItemStack aceptarItem() {
        String descStrinAceptar = ChatColor.GOLD + "Solicitud de contrato de la empresa " + this.empresa + " para trabajar como " +
                this.cargo + " , con un sueldo de " + ChatColor.GREEN + this.sueldo + " PC" + ChatColor.GOLD + "/" + tipoSueldo.nombre;
        List<String> lore = Funciones.dividirDesc(descStrinAceptar, 40);

        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(ChatColor.GREEN + "" + ChatColor.BOLD + "ACEPTAR")
                .lore(lore)
                .build();
    }
}
