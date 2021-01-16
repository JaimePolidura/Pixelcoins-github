package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import net.minecraft.server.v1_16_R1.ItemArmorStand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static es.serversurvival.validaciones.Validaciones.*;

public class LogotipoEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "logotipo";
    private final String sintaxis = "/empresas logotipo <empresa>";
    private final String ayuda = "Cambiar el logotipo de tu empresa al objeto que selecciones en la mano el ejecutar este comando";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        MySQL.conectar();
        String itemTipo = player.getInventory().getItemInMainHand().getType().toString();

        ValidationResult result = ValidationsService.startValidating(args.length == 2, True.of(mensajeUsoIncorrecto()))
                .and(itemTipo, NotEqualsIgnoreCase.of("AIR", "Tienes que tener un item en la mano"))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        empresasMySQL.cambiarIcono(args[1], player, itemTipo);
        MySQL.desconectar();
    }
}
