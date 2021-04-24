package es.serversurvival.legacy.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.legacy.mySQL.enums.TipoActivo;
import es.serversurvival.legacy.mySQL.enums.TipoPosicion;
import es.serversurvival.legacy.mySQL.tablasObjetos.PosicionAbierta;
import es.serversurvival.legacy.util.Funciones;
import es.serversurvival.legacy.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

@Command(name = "bolsa vender")
public class VenderBolsa extends PixelcoinCommand implements CommandRunner {
    private final String mensajeIncorrecto = DARK_RED + "Uso incorrecto: /bolsa vender <id> [numero a vender]";

    @Override
    public void execute(CommandSender player, String[] args) {

        ValidationResult result =ValidationsService.startValidating(args.length != 3 && args.length != 2, Validaciones.False.of(mensajeIncorrecto))
                .andMayThrowException(() -> args[1], mensajeIncorrecto, Validaciones.NaturalNumber, Validaciones.OwnerPosicionAbierta.de(player.getName(), TipoPosicion.LARGO))
                .andIfExists(() -> args[2], Validaciones.NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        PosicionAbierta posicionAVender = posicionesAbiertasMySQL.getPosicionAbierta(Integer.parseInt(args[1]));

        int cantidad = args.length == 2 ?
                posicionAVender.getCantidad() :
                Integer.parseInt(args[2]);

        if (posicionAVender.getCantidad() < cantidad)
            cantidad = posicionAVender.getCantidad();

        if(posicionAVender.getTipo_activo() == TipoActivo.ACCIONES_SERVER){
            //TODO:
            return;
        }

        if(Funciones.mercadoEstaAbierto()){
            transaccionesMySQL.venderPosicion(posicionAVender ,cantidad , player.getName());
        }else{
            ordenesMySQL.abrirOrdenVentaLargo((Player) player, args[1], cantidad);
        }

    }
}
