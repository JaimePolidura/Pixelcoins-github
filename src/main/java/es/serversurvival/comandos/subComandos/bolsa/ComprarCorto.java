package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.PosicionesAbiertas;
import es.serversurvival.mySQL.Transacciones;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import es.serversurvival.util.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
        if(args.length != 3){
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        if(noEsUnNumero(args[1]) || noEsUnNumero(args[2])){
            player.sendMessage(ChatColor.DARK_RED + "Tanto la id como la cantiadad de acciones a comprar en corto deben de ser un numero no texto. " + this.sintaxis);
            return;
        }
        int id = Integer.parseInt(args[1]);
        int cantidad = Integer.parseInt(args[2]);
        if(id < 0 || cantidad < 0){
            player.sendMessage(ChatColor.DARK_RED + "La id y la cantidad deben de ser positivos " + this.sintaxis);
            return;
        }
        MySQL.conectar();

        PosicionAbierta posicionAComprar = posicionesAbiertasMySQL.getPosicionAbierta(id);
        if(posicionAComprar == null){
            player.sendMessage(ChatColor.DARK_RED + "La id no existe, consulta /bolsa cartera para ver las ids de las posiciones");
            MySQL.desconectar();
            return;
        }
        if(cantidad > posicionAComprar.getCantidad()) {
            cantidad = posicionAComprar.getCantidad();
        }

        transaccionesMySQL.comprarPosicionCorto(posicionAComprar, cantidad, player);
        MySQL.desconectar();
    }

    private boolean noEsUnNumero (String numero) {
        return !Funciones.esInteger(numero);
    }
}
