package es.serversurvival.empresas.crear;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(
        value = "empresas crear",
        args = {"nombre", "descripccion..."},
        explanation = "Crear empresa en la que podras contratar jugadores, facturar etc"
)
public class CrearEmpresasComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<CrearEmpresasComando> {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas crear <nombre> <descripccion...>";
    private CrearEmpresaUseCase useCase = CrearEmpresaUseCase.INSTANCE;

    @Override
    public void execute(CrearEmpresasComando crearEmpresasComando, CommandSender sender) {
        String nombreEmpresa = crearEmpresasComando.getNombre();
        String desc = crearEmpresasComando.getDescripccion();
        Player player = (Player) sender;

        ValidationResult result = ValidatorService
                .startValidating(nombreEmpresa, NombreEmpresaNoPillado, MaxLength.of(Empresas.CrearEmpresaNombreLonMax, "El nombre no puede ser tan grande"))
                .and(desc, MaxLength.of(Empresas.CrearEmpresaDescLonMax, "La descripcion no puede ser tan larga"))
                .and(empresasMySQL.getEmpresasOwner(nombreEmpresa).size() + 1 <= Empresas.nMaxEmpresas, True.of("No puedes tener tantas empresas"))
                .validateAll();

        if (result.isFailed()){
            sender.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        useCase.crear(sender.getName(), nombreEmpresa, desc);

        player.sendMessage(ChatColor.GOLD + "Has creado una empresa! " + ChatColor.GOLD + "comandos utiles: " + ChatColor.AQUA + "/empresas depositar, /empresas contratar, /empresas logotipo, /empresas vertodas, /empresas miempresa /empresas editarnombre m?s: /ayuda empresario o /empresas ayuda");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        //TODO cambiar a que escuche un evento
        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + sender.getName() + " ha creado una nueva empresa: " + ChatColor.DARK_AQUA + nombreEmpresa);
    }
}
