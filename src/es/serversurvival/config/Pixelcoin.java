package es.serversurvival.config;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

public class Pixelcoin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("------------Plugin activado -------------");
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "------------------------------");
        this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "       Pixel Coins");
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "------------------------------");
        getServer().getPluginManager().registerEvents(this, this);
        this.getConfig().options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("----------- Plugin desactivado -----------");
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        String comando = cmd.getName();
        Comandos c = new Comandos();

        switch (comando.toLowerCase()) {
            case "dinero":
                c.mostrarDinero(p);
                break;
            case "topricos":
                c.mostarTopRicos(p);
                break;
            case "toppobres":
                c.mostrarTopPobres(p);
                break;
            case "verdinero":
                if (args.length == 1) {
                    c.verPixelCoins(p, args[0]);
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /verdinero <nombre del jugador>");
                }
                break;
            case "pagar":
                if (args.length == 2) {
                    String tname = args[0];
                    String scantidad = args[1];
                    c.pagarPixelCoins(p, tname, scantidad);
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /pagar <nombre del jugador> <cantidad a pagar>");
                }
                break;
            case "setdinero":
                if (args.length == 2) {
                    String tname = args[0];
                    String scantidad = args[1];
                    c.setDinero(p, tname, scantidad);
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /setdinero <nombre del jugador> <cantidad a poner>");
                }
                break;
            default:
                break;
        }
        return true;
    }


    //Whither Diamantes-Pixelcoins 
    int w1x = -249;
    int w1y = 66;
    int w1z = -219;

    //Wither bloque diam-Pixelcoins
    int w2x = -254;
    int w2y = 66;
    int w2z = -219;

    //Wither max pixelcoins-max diamantes
    int w3x = -260;
    int w3y = 66;
    int w3z = -219;

    //Wither Pixelcoins-Diamantes 
    int w4x = -265;
    int w4y = 66;
    int w4z = -219;

    //Wither cuarzo-pixelCoins
    int w5x = -245;
    int w5y = 66;
    int w5z = -215;

    //Wither pixelcoins-cuarzo
    int w6x = -245;
    int w6y = 66;
    int w6z = -210;

    //Wither max pixelcoins-max cuarzo   
    int w7x = -245;
    int w7y = 66;
    int w7z = -205;

    //NPC
    int n1x = -268;
    int n1y = 66;
    int n1z = -210;


    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e) {
        EquipmentSlot es = e.getHand();
        Player p = e.getPlayer();
        Entity en = e.getRightClicked();
        Metodos m = new Metodos();
			  
			  /*
			  p.sendMessage("x: " + Integer.toString(en.getLocation().getBlockX()));
			  p.sendMessage("y: " + Integer.toString(en.getLocation().getBlockY()));
			  p.sendMessage("z: " + Integer.toString(en.getLocation().getBlockZ()));
			  p.sendMessage("      ");
			  */

        int ecx = en.getLocation().getBlockX();
        int ecy = en.getLocation().getBlockY();
        int ecz = en.getLocation().getBlockZ();

        //Chequear withers
        if (en instanceof WitherSkeleton && es.equals(EquipmentSlot.HAND)) {

            if (w1x == ecx && w1y == ecy && w1z == ecz) {
                m.CambiarDiamantes(p);
            } else if (w2x == ecx && w2y == ecy && w2z == ecz) {
                m.cambiarBloquesDiamante(p);
            } else if (w4x == ecx && w4y == ecy && w4z == ecz) {
                m.cambiarPixelCoinsDia(p);
            } else if (w3x == ecx && w3y == ecy && w3z == ecz) {
                m.cambiarMaxPixelCoinsDiamante(p);
            } else if (w5x == ecx && w5y == ecy && w5z == ecz) {
                m.cambiarCuarzo(p);
            } else if (w6x == ecx && w6y == ecy && w6z == ecz) {
                m.cambiarPixelCoinsCua(p);
            } else if (w7x == ecx && w7y == ecy && w7z == ecz) {
                m.cambiarMaxPixelCoinsCuarzo(p);
            }
        }
        //Chequear npcs
        if (en instanceof Player && es.equals(EquipmentSlot.HAND)) {
            if (n1x == ecx && n1y == ecy && n1z == ecz) {
                p.sendMessage("no me des click derecho");
            }
        }

    }
}
