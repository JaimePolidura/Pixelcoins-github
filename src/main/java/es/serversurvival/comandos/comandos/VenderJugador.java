package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.menus.menus.solicitudes.VenderJugadorSolicitud;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static es.serversurvival.validaciones.Validaciones.*;


public class VenderJugador extends Comando {
    private final String cnombre = "venderjugador";
    private final String sintaxis = "/venderjugador <jugador> <precio>";
    private final String ayuda = "Vender un objeto en especifica a un precio a otro jugador";

    @Override
    public String getCNombre() {
        return cnombre;
    }

    @Override
    public String getSintaxis() {
        return sintaxis;
    }

    @Override
    public String getAyuda() {
        return ayuda;
    }

    @Override
    public void execute(Player player, String[] args) {
        MySQL.conectar();
        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(2))
                .andMayThrowException(() -> args[0], mensajeUsoIncorrecto(), NotEqualsIgnoreCase.of(player.getName()), JugadorOnline, InventarioNoLleno)
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto() ,PositiveNumber, SuficientesPixelcoins.of(() -> args[0], "El jugador no tiene las suficintes pixelcoins"))
                .and(player.getInventory().getItemInMainHand(), NotNull)
                .and(player.getInventory().getItemInMainHand().getType().toString(), NotEqualsIgnoreCase.of("AIR", "No puede ser aire"))
                .validateAll();

        MySQL.desconectar();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        VenderJugadorSolicitud solicitud = new VenderJugadorSolicitud(player, Bukkit.getPlayer(args[0]), player.getInventory().getItemInMainHand(), Double.parseDouble(args[1]));
        solicitud.enviarSolicitud();
    }
}
