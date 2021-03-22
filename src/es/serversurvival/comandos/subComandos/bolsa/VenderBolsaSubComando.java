package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.main.Pixelcoin;
import es.serversurvival.objetos.apiHttp.IEXCloud_API;
import es.serversurvival.objetos.mySQL.PosicionesAbiertas;
import es.serversurvival.objetos.mySQL.Transacciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class VenderBolsaSubComando extends BolsaSubCommand {
    private final String SCNombre = "vender";
    private final String sintaxis = "/bolsa vender <id> <numero acciones>";
    private final String ayuda = "Vender acciones con una id que se ven en /bolsa cartera y un numero de acciones a vender";

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
        if (args.length != 3) {
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        int id;
        int acciones;
        try {
            id = Integer.parseInt(args[1]);
            acciones = Integer.parseInt(args[2]);
        } catch (Exception e) {
            player.sendMessage(ChatColor.DARK_RED + "En la id y en el numero de acciones a vender necesitas meter numeros que no sean texto ni decimales");
            return;
        }
        if (!(id > 0 && acciones > 0)) {
            player.sendMessage(ChatColor.DARK_RED + "Necesitas introducir numeros que no sean negativos o que sean cero");
            return;
        }
        boolean existe;
        PosicionesAbiertas posicionesAbiertas = new PosicionesAbiertas();
        posicionesAbiertas.conectar();

        existe = posicionesAbiertas.existe(id);
        if (!existe) {
            player.sendMessage(ChatColor.DARK_RED + "No existe esa id, para verlas /bolsa cartera");
            return;
        }
        if (!posicionesAbiertas.getJugador(id).equalsIgnoreCase(player.getName())) {
            player.sendMessage(ChatColor.DARK_RED + "No tienes acciones invertidas en cartera con esa ID, para ver las ids: /bolsa cartera");
            return;
        }
        int accionesEnID = posicionesAbiertas.getNAcciones(id);
        if (accionesEnID < acciones) {
            player.sendMessage(ChatColor.DARK_RED + "No puedes vender mas acciones de las que tienes en cartera");
            return;
        }
        String ticker = posicionesAbiertas.getTicker(id);
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            try {
                double precioPorAccion = IEXCloud_API.getOnlyPrice(ticker);
                Transacciones t = new Transacciones();
                t.venderAccion(precioPorAccion, acciones, id, player);
                posicionesAbiertas.desconectar();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0L);
    }
}