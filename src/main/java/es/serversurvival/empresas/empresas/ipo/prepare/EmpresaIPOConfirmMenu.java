package es.serversurvival.empresas.empresas.ipo.prepare;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival._shared.menus.ConfirmacionMenu;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.empresas.ipo.IPOCommand;
import es.serversurvival.empresas.empresas.ipo.RealizarIPOUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class EmpresaIPOConfirmMenu extends ConfirmacionMenu {
    private static final RealizarIPOUseCase MAKE_IPO = new RealizarIPOUseCase();
    private final Empresa empresaToIpo;
    private final int accionesTotales;
    private final double precioPorAccion;
    private final int accionesOwner;

    @Override
    public void onAceptar(Player player, InventoryClickEvent event) {
        MAKE_IPO.makeIPO(this.empresaToIpo.getOwner(), new IPOCommand(
                this.empresaToIpo.getNombre(),
                this.accionesTotales,
                this.accionesOwner,
                this.precioPorAccion
        ));

        player.sendMessage(GOLD + "Has sacado a bolsa la empresa " + empresaToIpo.getNombre() + AQUA + "/empresas mercado");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Bukkit.broadcastMessage(GOLD + player.getName() + " ha sacado a bolsa " + empresaToIpo.getNombre() + " " + AQUA + "/empresas mercado");
    }

    @Override
    public ItemStack aceptarItem() {
        int accionesAVender = this.accionesTotales - this.accionesOwner;
        String valorAccionesAvender = FORMATEA.format(accionesAVender * precioPorAccion);
        String valorAccionesOwner = FORMATEA.format(accionesOwner * precioPorAccion);
        String valotTotalAcciones = FORMATEA.format(accionesTotales * precioPorAccion);

        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(GOLD + "" + BOLD + "Confirmar")
                .lore(List.of(
                        GOLD + "¿Seguro que quieres sacar a bolsa " + empresaToIpo.getNombre() + "?",
                        GOLD + "Precio/Accion: " + GREEN + "" + FORMATEA.format(precioPorAccion) + "PC",
                        GOLD + "Acciones totales: " + FORMATEA.format(accionesTotales) + GREEN + " ~ " + valotTotalAcciones + "PC",
                        GOLD + "Acciones a vender: " + FORMATEA.format(accionesAVender) + GREEN + " ~ " + valorAccionesAvender + "PC",
                        GOLD + "Acciones tuyas: " + FORMATEA.format(accionesOwner) + GREEN + " ~ " + valorAccionesOwner + "PC"
                ))
                .build();
    }
}
