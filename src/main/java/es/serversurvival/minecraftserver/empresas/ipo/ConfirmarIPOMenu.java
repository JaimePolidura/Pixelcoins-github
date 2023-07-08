package es.serversurvival.minecraftserver.empresas.ipo;

import es.bukkitbettermenus.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.menus.ConfirmacionMenu;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.ipo.EmpresaIPOParametros;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class ConfirmarIPOMenu extends ConfirmacionMenu<ConfirmarIPOMenu.ConfirmarIPOMenuState> {
    private final UseCaseBus useCaseBus;
        
    @Override
    public void onAceptar(Player player, InventoryClickEvent event, ConfirmarIPOMenuState state) {
        useCaseBus.handle(EmpresaIPOParametros.builder()
                .precioPorAccion(state.getPrecioPorAccion())
                .numeroAccionesVender(state.getNumeroAccionesEnPropiedadAVender())
                .jugadorId(player.getUniqueId())
                .empresaId(state.getEmpresa().getEmpresaId())
                .build());
    }

    @Override
    public ItemStack aceptarItem() {
        int numeroAccionesAVender = getState().getNumeroAccionesEnPropiedadAVender();
        double precioPorAccion = getState().getPrecioPorAccion();
        int accionesTotalesEmpresa = getState().getEmpresa().getNTotalAcciones();

        String valorAccionesAvender = formatPixelcoins(numeroAccionesAVender * precioPorAccion);
        String valorAccionesOwner = formatPixelcoins((accionesTotalesEmpresa - numeroAccionesAVender) * precioPorAccion);
        String valorTotalAcciones = formatPixelcoins(accionesTotalesEmpresa * precioPorAccion);

        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(GOLD + "" + BOLD + "Confirmar")
                .lore(List.of(
                        GOLD + "¿Seguro que quieres sacar a bolsa " + getState().getEmpresa().getNombre() + "?",
                        GOLD + "Precio/Accion: " + formatPixelcoins(precioPorAccion),
                        GOLD + "Nº Acciones a vender: " + formatNumero(numeroAccionesAVender) + " " + valorAccionesAvender,
                        "  ",
                        GOLD + "Acciones tuyas: " + formatNumero(accionesTotalesEmpresa - numeroAccionesAVender) + " " + valorAccionesOwner,
                        GOLD + "Valor total empresa: " + valorTotalAcciones
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
