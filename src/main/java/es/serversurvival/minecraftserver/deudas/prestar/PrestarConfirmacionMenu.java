package es.serversurvival.minecraftserver.deudas.prestar;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.menus.ConfirmacionMenu;
import es.serversurvival.minecraftserver.deudas._shared.DeudaItemMercadoLore;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.deudas.prestar.PrestarDeudaParametros;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class PrestarConfirmacionMenu extends ConfirmacionMenu<PrestarConfirmacionMenu.PrestarConfirmacionMenuState> {
    private final DeudaItemMercadoLore deudaItemMercadoLore;
    private final JugadoresService jugadoresService;
    private final UseCaseBus useCaseBus;

    @Override
    public String titulo() {
        return DARK_RED + "" + BOLD + "   SOLICITUD PRESTAMO";
    }

    @Override
    public void onAceptar(Player destinatario, InventoryClickEvent event, PrestarConfirmacionMenuState state) {
        useCaseBus.handle(PrestarDeudaParametros.builder()
                .numeroCuotasTotales(state.getNumeroCuotasTotales())
                .periodoPagoCuota(state.getPeriodoPagoCuotasMs())
                .nominal(state.getNominal())
                .interes(state.getInteres())
                .deudorJugadorId(getPlayer().getUniqueId())
                .acredorJugadorId(state.getAcredorJugadorId())
                .build());

        String enviadorNombre = jugadoresService.getNombreById(state.getAcredorJugadorId());

        destinatario.sendMessage(GOLD + "Has aceptado la solicitud de prestamo de " + enviadorNombre + " para ver tus deudas " + AQUA + "/deudas ver");
        MinecraftUtils.enviarMensajeYSonido(Bukkit.getPlayer(state.getAcredorJugadorId()),
                GOLD + destinatario.getName() + " te ha aceptado la solicitud de deuda", Sound.ENTITY_PLAYER_LEVELUP);
    }

    @Override
    public ItemStack aceptarItem() {
        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(GREEN + "" + BOLD + "Aceptar")
                .lore(deudaItemMercadoLore.buildNuevoPrestamoInfo(getState().getAcredorJugadorId(), getState().getNominal(),
                        getState().getInteres(), getState().getPeriodoPagoCuotasMs(), getState().getNumeroCuotasTotales()))
                .build();
    }

    @AllArgsConstructor
    public static class PrestarConfirmacionMenuState {
        @Getter private final UUID acredorJugadorId;
        @Getter private final double nominal;
        @Getter private final double interes;
        @Getter private final int numeroCuotasTotales;
        @Getter private final long periodoPagoCuotasMs;
    }
}
