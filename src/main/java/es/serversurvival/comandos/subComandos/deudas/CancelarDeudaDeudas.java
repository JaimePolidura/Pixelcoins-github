package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.Deudas;
import es.serversurvival.mySQL.tablasObjetos.Deuda;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CancelarDeudaDeudas extends DeudasSubCommand {
    private final String scnombre = "cancelar";
    private final String sintaxis = "/deudas cancelar <id>";
    private final String ayuda = "cacelar toda la deuda a un jugador, la id se ve en /deudas ver";

    public String getSCNombre() {
        return scnombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis + " , la id se ve en el comando /deudas");
            return;
        }
        if(!Funciones.esInteger(args[1])){
            player.sendMessage(ChatColor.DARK_RED + "La id debe de ser un numero no texto, la id se puede ver en el comando: /deudas");
            return;
        }
        int id_deuda = Integer.parseInt(args[1]);
        deudasMySQL.conectar();
        Deuda deudaACancelar = deudasMySQL.getDeuda(id_deuda);
        if(deudaACancelar == null){
            deudasMySQL.desconectar();
            player.sendMessage(ChatColor.DARK_RED + "No hay ninguna id con ese numero, la id se ve en comando /deudas");
            return;
        }
        if(!deudaACancelar.getAcredor().equalsIgnoreCase(player.getName())){
            deudasMySQL.desconectar();
            player.sendMessage(ChatColor.DARK_RED + "No eres el acredor de esa deuda, las id se ven en /deuda");
            return;
        }

        deudasMySQL.cancelarDeuda(player, id_deuda);
        deudasMySQL.desconectar();
    }
}
