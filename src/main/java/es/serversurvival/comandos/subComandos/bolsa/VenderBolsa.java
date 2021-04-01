package es.serversurvival.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.util.Funciones.*;
import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(name = "bolsa vender")
public class VenderBolsa extends PixelcoinCommand implements CommandRunner {
    private final String mensajeIncorrecto = DARK_RED + "Uso incorrecto: /bolsa vender <id> [numero a vender]";

    @Override
    public void execute(CommandSender player, String[] args) {
        MySQL.conectar();

        ValidationResult result =ValidationsService.startValidating(args.length != 3 && args.length != 2, False.of(mensajeIncorrecto))
                .andMayThrowException(() -> args[1], mensajeIncorrecto, NaturalNumber, OwnerPosicionAbierta.de(player.getName(), TipoPosicion.LARGO))
                .andIfExists(() -> args[2], NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            MySQL.desconectar();
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

        if(mercadoEstaAbierto()){
            transaccionesMySQL.venderPosicion(posicionAVender ,cantidad , player.getName());
        }else{
            ordenesMySQL.abrirOrdenVentaLargo((Player) player, args[1], cantidad);
        }

        MySQL.desconectar();
    }
}
