package es.serversurvival.v2.minecraftserver.empresas.ipo;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v1._shared.menus.ConfirmacionMenu;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.v2.pixelcoins.empresas.ipo.EmpresaIPOParametros;
import es.serversurvival.v2.pixelcoins.empresas.ipo.EmpresasIPOUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
public final class ConfirmarIPOMenu extends ConfirmacionMenu<ConfirmarIPOMenu.ConfirmarIPOMenuState> {
    private final EmpresasIPOUseCase empresasIPOUseCase;

    @Override
    public void onAceptar(Player player, InventoryClickEvent event, ConfirmarIPOMenuState state) {
        empresasIPOUseCase.ipo(EmpresaIPOParametros.builder()
                .precioPorAccion(state.getPrecioPorAccion())
                .numeroAccionesVender(state.getNumeroAccionesEnPropiedadAVender())
                .jugadorId(player.getUniqueId())
                .empresaId(state.getEmpresa().getEmpresaId())
                .build());

        player.sendMessage(GOLD + "Has sacado a bolsa la empresa " + state.getEmpresa().getNombre() + AQUA + "/empresas mercado");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Bukkit.broadcastMessage(GOLD + player.getName() + " ha sacado a bolsa " + state.getEmpresa().getNombre() + " " + AQUA + "/empresas mercado");
    }

    @Override
    public ItemStack aceptarItem() {
        int numeroAccionesAVender = getState().getNumeroAccionesEnPropiedadAVender();
        double precioPorAccion = getState().getPrecioPorAccion();
        int accionesTotalesEmpresa = getState().getEmpresa().getNTotalAcciones();

        String valorAccionesAvender = FORMATEA.format(numeroAccionesAVender * precioPorAccion);
        String valorAccionesOwner = FORMATEA.format((accionesTotalesEmpresa - numeroAccionesAVender) * precioPorAccion);
        String valorTotalAcciones = FORMATEA.format(accionesTotalesEmpresa * precioPorAccion);

        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(GOLD + "" + BOLD + "Confirmar")
                .lore(List.of(
                        GOLD + "¿Seguro que quieres sacar a bolsa " + getState().getEmpresa().getNombre() + "?",
                        GOLD + "Precio/Accion: " + GREEN + "" + FORMATEA.format(precioPorAccion) + "PC",
                        GOLD + "Nº Acciones a vender: " + FORMATEA.format(numeroAccionesAVender) + GREEN + " ~ " + valorAccionesAvender + "PC",
                        "  ",
                        GOLD + "Acciones tuyas: " + FORMATEA.format(accionesTotalesEmpresa - numeroAccionesAVender) + GREEN + " ~ " + valorAccionesOwner + "PC",
                        GOLD + "Valor total empresa: " + GREEN + valorTotalAcciones + " PC"
                ))
                .build();
    }

    @AllArgsConstructor
    public static class ConfirmarIPOMenuState {
        @Getter private final Empresa empresa;
        @Getter private final double precioPorAccion;
        @Getter private final int numeroAccionesEnPropiedadAVender;

        public static ConfirmarIPOMenuState from(Empresa empresam, double precioPorAccion, int numeroAccionesEnPropiedadAVender) {
            return new ConfirmarIPOMenuState(empresam, precioPorAccion, numeroAccionesEnPropiedadAVender);
        }
    }
}
