package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static es.serversurvival.util.Funciones.*;
import static es.serversurvival.validaciones.Validaciones.*;

public class VenderBolsa extends BolsaSubCommand {
    private final String SCNombre = "vender";
    private final String sintaxis = "/bolsa vender <id> [numero a vender]";
    private final String ayuda = "Vender acciones, bitcoin, barriles etc con una id que se ven en /bolsa cartera y un numero de acciones a vender";

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

        ValidationResult result =ValidationsService.startValidating(args.length != 3 && args.length != 2, False.of(mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), NaturalNumber, OwnerPosicionAbierta.de(player.getName(), TipoPosicion.LARGO))
                .andIfExists(() -> args[2], NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
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
            ordenesMySQL.abrirOrdenVentaLargo(player, args[1], cantidad);
        }

        MySQL.desconectar();
    }
}
