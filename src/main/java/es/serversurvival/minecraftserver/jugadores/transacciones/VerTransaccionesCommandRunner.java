package es.serversurvival.minecraftserver.jugadores.transacciones;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.minecraftserver.transacciones.VerTransaccionesMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "transacciones",
        explanation = "Ver los ultimos movimientos de pixelcoins que has hecho"
)
@RequiredArgsConstructor
public final class VerTransaccionesCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        menuService.open(player, VerTransaccionesMenu.class, VerTransaccionesMenu.State.builder()
                .entidadId(player.getUniqueId())
                .onBack(() -> menuService.open(player, PerfilMenu.class))
                .nombre(player.getName())
                .build());
    }
}
