package es.serversurvival.v1.empresas.empresas.ipo.prepare;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v1._shared.menus.ConfirmacionMenu;
import es.serversurvival.v1.empresas.empresas.ipo.IPOCommand;
import es.serversurvival.v1.empresas.empresas.ipo.RealizarIPOUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class EmpresaIPOConfirmMenu extends ConfirmacionMenu<EmpresaIPOConfirmMenuState> {
    private final RealizarIPOUseCase realizarIPOUseCase;

    @Override
    public void onAceptar(Player destinatario, InventoryClickEvent event, EmpresaIPOConfirmMenuState state) {
        this.realizarIPOUseCase.makeIPO(state.empresaToIpo().getOwner(), new IPOCommand(
                state.empresaToIpo().getNombre(),
                state.accionesTotales(),
                state.accionesOwner(),
                state.precioPorAccion()
        ));

        destinatario.sendMessage(GOLD + "Has sacado a bolsa la empresa " + state.empresaToIpo().getNombre() + AQUA + "/empresas mercado");
        destinatario.playSound(destinatario.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Bukkit.broadcastMessage(GOLD + destinatario.getName() + " ha sacado a bolsa " + state.empresaToIpo().getNombre() + " " + AQUA + "/empresas mercado");
    }

    @Override
    public ItemStack aceptarItem() {
        int accionesAVender = getState().accionesTotales() - getState().accionesOwner();
        String valorAccionesAvender = FORMATEA.format(accionesAVender * getState().precioPorAccion());
        String valorAccionesOwner = FORMATEA.format(getState().accionesOwner() * getState().precioPorAccion());
        String valotTotalAcciones = FORMATEA.format(getState().accionesTotales() * getState().precioPorAccion());

        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(GOLD + "" + BOLD + "Confirmar")
                .lore(List.of(
                        GOLD + "¿Seguro que quieres sacar a bolsa " + getState().empresaToIpo().getNombre() + "?",
                        GOLD + "Precio/Accion: " + GREEN + "" + FORMATEA.format(getState().precioPorAccion()) + "PC",
                        GOLD + "Acciones totales: " + FORMATEA.format(getState().accionesTotales()) + GREEN + " ~ " + valotTotalAcciones + "PC",
                        GOLD + "Acciones a vender: " + FORMATEA.format(accionesAVender) + GREEN + " ~ " + valorAccionesAvender + "PC",
                        GOLD + "Acciones tuyas: " + FORMATEA.format(getState().accionesOwner()) + GREEN + " ~ " + valorAccionesOwner + "PC"
                ))
                .build();
    }
}
