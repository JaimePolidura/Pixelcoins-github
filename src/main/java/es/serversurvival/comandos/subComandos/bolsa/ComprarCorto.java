package es.serversurvival.comandos.subComandos.bolsa;

import com.mojang.datafixers.functions.PointFreeRule;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.PosicionesAbiertas;
import es.serversurvival.mySQL.Transacciones;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import es.serversurvival.util.Funciones;
import es.serversurvival.validaciones.misValidaciones.OwnerPosicionAbierta;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static es.serversurvival.util.Funciones.*;
import static es.serversurvival.validaciones.Validaciones.*;

public class ComprarCorto extends BolsaSubCommand{
    private final String scnombre = "comprarcorto";
    private final String sintaxis = "/bolsa comprarcorto <id> <cantidad>";
    private final String ayuda = "Comprar en corto una posicion. Es decir cerrarla";

    @Override
    public String getSCNombre() {
        return scnombre;
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
        //TODO: Cuando args[1] es 1 no funciona pero con el resto de valores funciona perfectamente, Revisar
        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(3, mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), OwnerPosicionAbierta.de(player.getName(), TipoPosicion.CORTO))
                .andMayThrowException(() -> args[2], mensajeUsoIncorrecto(), NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            MySQL.desconectar();
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        int id = Integer.parseInt(args[1]);
        int cantidad = Integer.parseInt(args[2]);
        PosicionAbierta posicionAComprar = posicionesAbiertasMySQL.getPosicionAbierta(id);

        if(cantidad > posicionAComprar.getCantidad()) {
            cantidad = posicionAComprar.getCantidad();
        }

        if(mercadoEstaAbierto()){
            transaccionesMySQL.comprarPosicionCorto(posicionAComprar, cantidad, player.getName());
        }else{
            ordenesMySQL.abrirOrdenCompraCorto(player, args[2], cantidad);
        }

        MySQL.desconectar();
    }
}
