package es.serversurvival.minecraftserver.empresas.crear;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas.crear.CrearEmpresaParametros;
import es.serversurvival.pixelcoins.empresas.crear.CrearEmpresaUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static es.serversurvival.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas crear",
        args = {"empresa", "...descripccion"},
        explanation = "Crear una empresa"
)
@AllArgsConstructor
public final class CrearEmpresaCommnadRunner implements CommandRunnerArgs<CrearEmpresaComando> {
    private final UseCaseBus useCaseBus;

    @Override
    public void execute(CrearEmpresaComando comando, Player player) {
        useCaseBus.handle(CrearEmpresaParametros.builder()
                .descripccion(comando.getDescripccion())
                .nombre(comando.getEmpresa())
                .icono(Material.DIAMOND_PICKAXE.toString())
                .jugadorCreadorId(player.getUniqueId())
                .build());

        enviarMensajeYSonido(player, GOLD + "Has creado la empresa " + comando.getEmpresa() + " Comandos utiles: " +
                AQUA + "/empresas depositar, /empresas contratar, /empresas logotipo, /empresas vertodas, /empresas miempresa "+comando.getEmpresa()+" /empresas editar", Sound.ENTITY_PLAYER_LEVELUP);

        broadcastExcept(player, GOLD + player.getName() + " ha creado una nueva empresa: " + DARK_AQUA + comando.getEmpresa());
    }
}
