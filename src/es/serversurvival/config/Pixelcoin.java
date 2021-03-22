package es.serversurvival.config;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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

    //cuando se une jugador
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        p.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Bienvenido " + p.getName() + " al serversurival II !");
        p.sendMessage(ChatColor.YELLOW + "Escriba: /ayuda" + " por si no conoces algunos comandos, reglas etc");
        p.sendMessage(ChatColor.AQUA + "https://www.serversurvival2.ddns.net");
    }

    //comandos
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        String comando = cmd.getName();
        Comandos c = new Comandos();
        Tienda t = new Tienda();

        switch (comando.toLowerCase()) {
            //	/dinero
            case "dinero":
                try {
                    c.conectar("root", "", "pixelcoins");
                    c.mostrarDinero(p);
                    c.desconectar();
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos (di al admin que la encianda y que no sea vago)");
                }
                break;
            //	/topricos
            case "topricos":
                try {
                    c.conectar("root", "", "pixelcoins");
                    c.topRicos(p);
                    c.desconectar();
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos (di al admin que la encianda y que no sea vago)");
                }
                break;
            //	toppobres
            case "toppobres":
                try {
                    c.conectar("root", "", "pixelcoins");
                    c.topPobres(p);
                    ;
                    c.desconectar();
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos (di al admin que la encianda y que no sea vago)");
                }
                break;
            //	topvendedores
            case "topvendedores":
                try {
                    c.conectar("root", "", "pixelcoins");
                    c.topVendedores(p);
                    c.desconectar();
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos (di al admin que la encianda y que no sea vago)");
                }
                break;
            //	pagar
            case "pagar":
                if (args.length == 2) {
                    String tname = args[0];
                    String scantidad = args[1];
                    try {
                        c.conectar("root", "", "pixelcoins");
                        c.pagarPixelCoins(p, tname, scantidad);
                        c.desconectar();
                    } catch (Exception e1) {
                        p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos (Es comun que falle cuando pagas a un jugadores "
                                + "que no estan online pero funciona igualmente)");
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /pagar <nombre del jugador> <cantidad a pagar>");
                }
                break;
            //	/vender
            case "vender":
                if (args.length == 1) {
                    String precio = args[0];

                    try {
                        t.conectar("root", "", "pixelcoins");
                        t.A�adirObjeto(p, precio);
                        t.desconectar();
                    } catch (Exception e1) {
                        p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos, di a jaime que encianda la base de datos xd");
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /vender <precio a vender>");
                }
                break;
            //	/tienda
            case "tienda":
                try {
                    t.conectar("root", "", "pixelcoins");
                    t.mostrarTienda(p);
                    t.desconectar();
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos (di al admin que la encianda y que no sea vago)");
                }
                break;
            //	/listaofertas
            case "listaofertas":
                try {
                    t.conectar("root", "", "pixelcoins");
                    t.verOfertas(p);
                    t.desconectar();
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos (di al admin que la encianda y que no sea vago)");
                }
                break;
            //	/estadisticas
            case "estadisticas":
                try {
                    c.conectar("root", "", "pixelcoins");
                    c.mostrarEstadisticas(p);
                    c.desconectar();
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos (di al admin que la encianda y que no sea vago)");
                }

                break;

            //	/ayuda
            case "ayuda":
                Ayuda a = new Ayuda();
                if (args.length == 0) {
                    a.MostrarAyudas(p);
                } else {
                    String arg = args[0];

                    //comprobar los tipos de ayuda:
                    switch (arg) {

                        case "dinero":
                            a.AyudaDinero(p);
                            break;
                        case "normas":
                            a.AyudaNormas(p);
                            break;
                        case "jugar":
                            a.AyudaJugar(p);
                            break;
                        case "tienda":
                            a.AyudaTienda(p);
                            break;
                        default:
                            a.MostrarAyudas(p);
                    }
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
    public void onPlayerInteract(PlayerInteractEntityEvent e) throws Exception {
        EquipmentSlot es = e.getHand();
        Player p = e.getPlayer();
        Entity en = e.getRightClicked();
        Metodos m = new Metodos();
			  
			  /*
			  p.sendMessage("x: " + Integer.toString(en.getLocation().getBlockX()));
			  p.sendMessage("y: " + Integer.toString(en.getLocation().getBlockY()));
			  p.sendMessage("z: " + Integer.toString(en.getLocation().getBlockZ()));
			  p.sendMessage("      ");*/

        int ecx = en.getLocation().getBlockX();
        int ecy = en.getLocation().getBlockY();
        int ecz = en.getLocation().getBlockZ();

        //Chequear withers
        if (en instanceof WitherSkeleton && es.equals(EquipmentSlot.HAND)) {

            if (w1x == ecx && w1y == ecy && w1z == ecz) {
                try {
                    m.conectar("root", "", "pixelcoins");
                    m.cambiarDiamantes(p);
                    m.desconectar();
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos (di al admin que la encianda y que no sea vago)");
                }
            } else if (w2x == ecx && w2y == ecy && w2z == ecz) {
                try {
                    m.conectar("root", "", "pixelcoins");
                    m.cambiarBloquesDiamante(p);
                    m.desconectar();
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos (di al admin que la encianda y que no sea vago)");
                }
            } else if (w4x == ecx && w4y == ecy && w4z == ecz) {
                try {
                    m.conectar("root", "", "pixelcoins");
                    m.cambiarPixelCoinsDia(p);
                    m.desconectar();
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos(di al admin que la encianda y que no sea vago)");
                }
            } else if (w3x == ecx && w3y == ecy && w3z == ecz) {
                try {
                    m.conectar("root", "", "pixelcoins");
                    m.cambiarMaxPixelCoinsDiamante(p);
                    m.desconectar();
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos (di al admin que la encianda y que no sea vago)");
                }
            } else if (w5x == ecx && w5y == ecy && w5z == ecz) {
                try {
                    m.conectar("root", "", "pixelcoins");
                    m.cambiarCuarzo(p);
                    m.desconectar();
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos (di al admin que la encianda y que no sea vago)");
                }
            } else if (w6x == ecx && w6y == ecy && w6z == ecz) {
                try {
                    m.conectar("root", "", "pixelcoins");
                    m.cambiarPixelCoinsCua(p);
                    m.desconectar();
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos (di al admin que la encianda y que no sea vago)");
                }
            } else if (w7x == ecx && w7y == ecy && w7z == ecz) {
                try {
                    m.conectar("root", "", "pixelcoins");
                    m.cambiarMaxPixelCoinsCuarzo(p);
                    m.desconectar();
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos (di al admin que la encianda y que no sea vago)");
                }
            }
        }
        //Chequear NPC 
        if (en instanceof Player && es.equals(EquipmentSlot.HAND)) {
            Tienda t = new Tienda();
            if (n1x == ecx && n1y == ecy && n1z == ecz) {
                try {
                    t.conectar("root", "", "pixelcoins");
                    t.mostrarTienda(p);
                    t.desconectar();
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos");
                }
            }
        }
    }


    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent e) {
        Funciones f = new Funciones();
        String inNombre = e.getView().getTitle().toString();
        int slotsLibres = f.espaciosLibres(e.getWhoClicked().getInventory());


        if (inNombre.equalsIgnoreCase(ChatColor.DARK_RED + "" + ChatColor.BOLD + "            Tienda")) {
            Player p = (Player) e.getWhoClicked();

            if (slotsLibres != 0) {
                //Si clickas a un espacio libre salta error -> lo solucionamos con el try
                try {
                    Tienda t = new Tienda();
                    ItemStack i = e.getCurrentItem();
                    Inventory in = e.getInventory();

                    int slot = e.getSlot();
                    String dn = i.getItemMeta().getDisplayName();

                    //Comprobar si lo que has clickeado es tuyo o de otro jugador    	 
                    if (dn.equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "CLICK PARA RETIRAR")) {
                        try {
                            t.conectar("root", "", "pixelcoins");
                            t.retirarObjeto(p, i, in, slot);
                            t.mostrarTienda(p);
                            t.desconectar();
                        } catch (Exception e2) {
                            p.sendMessage(ChatColor.RED + "Di al admin que encienda la base de datos .1");
                        }

                    } else if (dn.equalsIgnoreCase(ChatColor.AQUA + "" + ChatColor.BOLD + "CLICK PARA COMPRAR")) {
                        try {
                            t.conectar("root", "", "pixelcoins");
                            t.ComprarObjeto(p, i, in, slot);
                            t.mostrarTienda(p);
                            t.desconectar();
                        } catch (Exception e2) {
                            p.sendMessage(ChatColor.DARK_RED + "No se ha podido conectar a la base de datosm di al admin que la encienda xd");
                        }

                    }
                    e.setCancelled(true);

                    //si no es int la id, el evento se cancela sino si eres el propietario te da el objeto y si no te da el objeto...

                } catch (Exception e2) {
                    e.setCancelled(true);
                }

            } else {
                p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario llenos :v");
                e.setCancelled(true);
            }


        }
    }
}