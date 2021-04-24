package es.serversurvival.legacy.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.legacy.mySQL.enums.TipoPosicion;
import es.serversurvival.legacy.mySQL.tablasObjetos.PosicionAbierta;
import es.serversurvival.legacy.util.Funciones;
import es.serversurvival.legacy.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

@Command(name = "bolsa comprarcorto")
public class ComprarCorto extends PixelcoinCommand implements CommandRunner{
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /bolsa comprarcorto <id> <cantidad>";

    @Override
    public void execute(CommandSender player, String[] args) {
        //TODO: Cuando args[1] es 1 no funciona pero con el resto de valores funciona perfectamente, Revisar
        ValidationResult result = ValidationsService.startValidating(args.length, Validaciones.Same.as(3, usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.OwnerPosicionAbierta.de(player.getName(), TipoPosicion.CORTO))
                .andMayThrowException(() -> args[2], usoIncorrecto, Validaciones.NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        int id = Integer.parseInt(args[1]);
        int cantidad = Integer.parseInt(args[2]);
        PosicionAbierta posicionAComprar = posicionesAbiertasMySQL.getPosicionAbierta(id);

        if(cantidad > posicionAComprar.getCantidad()) {
            cantidad = posicionAComprar.getCantidad();
        }

        if(Funciones.mercadoEstaAbierto()){
            transaccionesMySQL.comprarPosicionCorto(posicionAComprar, cantidad, player.getName());
        }else{
            ordenesMySQL.abrirOrdenCompraCorto((Player) player, args[2], cantidad);
        }

    }
}
