package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.Jugadores;
import es.serversurvival.mySQL.tablasObjetos.Deuda;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PagarDeudaDeudas extends DeudasSubCommand {
    private String scnombre = "pagar";
    private String sintaxis = "/deudas pagar <id>";
    private String ayuda = "Pagar toda la deuda, la id se ve en /deudas ver";

    public String getSCNombre() {
        return scnombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player playerDeudor, String[] args) {
        if (args.length != 2) {
            playerDeudor.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis + " la id se ve en /deudas");
            return;
        }
        if(!Funciones.esInteger(args[1])){
            playerDeudor.sendMessage(ChatColor.DARK_RED + "A ser posible mete numeros no letras, las id se ven el comando /deudas");
            return;
        }
        int id_deuda = Integer.parseInt(args[1]);

        deudasMySQL.conectar();
        Deuda deudaAPagar = deudasMySQL.getDeuda(id_deuda);
        if (deudaAPagar == null) {
            deudasMySQL.desconectar();
            playerDeudor.sendMessage(ChatColor.DARK_RED + "No esta registrada ninguna deuda con esa id, las ids se ven en /deudas");
            return;
        }
        if (!deudaAPagar.getDeudor().equalsIgnoreCase(playerDeudor.getName())) {
            deudasMySQL.desconectar();
            playerDeudor.sendMessage(ChatColor.DARK_RED + "No eres deudor de esa deuda, las ids se ven en el comando /deudas");
            return;
        }
        if (jugadoresMySQL.getJugador(playerDeudor.getName()).getPixelcoin() < deudaAPagar.getPixelcoins()) {
            deudasMySQL.desconectar();
            playerDeudor.sendMessage(ChatColor.DARK_RED + "No tienes las suficientes pixelcoins para pagar esa deuda, pixelcoins requeridas: " + ChatColor.GREEN + formatea.format(deudaAPagar.getPixelcoins()) + " PC");
            return;
        }

        deudasMySQL.pagarDeuda(playerDeudor, id_deuda);
        deudasMySQL.desconectar();
    }
}