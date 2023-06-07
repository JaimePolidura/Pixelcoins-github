package es.serversurvival.minecraftserver.empresas.editarempresa;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.pixelcoins.empresas.editarempresa.EditarEmpresaParametros;
import es.serversurvival.pixelcoins.empresas.editarempresa.EditarEmpresaUseCase;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Command(
        value = "empresas editar",
        args = {"empresa", "queSeEdita", "...[nuevoValor]"},
        explanation = "'/empresas editar <nombre de tu empresa> nombre nuevoNombre' " +
                "'/empresas editar <nombre de tu empresa> desc nuevaDescripcion' " +
                "'/empresas editar <nombre de tu empresa> logotipo' (el logotipo sera el item que tengas en la mano'"
)
@RequiredArgsConstructor
public final class EditarEmpresaCommandRunner implements CommandRunnerArgs<EditarEmpresaComando> {
    private final EditarEmpresaUseCase editor;
    private final EmpresasService empresasService;

    @Override
    public void execute(EditarEmpresaComando comando, Player player) {
        Empresa empresa = empresasService.getByNombre(comando.getEmpresa());
        ItemStack itemPlayerHand = player.getInventory().getItemInMainHand();

        EditarEmpresaParametros.EditarEmpresaParametrosBuilder parametrosBuilder = EditarEmpresaParametros.builder()
                .empresaId(empresa.getEmpresaId())
                .jugadorId(player.getUniqueId())
                .nuevaDescripccion(empresa.getDescripcion())
                .nuevoIcono(empresa.getIcono())
                .nuevoNombre(empresa.getNombre());

        switch (comando.getQueSeEdita().toLowerCase()) {
            case "nombre", "n" -> editor.editar(parametrosBuilder.nuevoNombre(comando.getNuevoValor()).build());
            case "descipccion", "desc", "d" -> editor.editar(parametrosBuilder.nuevaDescripccion(comando.getNuevoValor()).build());
            case "logotipo", "l" -> editor.editar(parametrosBuilder.nuevoIcono(itemPlayerHand.getType().toString()).build());
        }

        player.sendMessage(ChatColor.GOLD + "Has editado la empresa");
    }
}
