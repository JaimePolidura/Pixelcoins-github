package es.serversurvival.minecraftserver.empresas.contratar;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.menus.ConfirmacionMenu;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.contratar.ContratarEmpleadoParametros;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;

@AllArgsConstructor
public final class ConfirmarOfertaContratacionMenu extends ConfirmacionMenu<ConfirmarOfertaContratacionMenu.State> {
    private final UseCaseBus useCaseBus;

    @Override
    public void onAceptar(Player destinatario, InventoryClickEvent event, ConfirmarOfertaContratacionMenu.State state) {
        UUID jugadorIdAContratar = getPlayer().getUniqueId();
        UUID jugadorIdContratador = state.getJugadorIdContratador();

        useCaseBus.handle(ContratarEmpleadoParametros.builder()
                .jugadorIdAContratar(jugadorIdAContratar)
                .descripccion(state.getDescripccion())
                .empresaId(state.getEmpresa().getEmpresaId())
                .jugadorIdContrador(jugadorIdContratador)
                .periodoPagoMs(state.getPeriodoPagoSueldoMs())
                .sueldo(state.getSueldo())
                .build());

        enviarMensajeYSonido(jugadorIdContratador, GOLD + "Has contratado a " + getPlayer().getName() +
                " en la empresa " + state.getEmpresa().getNombre(), ENTITY_PLAYER_LEVELUP) ;
        enviarMensajeYSonido(jugadorIdAContratar, GOLD + "Has sido contratado en " + state.getEmpresa().getNombre() + "con el cargo " +
                state.getDescripccion() + " con un sueldo de " + formatPixelcoins(state.getSueldo()) + "/ " +
                millisToDias(state.getPeriodoPagoSueldoMs()) + " dias", ENTITY_PLAYER_LEVELUP);
    }

    @Override
    public ItemStack aceptarItem() {
        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(BOLD + "" + GREEN + "ACEPTAR")
                .lore(List.of(
                        GOLD + "Empresa: " + getState().getEmpresa().getNombre(),
                        GOLD + "Sueldo: " + formatPixelcoins(getState().getSueldo()) + "/ " + millisToDias(getState().getPeriodoPagoSueldoMs()) + " dias",
                        GOLD + "Cargo: " + getState().getDescripccion()
                ))
                .build();
    }

    @Override
    public String titulo() {
        return DARK_RED + "" + BOLD + "      OFERTA TRABAJO";
    }

    @AllArgsConstructor
    public static class State {
        @Getter private final UUID jugadorIdContratador;
        @Getter private final Empresa empresa;
        @Getter private final double sueldo;
        @Getter private final long periodoPagoSueldoMs;
        @Getter private final String descripccion;
    }
}
