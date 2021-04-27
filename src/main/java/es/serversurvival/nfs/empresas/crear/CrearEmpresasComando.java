package es.serversurvival.nfs.empresas.crear;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.nfs.shared.comandos.PixelcoinCommand;
import es.serversurvival.nfs.empresas.mysql.Empresas;
import es.serversurvival.nfs.utils.Funciones;
import es.serversurvival.nfs.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.DARK_RED;

@Command("empresas crear")
public class CrearEmpresasComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas crear <nombre> <descripccion...>";
    private CrearEmpresaUseCase useCase = CrearEmpresaUseCase.INSTANCE;

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        ValidationResult result = ValidationsService.startValidating(args.length >= 3, Validaciones.True.of(usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.NombreEmpresaNoPillado, Validaciones.MaxLength.of(Empresas.CrearEmpresaNombreLonMax, "El nombre no puede ser tan grande"))
                .andMayThrowException(() -> Funciones.buildStringFromArray(args, 2), usoIncorrecto, Validaciones.MaxLength.of(Empresas.CrearEmpresaDescLonMax, "La descripcion no puede ser tan larga"))
                .and(empresasMySQL.getEmpresasOwner(sender.getName()).size() + 1 <= Empresas.nMaxEmpresas, Validaciones.True.of("No puedes tener tantas empresas"))
                .validateAll();

        if (result.isFailed()){
            sender.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        String nombreEmpresa = args[1];
        String descripccion = Funciones.buildStringFromArray(args, 2);

        useCase.crear(sender.getName(),  nombreEmpresa, descripccion);

        player.sendMessage(ChatColor.GOLD + "Has creado una empresa! " + ChatColor.GOLD + "comandos utiles: " + ChatColor.AQUA + "/empresas depositar, /empresas contratar, /empresas logotipo, /empresas vertodas, /empresas miempresa /empresas editarnombre m?s: /ayuda empresario o /empresas ayuda");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        //TODO cambiar a que escuche un evento
        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + player.getName() + " ha creado una nueva empresa: " + ChatColor.DARK_AQUA + nombreEmpresa);
    }
}
