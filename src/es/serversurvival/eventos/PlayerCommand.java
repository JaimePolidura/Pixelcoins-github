package es.serversurvival.eventos;

import es.serversurvival.objetos.Solicitudes;
import es.serversurvival.objetos.Deudas;
import es.serversurvival.objetos.Empresas;
import es.serversurvival.objetos.Empleados;
import es.serversurvival.objetos.Mensajes;
import es.serversurvival.objetos.Ofertas;
import es.serversurvival.objetos.Jugador;
import es.serversurvival.objetos.Transacciones;
import es.serversurvival.config.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class PlayerCommand implements Listener, CommandExecutor {
    private DecimalFormat formatea = new DecimalFormat("###,###.##");
    private Jugador j = new Jugador();
    private Ofertas o = new Ofertas();
    private Funciones f = new Funciones();
    private Deudas d = new Deudas();
    private Empleados empl = new Empleados();
    private Empresas empr = new Empresas();
    private Transacciones t = new Transacciones();
    private Mensajes m = new Mensajes();
    private Solicitudes s = new Solicitudes();

    @EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        String cnombre = cmd.getName();

        //		COMANDOS JUGADOR
        switch (cnombre.toLowerCase()) {
            //DINERO
            case "dinero":
                try {
                    j.conectar("root", "", "pixelcoins");
                    j.mostarPixelcoin(p);
                    j.desconectar();
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "La base de datos esta apagada, di al admin que no sea vago y que la encienda xd");
                }
                break;

            //DEUDAS
            case "deudas":
                try {
                    d.conectar("root", "", "pixelcoins");
                    d.mostarDeudas(p);
                    d.desconectar();
                } catch (Exception e) {

                }
                break;

            //ESTADISTICAS
            case "estadisticas":
                try {
                    j.conectar("root", "", "pixelcoins");
                    j.mostarEstadisticas(p);
                    j.desconectar();
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "La base de datos esta apagada dile aladmin que la encienda");
                }
                break;

            //TOPRICOS8
            case "topricos":
                try {
                    j.conectar("root", "", "pixelcoins");
                    j.mostarTopRicos(p);
                    j.desconectar();
                } catch (Exception e) {
                    p.sendMessage(e.getMessage());
                    p.sendMessage(ChatColor.DARK_RED + "Di al admin que encienda la base de datos");
                }
                break;

            //TOPPOBRES
            case "toppobres":
                try {
                    j.conectar("root", "", "pixelcoins");
                    j.mostarTopPobres(p);
                    j.desconectar();
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "Di al admin que encienda la base de datos");
                }
                break;

            //TOPVENDEDORES
            case "topvendedores":
                try {
                    j.conectar("root", "", "pixelcoins");
                    j.mostarTopVendedores(p);
                    j.desconectar();
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "Di al admin que encienda la base de datos xd");
                }
                break;

            //TOPFIABLES
            case "topfiables":
                try {
                    j.conectar("root", "", "pixelcoins");
                    j.mostrarTopFiables(p);
                    j.desconectar();
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "Di al admin que encienda la base de datos xd");
                }
                break;
            case "topmenosfiables":
                try {
                    j.conectar("root", "", "pixelcoins");
                    j.mostrarTopMenosFiables(p);
                    j.desconectar();
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "Di al admin que encienda la base de datos xd");
                }
                break;
            //TIENDA
            case "tienda":
                try {
                    o.conectar("root", "", "pixelcoins");
                    o.mostarOfertas(p);
                    o.desconectar();
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "Di al admin que abra la base de datos pls");
                }
                break;

            //EMPRESAS
            case "empresas":
                try {
                    empr.conectar("root", "", "pixelcoins");
                    empr.mostrarEmpresas(p);
                    empr.desconectar();
                } catch (Exception e) {
                    p.sendMessage("Error al conectar a la base de datos");
                }
                break;
            //VENDER
            case "vender":
                //comprobamos: args=1, que no sea pociones,baners o libro encantdo, que sea texto el argumento, y que el precio sea positivo
                if (args.length == 1) {
                    String sprecio = args[0];
                    ItemStack i = p.getInventory().getItemInMainHand();
                    String nombreItem = i.getType().toString();
                    int precio;

                    if (!nombreItem.equals("POTION") && !nombreItem.contentEquals("BANNER") && !nombreItem.equals("AIR") && !nombreItem.equals("SPLASH_POTION") && !nombreItem.contentEquals("LINGERING_POTION")) {
                        try {
                            precio = Integer.parseInt(sprecio);
                            if (precio > 0) {
                                try {
                                    o.conectar("root", "", "pixelcoins");
                                    o.crearOferta(i, p, precio);
                                    o.desconectar();
                                } catch (Exception e) {
                                    p.sendMessage(ChatColor.DARK_RED + "La base de datos esta apagada, di al admin que al encienda");
                                }
                            } else {
                                p.sendMessage(ChatColor.DARK_RED + "A ser posible que los precios no sean negativos ");
                            }
                        } catch (NumberFormatException e) {
                            p.sendMessage(ChatColor.DARK_RED + "Introduce un numero, no texto de tal manera: /vender <precio a vender/item>");
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "No se puede vender ese tipo de objeto");
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /vender <precio a vender en la tienda>");
                }

                break;
            //PAGAR
            case "pagar":
                String scantidad = "";
                String tname = "";
                int cantidad = 0;
                boolean done = false;

                //Comprobamos que haya metido dos argumentos
                if (args.length == 2) {
                    tname = args[0];
                    scantidad = args[1];
                    //Comprobamos que el segundo argumento sea texto
                    try {
                        cantidad = Integer.parseInt(scantidad);
                        done = true;
                    } catch (NumberFormatException e) {
                        p.sendMessage(ChatColor.DARK_RED + "Introduce un numero, no texto de tal manera: /pagar <nombre del jugador> <cantidad a pagar>");
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /pagar <nombreDelJugador> <precio>");
                }

                //Ha metido 2 argumentos y el segundo es numerico
                //Comprobamos si el numero que ha metido es superior a 0
                if (done == true && cantidad > 0) {
                    String destinatario = "";
                    try {
                        Player tp = Bukkit.getServer().getPlayer(tname);

                        try {
                            destinatario = tp.getName();
                        } catch (Exception e) {
                            p.sendMessage(ChatColor.DARK_RED + "Solo puedes pagar pixelcoins a jugadores que esten online");
                            break;
                        }

                        //Comprobamos que el jugador a pagar este online
                        if (destinatario.equalsIgnoreCase(p.getName()) == false) {
                            t.conectar("root", "", "pixelcoins");
                            t.realizarPagoManual(p.getName(), tp.getName(), cantidad, p, "", "PAGO");
                            t.desconectar();
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "Tu y yo sabemos que eso no va a pasar xd");
                        }
                    } catch (Exception e) {
                        p.sendMessage(ChatColor.DARK_RED + "Error en la base de datos, es posible que no este encendida, comentar al admin");
                    }
                } else if (done == true && cantidad <= 0) {
                    p.sendMessage(ChatColor.DARK_RED + "A ser posible hay que pagar una cantidad de dinero que no sea negativa o que no sea 0");
                }
                break;

            //PRESTAR
            case "prestar":
                int dinero = 0;
                int dias = 0;
                int interes = 0;
                Player tp = null;
                String destinatario = "";

                //n� de argumentos
                if (args.length == 3 || args.length == 4) {
                    destinatario = "";
                    tp = Bukkit.getServer().getPlayer(args[0]);

                    try {
                        destinatario = tp.getName();
                    } catch (Exception e) {
                        p.sendMessage(ChatColor.DARK_RED + "Solo puedes prestar dinero a jugadores que esten online");
                        break;
                    }

                    if (!(destinatario.equalsIgnoreCase(p.getName()))) {
                        //argumentos son texto
                        try {
                            dinero = Integer.parseInt(args[1]);
                            dias = Integer.parseInt(args[2]);
                            if (args.length == 4) {
                                interes = Integer.parseInt(args[3]);
                            }

                            if (dinero >= 0 && dias > 0 && interes >= 0) {
                                if (dinero < dias) {
                                    p.sendMessage(ChatColor.DARK_RED + "Introduce valores de tal modo que el dinero sea superior a los dias");
                                    break;
                                }
                            } else {
                                p.sendMessage(ChatColor.DARK_RED + "Introduce valores que no sean negativos y dias mayores de 0");
                                break;
                            }
                        } catch (NumberFormatException e) {
                            p.sendMessage(ChatColor.DARK_RED + "Introduce numeros no texto");
                            break;
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Como que no :v");
                        break;
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /prestar <jugador> <dinero> <dias> [interes]");
                    break;
                }

                try {
                    s.conectar("root", "", "pixelcoins");

                    boolean regEnviador = s.estRegistradoEnviador(p.getName());
                    boolean regDestinatario = s.estRegistradoDestinatario(destinatario);
                    s.desconectar();
                    if (regEnviador) {
                        p.sendMessage(ChatColor.DARK_RED + "Solo puedes tener una soliciud a la vez");
                    } else if (regDestinatario) {
                        p.sendMessage(ChatColor.DARK_RED + destinatario + " ya le han eviado una solicitud, esperar 15s");
                    } else {
                        int id_tabla = 0;

                        try {
                            dinero = f.interes(dinero, interes);
                            d.conectar("root", "", "pixelcoins");
                            d.nuevaDeuda(destinatario, p.getName(), dinero, dias, interes);
                            id_tabla = d.getMaxId();
                            d.desconectar();
                        } catch (Exception e) {
                            p.sendMessage("error.1");
                            break;
                        }
                        s.conectar("root", "", "pixelcoins");
                        s.nuevaSolicitud(p.getName(), destinatario, 1, id_tabla, p);
                        s.desconectar();

                        tp.sendMessage(ChatColor.GOLD + p.getName() + " te ha enviado una solicitud para prestarte: " + ChatColor.GREEN + formatea.format(dinero) + " PC" + ChatColor.GOLD + " a " + ChatColor.GREEN + dias + " dias, " + ChatColor.GOLD + "con un interes del: " + ChatColor.GREEN + interes + "% "
                                + "(" + formatea.format(f.interes(dinero, interes)) + " PC)");
                        tp.sendMessage("/aceptar" + ChatColor.GOLD + " o " + ChatColor.WHITE + "/rechazar" + ChatColor.GOLD + " Expira en 15 segundos");
                        tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                        p.sendMessage(ChatColor.GOLD + "Has enviado una solicitud a " + tp.getName() + " de prestamo, expira en 15s");
                    }
                } catch (Exception e) {
                    p.sendMessage("error");
                }
                break;

            //CONTRATAR
            case "contratar":
                if (args.length != 4 && args.length != 5) {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /contratar <nombreJugador> <nombreEmpresa> <sueldo> <tipo sueldo (/ayuda empresas)> [cargo]");
                    break;
                }

                int sueldo = 0;
                try {
                    sueldo = Integer.parseInt(args[2]);
                    if (sueldo <= 0) {
                        p.sendMessage(ChatColor.DARK_RED + "EXPLOTADORRRR MACHURULOOO MACHISTA OPRESOR");
                        break;
                    }
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "A ser posible introduce texto, no numeros.");
                    break;
                }
                String nombreContratar = args[0];
                Player tp2 = p.getServer().getPlayer(nombreContratar);
                try {
                    nombreContratar = tp2.getName();
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "Solo puedes contratar a jugadores que esten online");
                    break;
                }

                if (nombreContratar.equalsIgnoreCase(p.getName())) {
                    p.sendMessage(ChatColor.DARK_RED + "No te puedes contratar a ti mismo crack");
                    break;
                }

                String nombreEmpresa = args[1];
                String tipo = args[3];
                String cargo = "Trabajador";
                if (args.length == 5) {
                    cargo = args[4];
                }

                if (!tipo.equalsIgnoreCase("s") && !tipo.equalsIgnoreCase("m") && !tipo.equalsIgnoreCase("2s") && !tipo.equalsIgnoreCase("d")) {
                    p.sendMessage(ChatColor.DARK_RED + "El tipo de pago de sueldo puede ser:");
                    p.sendMessage(ChatColor.DARK_RED + "   s: El sueldo se pagara cada semana");
                    p.sendMessage(ChatColor.DARK_RED + "   2s: El sueldo se pagara cada 2 semanas");
                    p.sendMessage(ChatColor.DARK_RED + "   m: El sueldo se pagara cada mes");
                    p.sendMessage(ChatColor.DARK_RED + "   d: El sueldo se pagara cada dia");
                    break;
                }

                try {
                    empr.conectar("root", "", "pixelcoins");
                    boolean reg = empr.estaRegistradoNombre(nombreEmpresa);

                    if (reg == false) {
                        p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
                        break;
                    }
                    boolean ow = empr.esOwner(p.getName(), nombreEmpresa);
                    if (ow == false) {
                        p.sendMessage(ChatColor.DARK_RED + "No eres due�o de esa empresa");
                        break;
                    }
                    empr.desconectar();
                } catch (Exception e) {

                }

                try {
                    s.conectar("root", "", "pixelcoins");
                    boolean reg = s.estRegistradoDestinatario(nombreContratar);

                    if (reg == true) {
                        p.sendMessage(ChatColor.DARK_RED + "Al jugador ya le han enviado solicitudes");
                        break;
                    }
                    boolean regEnviador = s.estRegistradoEnviador(p.getName());
                    if (regEnviador == true) {
                        p.sendMessage(ChatColor.DARK_RED + "Solo puedes tener una solicitud a la vez");
                        return true;
                    }
                    s.desconectar();
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "Error en conectar a la base de datos, hablar con el admin");
                }

                int id = 0;
                try {
                    empl.conectar("root", "", "pixelcoins");

                    id = empl.getId(nombreContratar, nombreEmpresa);

                    if (id != 0) {
                        p.sendMessage(ChatColor.DARK_RED + "Ese jugador ya esta contratado / ya le has enviado solicitud");
                        break;
                    }

                    empl.contratar(tp2.getName(), nombreEmpresa, sueldo, tipo, cargo, p, tp2);
                    id = empl.getId(tp2.getName(), nombreEmpresa);
                    empl.desconectar();
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "Error en conectar a la base de datos, hablar con el admin");
                }

                try {
                    s.conectar("root", "", "pixelcoins");
                    s.nuevaSolicitud(p.getName(), nombreContratar, 2, id, p);
                    s.desconectar();
                } catch (Exception e) {

                }

                break;
            //CREAREMPRESA
            case "crearempresa":
                if (args.length >= 2) {
                    String nombre = args[0];
                    int nombreLongitud = nombre.toCharArray().length;
                    if (nombreLongitud <= 24) {
                        String descripcion = "";
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < args.length; i++) {
                            if (i != 0) {
                                sb.append(args[i] + " ");
                            }
                        }
                        descripcion = sb.toString();

                        int longitudDescripccion = descripcion.toCharArray().length;
                        if (longitudDescripccion <= 200) {
                            try {
                                empr.conectar("root", "", "pixelcoins");
                                empr.crearEmpresa(nombre, p, descripcion);
                                empr.desconectar();
                            } catch (Exception e) {

                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "La descripcion solo puede tener como maximo 200 caracteres (incluidos espacios)");
                        }

                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "El tama�o del nombre de la empresas tiene que tener como maximo 24 caracteres");
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /crearempresa <nombre> <descripccion>");
                }

                break;
            case "depositar":
                if (args.length == 2) {
                    try {
                        int pixelcoins = Integer.parseInt(args[1]);
                        String nombreEmpresa2 = args[0];
                        if (pixelcoins > 0) {
                            try {
                                t.conectar("root", "", "pixelcoins");
                                t.depositarPixelcoins(p, pixelcoins, nombreEmpresa2);
                                t.desconectar();
                            } catch (Exception e) {

                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "Introduce numeros que no sean igual a cero o que no sean negativos");
                        }

                    } catch (Exception e) {
                        p.sendMessage(ChatColor.DARK_RED + "Introduce numeros no texto");
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /depositar <nombre empresa> <pixelcoins>");
                }
                break;

            case "sacar":
                if (args.length == 2) {
                    try {
                        int pixelcoins = Integer.parseInt(args[1]);
                        String nombreEmpresa3 = args[0];
                        if (pixelcoins > 0) {
                            try {
                                t.conectar("root", "", "pixelcoins");
                                t.sacarPixelcoins(p, pixelcoins, nombreEmpresa3);
                                t.desconectar();
                            } catch (Exception e) {

                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "Introduce numeros que no sean igual a cero o que no sean negativos");
                        }

                    } catch (Exception e) {
                        p.sendMessage(ChatColor.DARK_RED + "Introduce numeros no texto");
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /sacar <nombre empresa> <pixelcoins>");
                }
                break;

            case "logotipo":
                if (args.length == 1) {
                    String tipo2 = p.getInventory().getItemInMainHand().getType().toString();
                    if (tipo2.equalsIgnoreCase("AIR") == false) {
                        try {
                            empr.conectar("root", "", "pixelcoins");
                            empr.cambiarIcono(args[0], p, tipo2);
                            empr.desconectar();
                        } catch (Exception e) {
                            p.sendMessage("error al conectar a la base de datos");
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Tienes que seleccionar un objeto en la mano");
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /logotipo <nombre empresa>");
                }
                break;
            case "editarempleado":
                if (args.length != 4) {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /editarEmpleado <empresa> <empleado> <tipo> <valor>");
                    return true;
                }

                if (args[1].equalsIgnoreCase(p.getName())) {
                    p.sendMessage(ChatColor.DARK_RED + "No te puedes editar a ti mismo");
                    return true;
                }

                String tipoEditar = args[2];
                switch (tipoEditar.toLowerCase()) {
                    case "sueldo":
                        int sueldoEditar;
                        try {
                            sueldoEditar = Integer.parseInt(args[3]);
                        } catch (Exception e) {
                            p.sendMessage(ChatColor.DARK_RED + "A ser posible mete texto no numeros");
                            break;
                        }

                        if (sueldoEditar <= 0) {
                            p.sendMessage(ChatColor.DARK_RED + "A ser posible mete numeros que sean superiores a 0");
                            break;
                        }
                        try {
                            empl.conectar("root", "", "pixelcoins");
                            empl.editarSueldo(args[1], args[0], p, args[2], args[3]);
                            empl.desconectar();
                        } catch (Exception e) {

                        }

                        break;
                    case "tiposueldo":
                        String tipoSueldo = args[3];
                        tipoSueldo.toLowerCase();
                        if (tipoSueldo.equalsIgnoreCase("s") == false && tipoSueldo.equalsIgnoreCase("2s") == false && tipoSueldo.equalsIgnoreCase("d") == false && tipoSueldo.equalsIgnoreCase("m") == false) {
                            p.sendMessage(ChatColor.DARK_RED + "Tipo incorrecto:");
                            p.sendMessage(ChatColor.DARK_RED + "d: el sueldo se paga diariamente");
                            p.sendMessage(ChatColor.DARK_RED + "s: el sueldo se paga cada semana");
                            p.sendMessage(ChatColor.DARK_RED + "2s: el sueldo se paga cada 2 semanas");
                            p.sendMessage(ChatColor.DARK_RED + "m: el sueldo se paga cada mes");
                            break;
                        }
                        try {
                            empl.conectar("root", "", "pixelcoins");
                            empl.editarSueldo(args[1], args[0], p, args[2], args[3]);
                            empl.desconectar();
                        } catch (Exception e) {

                        }

                        break;
                    default:
                        p.sendMessage(ChatColor.DARK_RED + "Tipo incorrecto, tipos:");
                        p.sendMessage(ChatColor.DARK_RED + "sueldo");
                        p.sendMessage(ChatColor.DARK_RED + "tiposueldo");
                        break;
                }
                break;
            case "miempresa":
                if (args.length != 1) {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /miempresa <nombrede tu empresa>");
                    return true;
                }
                try {
                    ;
                    empr.conectar("root", "", "pixelcoins");
                    empr.verEmpresa(p, args[0]);
                    empr.desconectar();
                } catch (Exception e) {

                }
                break;
            case "venderempresa":
                if (args.length != 3) {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /venderempresa <empresa> <jugador> <precio>");
                    return true;
                }
                int precio;
                try {
                    precio = Integer.parseInt(args[2]);
                    if (precio <= 0) {
                        p.sendMessage(ChatColor.DARK_RED + "A ser posible mete numeros que no sean negativo o que no sean ceros");
                        return true;
                    }
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "A ser posible mete numeros de precio no texto");
                    return true;
                }
                String jugador = args[1];
                if (jugador.equalsIgnoreCase(p.getName())) {
                    p.sendMessage(ChatColor.DARK_RED + "No puedes vender tu empresa a ti mismo");
                    return true;
                }
                Player tp1 = p.getServer().getPlayer(jugador);
                if (tp1 == null) {
                    p.sendMessage(ChatColor.DARK_RED + "Solo puedes vender tu empresa a jugadores que esten online");
                    return true;
                }

                String empresa = args[0];
                try {
                    empr.conectar("root", "", "pixelcoins");
                    boolean reg = empr.estaRegistradoNombre(empresa);
                    if (reg == false) {
                        p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
                        return true;
                    }
                    boolean ow = empr.esOwner(p.getName(), empresa);
                    if (ow == false) {
                        p.sendMessage(ChatColor.DARK_RED + "No eres due�o de esa empresa");
                        return true;
                    }
                    empr.desconectar();
                } catch (Exception e) {

                }
                try {
                    s.conectar("root", "", "pixelcoins");
                    boolean regEnviador = s.estRegistradoEnviador(p.getName());
                    if (regEnviador == true) {
                        p.sendMessage(ChatColor.DARK_RED + "Solo puedes tener una solicitud a la vez, esperar 15s");
                        return true;
                    }
                    boolean regDestinatario = s.estRegistradoEnviador(jugador);
                    if (regDestinatario == true) {
                        p.sendMessage(ChatColor.DARK_RED + "Es jugador ya le han enviado una solicitud, esperar unos 15s");
                        return true;
                    }
                    int maxId = 0;
                    try {
                        t.conectar("root", "", "pixelcoins");
                        t.nuevaTransaccion(jugador, p.getName(), precio, empresa, "EMPRESA");
                        maxId = t.getMaxId();
                        t.desconectar();
                    } catch (Exception e) {

                    }

                    s.nuevaSolicitud(p.getName(), jugador, 3, maxId, p);
                    tp1.sendMessage(ChatColor.GOLD + p.getName() + " te ha enviado una solicitud para que le compres la empresa: " + ChatColor.DARK_AQUA + empresa + " a " + ChatColor.GREEN + precio + " PC " + ChatColor.AQUA + "/aceptarcompra /rechazarcompra");
                    tp1.playSound(tp1.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 10);
                    p.sendMessage(ChatColor.GOLD + "Has enviado la solicitud de venta a " + jugador + " , expira en 15s");
                    s.desconectar();
                } catch (Exception e) {

                }
                break;
            case "editarnombre":
                if (args.length != 2) {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /editarnombre <empresa> <nuevonombre>");
                    break;
                }
                String nuevoNombre = args[1];
                if (nuevoNombre.toCharArray().length > 24) {
                    p.sendMessage(ChatColor.DARK_RED + "La longitud maxima del nombre es 24.");
                    break;
                }
                try {
                    empr.conectar("root", "", "pixelcoins");
                    empr.cambiarNombre(p, args[0], nuevoNombre);
                    empr.desconectar();
                } catch (Exception e) {

                }
                break;
            case "aceptar":
                try {
                    s.conectar("root", "", "pixelcoins");
                    s.aceptarSolicitudDeuda(p);
                    s.desconectar();
                } catch (Exception e) {

                }
                break;
            case "aceptartrabajo":
                try {
                    s.conectar("root", "", "pixelcoins");
                    s.aceptarSolicitudTrabajo(p);
                    s.desconectar();
                } catch (Exception e) {

                }
                break;
            case "rechazar":
                try {
                    s.conectar("root", "", "pixelcoins");

                    boolean reg = s.estRegistradoDestinatario(p.getName());

                    if (reg == false) {
                        p.sendMessage(ChatColor.DARK_RED + "No te han mandado ninguna solicitud");
                        break;
                    } else {
                        s.cancelarSolicitudDeuda(p);
                    }
                    s.desconectar();
                } catch (Exception e) {

                }
                break;
            case "rechazartrabajo":
                try {
                    s.conectar("root", "", "pixelcoins");

                    boolean reg = s.estRegistradoDestinatario(p.getName());

                    if (reg == false) {
                        p.sendMessage(ChatColor.DARK_RED + "No te han mandado ninguna solicitud");
                        break;
                    } else {
                        s.cancelarSolicitudTrabajo(p);
                    }
                    s.desconectar();
                } catch (Exception e) {

                }
                break;
            case "aceptarcompra":
                try {
                    s.conectar("root", "", "pixelcoins");
                    s.aceptarSolicitudCompra(p);
                    s.desconectar();
                } catch (Exception e) {

                }
                break;
            case "mensajes":
                try {
                    m.conectar("root", "", "pixelcoins");
                    m.mostrarMensajes(p);
                    m.desconectar();
                } catch (Exception e) {

                }
                break;
            case "mistrabajos":
                try {
                    empl.conectar("root", "", "pixelcoins");
                    empl.mostarTrabajos(p);
                    empl.desconectar();
                } catch (Exception e) {

                }
                break;
            case "rechazarcompra":
                try {
                    s.conectar("root", "", "pixelcoins");
                    boolean reg = s.estRegistradoDestinatario(p.getName());

                    if (reg == false) {
                        p.sendMessage(ChatColor.DARK_RED + "No te han enviador ninguna solicitud");
                        return true;
                    }
                    s.rechazarSolicitudCompra(p);
                    s.desconectar();
                } catch (Exception e) {

                }
                break;
            case "irse":
                if (args.length != 1) {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /irse <nombreEmpresa>");
                    return true;
                }
                try {
                    empl.conectar("root", "", "pixelcoins");
                    empl.irseEmpresa(args[0], p);
                    empl.desconectar();
                } catch (Exception e) {

                }

                break;
            case "despedir":
                if (args.length != 3) {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /despedir <empresa> <nombreJugador> <razon>");
                    break;
                }

                if (args[1].equalsIgnoreCase(p.getName())) {
                    p.sendMessage(ChatColor.DARK_RED + "No te puuedes despedir a ti mismo");
                    return true;
                }
                try {
                    empl.conectar("root", "", "pixelcoins");
                    empl.despedir(args[0], args[1], args[2], p);
                    empl.desconectar();
                } catch (Exception e) {

                }
                break;
            case "borrarempresa":
                if (args.length != 1) {
                    p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /borrarempresa <nombre de empresa>");
                    return true;
                }
                String empresaNombre = args[0];
                int pixelcoisnEmpresa = 0;
                int maxid = 0;
                try {
                    empr.conectar("root", "", "pixelcoins");
                    boolean reg = empr.estaRegistradoNombre(empresaNombre);

                    if (reg == false) {
                        p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
                        return true;
                    }
                    boolean ow = empr.esOwner(p.getName(), empresaNombre);
                    if (ow == false) {
                        p.sendMessage(ChatColor.DARK_RED + "No eres owner de esa empresa");
                        return true;
                    }
                    pixelcoisnEmpresa = empr.getPixelcoins(empresaNombre);
                    empr.desconectar();
                } catch (Exception e) {

                }
                try {
                    s.conectar("root", "", "pixelcoins");
                    boolean regEnv = s.estRegistradoEnviador(p.getName());
                    boolean regDes = s.estRegistradoDestinatario(p.getName());

                    if (regEnv == true) {
                        p.sendMessage(ChatColor.DARK_RED + "Ya has enviado una solicitud");
                        return true;
                    }
                    if (regDes == true) {
                        p.sendMessage(ChatColor.DARK_RED + "Ya te has o han enviado una solicitud");
                        return true;
                    }

                    try {
                        t.conectar("root", "", "pixelcoins");
                        t.nuevaTransaccion(p.getName(), empresaNombre, pixelcoisnEmpresa, "", "BORRAR_EMPRESA");
                        maxid = t.getMaxId();
                        t.desconectar();
                    } catch (Exception e) {

                    }

                    s.nuevaSolicitud(p.getName(), p.getName(), 4, maxid, p);
                    s.desconectar();
                } catch (Exception e) {

                }

                p.sendMessage(ChatColor.GOLD + "�Seguro que quires borrar tu empresa? " + ChatColor.AQUA + "/confirmar /cancelar");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 10);
                break;
            case "ayuda":
                if (args.length == 0) {
                    this.mostrarAyudas(p);
                } else {
                    String argm = args[0];
                    switch (argm) {
                        case "jugar":
                            this.ayudaJugar(p);
                            break;
                        case "pixelcoins":
                            this.ayudaDinero(p);
                            break;
                        case "tienda":
                            this.ayudaTienda(p);
                            break;
                        case "normas":
                            this.ayudaNormas(p);
                            break;
                        case "estadisticas":
                            this.ayudaEstadisticas(p);
                            break;
                        case "deudas":
                            this.ayudaDeuda(p);
                            break;
                        default:
                            p.sendMessage("hi");
                            this.mostrarAyudas(p);
                            break;
                    }
                }
            default:
                break;
        }
        return true;
    }


    //Ayudas
    private void mostrarAyudas(Player p) {
        p.sendMessage(ChatColor.YELLOW + "Puedes elegir entre:");
        p.sendMessage("         ");
        p.sendMessage(ChatColor.YELLOW + "/ayuda empleado");
        p.sendMessage(ChatColor.YELLOW + "/ayuda empresario");
        p.sendMessage(ChatColor.YELLOW + "/ayuda deudas");
        p.sendMessage(ChatColor.YELLOW + "/ayuda pixelcoins");
        p.sendMessage(ChatColor.YELLOW + "/ayuda tienda");
        p.sendMessage(ChatColor.YELLOW + "/ayuda estadisticas");
        p.sendMessage(ChatColor.YELLOW + "/ayuda normas");
        p.sendMessage(ChatColor.YELLOW + "/ayuda jugar");
    }

    private void ayudaDinero(Player p) {
        p.sendMessage("          ");
        p.sendMessage(ChatColor.YELLOW + " Puedes intercambiar 1 diamante por " + Transacciones.DIAMANTE + " pixelcoins o 1 un bloque de cuarzo por " + Transacciones.CUARZO + " pixelcoins o lapislazuli por " + Transacciones.LAPISLAZULI + " pixelcoins y viceversa  en los wither en " + ChatColor.WHITE + "/warp Spawn " + ChatColor.YELLOW +
                ". Comandos de pixelcoins:");
        p.sendMessage("          ");
        p.sendMessage("/dinero" + ChatColor.GOLD + " Ver el dinero que tienes");
        p.sendMessage("          ");
        p.sendMessage("/pagar NombreDelJugador CantidadDePixelcoins" + ChatColor.GOLD + " Pagar a un jugador un cierto numero de pixelcoins");
        p.sendMessage("          ");
        p.sendMessage("/topricos" + ChatColor.GOLD + " Ver los m�s ricos del servidor");
        p.sendMessage("          ");
        p.sendMessage("/toppobres" + ChatColor.GOLD + " Ver elos m�s pobres del servidor");
        p.sendMessage("          ");
        p.sendMessage("/topfiables" + ChatColor.GOLD + " Ver los jugadores que mas han podido pagar la deuda");
        p.sendMessage("          ");
        p.sendMessage("/topmenosfiables" + ChatColor.GOLD + " Ver los jugadores que mas no han podido pagar la deuda");
        p.sendMessage("          ");
        p.sendMessage("/topvendedores" + ChatColor.GOLD + " Ver los jugadores que mas objetos han vendido en tienda y mas veces han sido pagados");
        p.sendMessage("          ");
        p.sendMessage("/estadistcias" + ChatColor.GOLD + " Ver todas tus estadisticas de la tienda, deudas etc. /ayuda estadisticas");

    }

    private void ayudaEmpresario(Player p) {
        p.sendMessage("   ");
        p.sendMessage(ChatColor.YELLOW + "Puedes hacer tu propia empresa, contratar a gente, venderla, ganar pixelcoins etc, principales comandos:");
        p.sendMessage("/crearempresa <nombre> <" + ChatColor.GOLD + "");
    }

    private void ayudaNormas(Player p) {
        p.sendMessage("          ");
        p.sendMessage(ChatColor.RED + " El incumplimiento de las siguintes normas resultara con un baneo de tiempo: La primera vez que has sido baneado: 1 dia, segunda vez 3 dias y tercera vez 10 dias. Normas:");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.RED + "1� " + ChatColor.GOLD + "Matar continuamente a alguien de manera que le inpida jugar");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.RED + "2� " + ChatColor.GOLD + "no insultar en el chat de manera que ofenda al jugador (ofendidito xd)");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.RED + "3�" + ChatColor.GOLD + "respetar el territorio de los demas (no expandirse de manera exajerada, no pegar las casas maximo 5 bloques de separacion sin el consentimiento del jugador)");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.RED + "4� " + ChatColor.GOLD + "no utilizar hacks (xray, kill aura, kurium etc)");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.RED + "5� " + ChatColor.GOLD + "no explotar bugs");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.RED + "Si has hecho algo grave que no entra en estas razones seras igualmente baneado");
        p.sendMessage("          ");
    }

    private void ayudaJugar(Player p) {
        p.sendMessage("          ");
        p.sendMessage(ChatColor.YELLOW + " Para jugar:");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.GOLD + "Para construir una casa la puedes construir donde quieras pero sin que este muy cerca de otra casa de otro jugador (/ayuda normas) y que no este en el spawn");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.GOLD + "Para proteger tus cofres/puertas/shulker box etc : " + ChatColor.WHITE + "/cprivate " + ChatColor.GOLD + "y click derecho en ellos");
        p.sendMessage(ChatColor.GOLD + "Para quitarles la proteccion: " + ChatColor.WHITE + "/remove " + ChatColor.GOLD + "y click derecho en ellos");
        p.sendMessage(ChatColor.GOLD + "Para ponerles contrase�a " + ChatColor.WHITE + "/cpassword <contrase�a> " + ChatColor.GOLD + "y click derecho en ellos " +
                ChatColor.GOLD + "y para desbloquarlos " + ChatColor.WHITE + "/cunlock <contrase�a> " + ChatColor.GOLD + "y click derecho en ellos");
        p.sendMessage("          ");
        p.sendMessage("/warp <nombre del sitio donde quiere ir>" + ChatColor.GOLD + " Para teletransportarte a un sitio donde quieres ir (/warp para ver los sitios)");
        p.sendMessage("          ");
        p.sendMessage("/sethome " + ChatColor.GOLD + "Para fijar tu casa. " + ChatColor.WHITE + "/home " + ChatColor.GOLD + "Para ir a tu casa");
        p.sendMessage("          ");
    }

    private void ayudaTienda(Player p) {
        p.sendMessage("          ");
        p.sendMessage(ChatColor.GOLD + "En la tienda puedes comprar y vender objetos de manera segura y rapida sin requerir de que el vendedor este online. Se accede con /tienda o /warp spawn y clickeando al NPC llamando tienda.");
        p.sendMessage("          ");
        p.sendMessage("/vender <precio de pixelcoins>" + ChatColor.GOLD + "Con este podras lanzar a la venta el objeto que tengas en la mano a un determinado precio en la tienda, solo podras tener 4 como maximo en la tienda a le vez.");
        p.sendMessage("          ");
        p.sendMessage("/tienda " + ChatColor.GOLD + "Con este comando podras acceder a la tienda");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.GOLD + " Para comprar objetos tendras que darle click izquierdo y automaticamente se te comprar y se a�adara al inventario.");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.GOLD + "Para quitarlos click izquierdo en tus objetos que esten en la venta.");
        p.sendMessage("          ");
    }

    private void ayudaDeuda(Player p) {
        p.sendMessage("   ");
        p.sendMessage(ChatColor.GOLD + "Puedes prestar dinero a los jugadores poniendo asi un interes y una cantidad de tiempo en el que el jugador te devolvero el dinero");
        p.sendMessage("   ");
        p.sendMessage(ChatColor.GOLD + "- Si quieres puedes poner un interes a tu prestamo. Es la cantidad aumentada en un % al que el jugador que se le ha prestado las pixelcoins tendra que delvolver dentro de un tiempo");
        p.sendMessage("   ");
        p.sendMessage(ChatColor.GOLD + "- Para conceder un prestamo (/prestar) se le enviara al jugador una solicitud que expirara en 15s que al aceptarla se le cobrara el prestamo de maneta inmediata (sin interes)");
        p.sendMessage("   ");
        p.sendMessage(ChatColor.GOLD + "- Al conceberse el prestamo, cada dia que pase en la vida real en el que el server este abierto, se le cobrara al jugador que ha prestado las pixelcoins una cierta cantidad todos los dias, para calcular esa cantidad: " +
                "que se calculara : pixelcoinsPrestadas con interes / dias a prestar. Asi delvolviendo al jugador la cantidad que presto mas el interes.");
        p.sendMessage("   ");
        p.sendMessage(ChatColor.GOLD + "- Si el jugador que se le ha concedido el prestamo no tiene el dinero suficiente para pagarlo, a la minima que tenga pixelcoins y se abra el server se pagara el prestamo. y si le anotara que no ha podigo pagar el prestamo (/topmenosfiables)");
        p.sendMessage("   ");
        p.sendMessage(ChatColor.GOLD + "Ejmeplo: Si julia presta a jaime 100 pixelcoins a 2 dias y a un interes del 10% (/pagar JaimeTruman 100 2 10): 1� Se le pagara a jaime 100 pixelcoins 2� Cada dia que pase jaime pagara una cuota (110/2 = 55) asi hasta 2 dias, Si Jaime no tiene pixelcoins para pagar un dia pagara el siguinete con la cuota establecida como si nada");
        p.sendMessage("     ");
        p.sendMessage("/prestar <nombreJugador> <pixelcoins> <dias> [interes]" + ChatColor.GOLD + " El interes es opcional");
        p.sendMessage("     ");
        p.sendMessage("/deudas" + ChatColor.GOLD + " Ver toda la informacion de las deudas que tienes");
    }

    private void ayudaEstadisticas(Player p) {
        p.sendMessage("      ");
        p.sendMessage(ChatColor.GOLD + "Las estadisticas se miden solo de la tienda y por pagamos mediante el comando /pagar");
        p.sendMessage("        ");
        p.sendMessage("N� Ventas " + ChatColor.GOLD + "El numero de veces que te han comprado en la tienda y te han pagado mediante /pagar");
        p.sendMessage("      ");
        p.sendMessage("Precio/Venta " + ChatColor.GOLD + "Se calcula: ingresos/n� de ventas. Trata sobre dar el precio medio por el cual vendes en la tienda y te te pagan con /pagar");
        p.sendMessage("      ");
        p.sendMessage("Ingresos " + ChatColor.GOLD + "Es la suma de todo el dinero que te han pagado mediante /pagar y del dinero que te han pagado en la tienda");
        p.sendMessage("         ");
        p.sendMessage("Gastos " + ChatColor.GOLD + "Es la suma de todo el dinero que te has gastado en la tienda y todo el dinero que has pagado a jugadores mediante /pagar");
        p.sendMessage("        ");
        p.sendMessage("Beneficios " + ChatColor.GOLD + "Se calcula mediante ingresos - gastos. Es el total del dinero que has ganado o perdido");
        p.sendMessage("       ");
        p.sendMessage("Rentabilidad " + ChatColor.GOLD + "Se calcula: (beneficios / ingresos) * 100. Representa sobre 100 PC cuantas has ganado o perdido(Si se indica en rojo)");
        p.sendMessage("       ");
        p.sendMessage("Pixelcoins que debes " + ChatColor.GOLD + "El total de dinero que debes a jugadores (/ayuda deuda o /deudas)");
        p.sendMessage("       ");
        p.sendMessage("Pixelcoin que te deben " + ChatColor.GOLD + "El total de dinero que te deben (ayuda deudas o /deudas)");
        p.sendMessage("       ");
        p.sendMessage("N� inpago" + ChatColor.GOLD + "El total de veces que no has podido pagar la deuda en un dia");
        p.sendMessage("       ");
        p.sendMessage("N� pago " + ChatColor.GOLD + "El total de veces que has pagado la deuda");
    }
}
