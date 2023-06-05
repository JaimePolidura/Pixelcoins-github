package es.serversurvival.v2.minecraftserver.deudas.prestar;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v1._shared.menus.ConfirmacionMenu;
import es.serversurvival.v2.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.v2.minecraftserver.deudas._shared.DeudaItemMercadoLore;
import es.serversurvival.v2.pixelcoins.deudas.prestar.PrestarDeudaParametros;
import es.serversurvival.v2.pixelcoins.deudas.prestar.PrestarDeudaUseCase;
import es.serversurvival.v2.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static es.serversurvival.v2.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class PrestarConfirmacionMenu extends ConfirmacionMenu<PrestarConfirmacionMenu.PrestarConfirmacionMenuState> {
    private final DeudaItemMercadoLore deudaItemMercadoLore;
    private final PrestarDeudaUseCase prestarDeudaUseCase;
    private final JugadoresService jugadoresService;

    @Override
    public String titulo() {
        return DARK_RED + "" + BOLD + "SOLICITUD PRESTAMO DE " + jugadoresService.getNombreById(getState().acredorJugadorId);
    }

    @Override
    public void onAceptar(Player destinatario, InventoryClickEvent event, PrestarConfirmacionMenuState state) {
        prestarDeudaUseCase.prestar(PrestarDeudaParametros.builder()
                .numeroCuotasTotales(state.getNumeroCuotasTotales())
                .periodoPagoCuita(state.getPeriodoPagoCuotasMs())
                .nominal(state.getNominal())
                .interes(state.getInteres())
                .deudorJugadorId(getPlayer().getUniqueId())
                .acredorJugadorId(state.getAcredorJugadorId())
                .build());

        String enviadorNombre = jugadoresService.getNombreById(state.getAcredorJugadorId());

        destinatario.sendMessage(GOLD + "Has aceptado la solicitud de prestamo de " + enviadorNombre + " para ver tus deudas " + AQUA + "/deudas ver");
        enviarMensajeYSonido(Bukkit.getPlayer(state.getAcredorJugadorId()),
                GOLD + destinatario.getName() + " te ha aceptado la solicitud de deuda", Sound.ENTITY_PLAYER_LEVELUP);
    }

    @Override
    public ItemStack aceptarItem() {
        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(GREEN + "" + BOLD + "Aceptar")
                .lore(deudaItemMercadoLore.buildNuevoPrestamoInfo(getState().getAcredorJugadorId(), getState().getNominal(),
                        getState().getNominal(), getState().getPeriodoPagoCuotasMs(), getState().getNumeroCuotasTotales()))
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
