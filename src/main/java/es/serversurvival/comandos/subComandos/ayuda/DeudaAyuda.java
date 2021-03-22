package es.serversurvival.comandos.subComandos.ayuda;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DeudaAyuda extends AyudaSubCommand {
    private final String SCNombre = "deuda";
    private final String sintaxis = "/ayuda deuda";
    private final String ayuda = "";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player p, String[] args) {
        p.sendMessage("   ");
        p.sendMessage(ChatColor.GOLD + "Puedes prestar dinero a los jugadores poniendo asi un interes y una cantidad de tiempo en el que el jugador te devolvero el dinero");
        p.sendMessage("   ");
        p.sendMessage(ChatColor.GOLD + "- Si quieres puedes poner un interes a tu prestamo. Es la cantidad aumentada en un % al que el jugador que se le ha prestado las pixelcoins tendra que delvolver dentro de un tiempo");
        p.sendMessage("   ");
        p.sendMessage(ChatColor.GOLD + "- Para conceder un prestamo (/deudas prestar) se le enviara al jugador una solicitud que expirara en 15s que al aceptarla se le cobrara el prestamo de maneta inmediata (sin interes)");
        p.sendMessage("   ");
        p.sendMessage(ChatColor.GOLD + "- Al conceberse el prestamo, cada dia que pase en la vida real en el que el server este abierto, se le cobrara al jugador que ha prestado las pixelcoins una cierta cantidad todos los dias, para calcular esa cantidad: " +
                "que se calculara : pixelcoinsPrestadas con interes / dias a prestar. Asi delvolviendo al jugador la cantidad que presto mas el interes.");
        p.sendMessage("   ");
        p.sendMessage(ChatColor.GOLD + "- Si el jugador que se le ha concedido el prestamo no tiene el dinero suficiente para pagarlo, a la minima que tenga pixelcoins y se abra el server se pagara el prestamo. y si le anotara que no ha podigo pagar el prestamo (/top)");
        p.sendMessage("   ");
        p.sendMessage(ChatColor.GOLD + "Ejmeplo: Si julia presta a jaime 100 pixelcoins a 2 dias y a un interes del 10% (/pagar JaimeTruman 100 2 10): 1º Se le pagara a jaime 100 pixelcoins 2º Cada dia que pase jaime pagara una cuota (110/2 = 55) asi hasta 2 dias, Si Jaime no tiene pixelcoins para pagar un dia pagara el siguinete con la cuota establecida como si nada");
        p.sendMessage("     ");
        p.sendMessage("/deduas prestar <nombreJugador> <pixelcoins> <dias> [interes]" + ChatColor.GOLD + " El interes es opcional");
        p.sendMessage("     ");
        p.sendMessage("/deudas ver" + ChatColor.GOLD + " Ver toda la informacion de las deudas que tienes");
        p.sendMessage("    ");
        p.sendMessage("/deudas cancelar <id> " + ChatColor.GOLD + "Cancelar una deuda de un jugador que te deba pixelcoins, la id se vera en " + ChatColor.AQUA + "/deudas ver");
        p.sendMessage("    ");
        p.sendMessage("/deudas pagar <id> " + ChatColor.GOLD + "Pagar toda las pixelcoins al jugador que le debes, la id se vera en " + ChatColor.AQUA + "/deudas ver");
        p.sendMessage("          ");
    }
}