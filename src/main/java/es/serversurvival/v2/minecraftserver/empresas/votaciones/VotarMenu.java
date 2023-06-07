package es.serversurvival.v2.minecraftserver.empresas.votaciones;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v1._shared.menus.ConfirmacionMenu;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones._shared.votaciones.Votacion;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votar.VotarVotacionParametros;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votar.VotarVotacionUseCase;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class VotarMenu extends ConfirmacionMenu<Votacion> {
    private final VotarVotacionUseCase votarVotacionUseCase;

    @Override
    public void onAceptar(Player player, InventoryClickEvent event, Votacion state) {
        votar(true);

        player.sendMessage(GOLD + "Has votado a favor");
    }

    @Override
    public void onCancelar(Player player, InventoryClickEvent event) {
        votar(false);

        player.sendMessage(GOLD + "Has votado en contra");
    }

    private void votar(boolean aFavor) {
        votarVotacionUseCase.votar(VotarVotacionParametros.builder()
                .aFavor(aFavor)
                .empresaId(getState().getEmpresaId())
                .votacionId(getState().getVotacionId())
                .jugadorId(getPlayer().getUniqueId())
                .build());
    }

    @Override
    public String titulo() {
        return DARK_RED + "" + BOLD + "          VOTAR";
    }

    @Override
    public ItemStack cancelarItem() {
        return ItemBuilder.of(Material.RED_WOOL).title(RED + "" + BOLD + "VOTAR EN CONTRA").build();
    }

    @Override
    public ItemStack aceptarItem() {
        return ItemBuilder.of(Material.GREEN_WOOL).title(RED + "" + BOLD + "VOTAR A FAVOR").build();
    }
}
