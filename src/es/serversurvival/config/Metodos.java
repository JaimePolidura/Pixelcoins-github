package es.serversurvival.config;


import es.serversurvival.config.Metodos;

import java.text.DecimalFormat;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class Metodos {
    public DecimalFormat formatea = new DecimalFormat("###,###.##");
    public static Connection conexion;
    int cambioDiamantes = 750;
    int cambioCuarzo = 10;
    public Funciones f = new Funciones();

    //Metodo para conectarme a la base de datos
    public void conectar(String user, String pass, String dbName) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, user, pass);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, e);
        } catch (SQLException e) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //Metodo para desconectar
    public void desconectar() {
        try {
            conexion.close();
        } catch (SQLException e) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //------------Diamantes --> PixelCoins----------- 
    public void cambiarDiamantes(Player p) {
        Inventory in = p.getInventory();
        @SuppressWarnings("deprecation")
        ItemStack itemMano = p.getItemInHand();
        boolean done = false;
        boolean done2 = false;
        int posicion = 0;
        String nombreJugador = p.getName();
        int actuales = 0;
        int cantidad = 0;


        //comprobar si tienes un diamante en la mano y sacar el slot
        for (int i = 0; i < 9; i++) {
            ItemStack itemI = in.getItem(i);
            if (itemMano.isSimilar(itemI) && itemMano.getType() == Material.DIAMOND) {
                cantidad = itemMano.getAmount();
                posicion = i;
                done = true;

                break;
            }
        }

        //tienes un diamante en la mano
        if (done == true) {
            in.clear(posicion);
            int dinero = 0;
            int espaciosActuales = 0;
            int nventas = 0;
            int ingresos = 0;
            int gastos = 0;
            int beneficios = 0;

            try {
                String consulta1 = "SELECT * FROM jugadores ";
                Statement st = conexion.createStatement();
                ResultSet rs;
                rs = st.executeQuery(consulta1);
                String nombreActual = "";

                //recorrer la tabal para ver si el jugador esta registrado en la base de datos o no y si si sacar sus pixelcoins actuales
                while (rs.next()) {
                    nombreActual = rs.getString("nombre");
                    if (nombreActual.equalsIgnoreCase(nombreJugador)) {
                        nventas = rs.getInt("nventas");
                        ingresos = rs.getInt("ingresos");
                        gastos = rs.getInt("gastos");
                        beneficios = rs.getInt("beneficios");
                        espaciosActuales = rs.getInt("espacios");
                        actuales = rs.getInt("pixelcoin");
                        done2 = true;

                        break;
                    }
                }
                //El jugador ya esta registrado en la base de datos
                if (done2 == true) {
                    dinero = (cantidad * cambioDiamantes) + actuales;
                    String consulta3 = "UPDATE jugadores SET pixelcoin = ?, espacios = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
                    PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta3);

                    pst.setInt(1, dinero);
                    pst.setInt(2, espaciosActuales);
                    pst.setInt(3, nventas);
                    pst.setInt(4, ingresos);
                    pst.setInt(5, gastos);
                    pst.setInt(6, beneficios);
                    pst.setString(7, nombreJugador);

                    pst.executeUpdate();
                    p.sendMessage(ChatColor.GOLD + "Se ha a�adido: " + ChatColor.GREEN + formatea.format(cantidad * cambioDiamantes) + " PC " + ChatColor.GOLD + "Ahora tienes: " + ChatColor.GREEN +
                            formatea.format(dinero) + " PC");
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                    //el jugador no estaba registrado en la base de datos
                } else {
                    dinero = (cantidad * cambioDiamantes) + actuales;
                    String consulta2 = "INSERT INTO jugadores (nombre, pixelcoin, espacios, nventas, ingresos, gastos, beneficios) VALUES ('" + nombreJugador + "','" + dinero + "','" + espaciosActuales + "','" + nventas + "','" + ingresos + "','" + gastos + "','" + beneficios + "')";
                    Statement st2 = (Statement) conexion.createStatement();
                    st2.executeUpdate(consulta2);
                    p.sendMessage(ChatColor.GOLD + "Se ha a�adido: " + ChatColor.GREEN + formatea.format(cantidad * cambioDiamantes) + " PC " + ChatColor.GOLD + "Ahora tienes: " + ChatColor.GREEN +
                            formatea.format(dinero) + " PC");
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                }

            } catch (SQLException e) {
                p.sendMessage(ChatColor.RED + "Error en Metodos.cambiarDiamantes, hablar con el admin");
            }
        } else {
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            p.sendMessage(ChatColor.DARK_RED + "Necesitas tener diamantes en la mano para intercambiarlas por pixel coins");
        }
    }

    //----------Pixel Coins --> Diamantes----------
    @SuppressWarnings("deprecation")
    public void cambiarPixelCoinsDia(Player p) {
        int actuales = 0;
        String nombreJugador = p.getName();
        Inventory in = p.getInventory();
        int espaciosLibres = f.espaciosLibres(in);
        boolean done = false;
        int aux = 0;
        int espaciosActuales = 0;
        int nventas = 0;
        int ingresos = 0;
        int gastos = 0;
        int beneficios = 0;
        //comprobar que tengas espacios libres O que si tienes diamantes en la mano que la cantidad no sea 64
        if (espaciosLibres != 0 || (p.getItemInHand().getAmount() != 64 && p.getItemInHand().getType() == Material.DIAMOND)) {
            try {
                String consulta1 = "SELECT * FROM jugadores ";
                Statement st = conexion.createStatement();
                ResultSet rs;
                rs = st.executeQuery(consulta1);

                while (rs.next()) {
                    String nombreActual = rs.getString("nombre");
                    if (nombreActual.equalsIgnoreCase(nombreJugador)) {
                        espaciosActuales = rs.getInt("espacios");
                        actuales = rs.getInt("pixelcoin");
                        nventas = rs.getInt("nventas");
                        ingresos = rs.getInt("ingresos");
                        gastos = rs.getInt("gastos");
                        beneficios = rs.getInt("beneficios");

                        done = true;
                        break;
                    }
                }
                //Si se ha encontrado al jugador y las PC que tiene son mayores o iguales a 750 
                if (done == true && actuales >= cambioDiamantes) {
                    aux = 1;
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Necesitas tener 750 o m�s pixel coins, para convertirlas a diamantes");
                }

            } catch (SQLException e) {
                p.sendMessage(ChatColor.RED + "Error en MySQL.cambiarPixelCoinDia, hablar con el admin");
            }
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Tines el inventario lleno");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
        //a�adir diamantes si  se ha encontrado al jugador y las PC que tiene son mayores o iguales a 750  (linea 147)
        if (aux == 1) {
            try {
                String consulta2 = "UPDATE jugadores SET pixelcoin = ?, espacios = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
                PreparedStatement pst2 = (PreparedStatement) conexion.prepareStatement(consulta2);
                pst2.setInt(1, (actuales - cambioDiamantes));
                pst2.setInt(2, espaciosActuales);
                pst2.setInt(3, nventas);
                pst2.setInt(4, ingresos);
                pst2.setInt(5, gastos);
                pst2.setInt(6, beneficios);
                pst2.setString(7, nombreJugador);

                pst2.executeUpdate();
                in.addItem(new ItemStack(Material.DIAMOND, 1));
                p.sendMessage(ChatColor.GOLD + "Se ha a�adido un diamante. " + ChatColor.RED + "-750 PC " + ChatColor.GOLD + "Quedan " + ChatColor.GREEN + formatea.format(actuales - cambioDiamantes) + " PC");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            } catch (SQLException e) {
                p.sendMessage(ChatColor.RED + "Error en Metodos.cambiarPixelCoinDia, hablar con el admin");
            }
        }
    }

    //---------Bloques diamante -> pixelcoins-------
    public void cambiarBloquesDiamante(Player p) {
        Inventory in = p.getInventory();
        @SuppressWarnings("deprecation")
        ItemStack itemMano = p.getItemInHand();
        boolean done = false;
        boolean done2 = false;
        int posicion = 0;
        String nombreJugador = p.getName();
        int actuales = 0;
        int cantidad = 0;

        //comprobar si tienes un bloque de diamante en la mano y sacar el slot
        for (int i = 0; i < 9; i++) {
            ItemStack itemI = in.getItem(i);
            if (itemMano.isSimilar(itemI) && itemMano.getType() == Material.DIAMOND_BLOCK) {
                cantidad = itemMano.getAmount();
                posicion = i;
                done = true;

                break;
            }
        }
        // tienes un bloque de diamante en la mano
        if (done == true) {
            in.clear(posicion);
            int dinero = 0;
            int espaciosActuales = 0;
            int nventas = 0;
            int ingresos = 0;
            int gastos = 0;
            int beneficios = 0;

            try {
                String consulta1 = "SELECT * FROM jugadores ";
                Statement st = conexion.createStatement();
                ResultSet rs;
                rs = st.executeQuery(consulta1);
                String nombreActual = "";

                //recorrer la tabla para ver si el jugador se ha encontrado o no y si si sacar sus pixelcoins
                while (rs.next()) {
                    nombreActual = rs.getString("nombre");
                    if (nombreActual.equalsIgnoreCase(nombreJugador)) {
                        nventas = rs.getInt("nventas");
                        ingresos = rs.getInt("ingresos");
                        gastos = rs.getInt("gastos");
                        beneficios = rs.getInt("beneficios");
                        espaciosActuales = rs.getInt("espacios");
                        actuales = rs.getInt("pixelcoin");
                        done2 = true;

                        break;
                    }
                }
                //se ha encontrado que el jugador esta registrado en la base de datos
                if (done2 == true) {
                    dinero = (cantidad * cambioDiamantes * 9) + actuales;
                    String consulta3 = "UPDATE jugadores SET pixelcoin = ?, espacios = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
                    PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta3);
                    pst.setInt(1, dinero);
                    pst.setInt(2, espaciosActuales);
                    pst.setInt(3, nventas);
                    pst.setInt(4, ingresos);
                    pst.setInt(5, gastos);
                    pst.setInt(6, beneficios);
                    pst.setString(7, nombreJugador);

                    pst.executeUpdate();

                    p.sendMessage(ChatColor.GOLD + "Se ha a�adido: " + ChatColor.GREEN + formatea.format(cantidad * cambioDiamantes * 9) + " PC " + ChatColor.GOLD + "Ahora tienes: " + ChatColor.GREEN +
                            formatea.format(dinero) + " PC");
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                    //el jugador no esta registrado en la base de datos

                } else {
                    dinero = (cantidad * cambioDiamantes * 9) + actuales;
                    String consulta2 = "INSERT INTO jugadores (nombre, pixelcoin, espacios, nventas, ingresos, gastos, beneficios) VALUES ('" + nombreJugador + "','" + dinero + "','" + espaciosActuales + "','" + nventas + "','" + ingresos + "','" + gastos + "','" + beneficios + "')";
                    Statement st2 = (Statement) conexion.createStatement();
                    st2.executeUpdate(consulta2);
                    p.sendMessage(ChatColor.GOLD + "Se ha a�adido: " + ChatColor.GREEN + formatea.format(cantidad * cambioDiamantes * 9) + " PC " + ChatColor.GOLD + "Ahora tienes: " + ChatColor.GREEN +
                            formatea.format(dinero) + " PC");
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                }

            } catch (SQLException e) {
                p.sendMessage(ChatColor.RED + "Error en Metodos.cambiarBloquesDiamante, hablar con el admin");
            }
        } else {
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            p.sendMessage(ChatColor.DARK_RED + "Necesitas tener bloques de diamante en la mano para intercambiarlas por pixel coins");
        }
    }

    //---------------Metedo maximas pixelcoins -> Maximos diamantes-------------
    public void cambiarMaxPixelCoinsDiamante(Player p) {

        Inventory in = p.getInventory();
        String nombreJugador = p.getName();
        int espaciosLibres = f.espaciosLibres(in);
        int actuales = 0;
        int espaciosActuales = 0;
        int nventas = 0;
        int ingresos = 0;
        int gastos = 0;
        int beneficios = 0;

        try {
            String consulta1 = "SELECT * FROM jugadores ";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta1);

            //recorrer la base de datos y ver si el jugador esta registrado y si si sacar sus pixelcoins actuales
            while (rs.next()) {
                String nombreActual = rs.getString("nombre");
                if (nombreActual.equalsIgnoreCase(nombreJugador)) {
                    actuales = rs.getInt("pixelcoin");
                    espaciosActuales = rs.getInt("espacios");
                    nventas = rs.getInt("nventas");
                    ingresos = rs.getInt("ingresos");
                    gastos = rs.getInt("gastos");
                    beneficios = rs.getInt("beneficios");

                    break;
                }
            }
        } catch (SQLException e) {
            p.sendMessage(ChatColor.RED + "Error en Metodos.cambiarBloquesDiamante, hablar con el admin");
        }

        //comprobar si las PC que tiene es superior o igual a 750 y que tenga espacios libres en el inventario
        if (actuales >= cambioDiamantes && espaciosLibres > 0) {
            int convertibles = actuales - (actuales % cambioDiamantes);
            int diamantes = (convertibles / cambioDiamantes) % 9;
            int bloques = ((convertibles / cambioDiamantes) - diamantes) / 9;

            int[] slotsBloques = f.slotsItem(bloques, espaciosLibres);
            int bloquesA�adidos = 0;
            int diamantesA�adidos = 0;

            //A�adir bloques
            for (int i = 0; i < slotsBloques.length; i++) {
                bloquesA�adidos = bloquesA�adidos + slotsBloques[i];
                in.addItem(new ItemStack(Material.DIAMOND_BLOCK, slotsBloques[i]));
            }

            int[] slotsDiamantes = f.slotsItem(diamantes, f.espaciosLibres(in));
            //A�adir diamantes
            for (int i = 0; i < slotsDiamantes.length; i++) {
                diamantesA�adidos = diamantesA�adidos + slotsDiamantes[i];
                in.addItem(new ItemStack(Material.DIAMOND, slotsDiamantes[i]));
            }
            int coste = (bloquesA�adidos * cambioDiamantes * 9)+(diamantesA�adidos * cambioDiamantes);

            //restar al jugador el coste de los bloques de todos los diamantes que ha sacado
            try {
                String consulta2 = "UPDATE jugadores SET pixelcoin = ?, espacios = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
                PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);
                pst.setInt(1, (actuales - coste));
                pst.setInt(2, espaciosActuales);
                pst.setInt(3, nventas);
                pst.setInt(4, ingresos);
                pst.setInt(5, gastos);
                pst.setInt(6, beneficios);
                pst.setString(7, nombreJugador);

                pst.executeUpdate();

                p.sendMessage(ChatColor.GOLD + "Se ha a�adio: " + ChatColor.AQUA + "+" + bloquesA�adidos + " bloques " + "+" + diamantesA�adidos + " diamantes. " + ChatColor.RED + "-" + formatea.format(coste)
                        + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + (actuales - coste) + " PC");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            } catch (SQLException e) {
                p.sendMessage(ChatColor.RED + "Error en Metodos.cambiarPixelCoinDia, hablar con el admin");
            }

            // si tiene menos de 750
        } else if (actuales < 750) {
            p.sendMessage(ChatColor.DARK_RED + "Necesitas tener 750 o m�s pixel coins, para convertirlas a diamantes");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
    }

    //-------------Cuarzo -> pixelcoins-------------
    public void cambiarCuarzo(Player p) {
        Inventory in = p.getInventory();
        @SuppressWarnings("deprecation")
        ItemStack itemMano = p.getItemInHand();
        boolean done = false;
        boolean done2 = false;
        int posicion = 0;
        String nombreJugador = p.getName();
        int actuales = 0;
        int cantidad = 0;
        int espaciosActuales = 0;


        //comprobar si tienes un diamante en la mano y sacar el slot
        for (int i = 0; i < 9; i++) {
            ItemStack itemI = in.getItem(i);
            if (itemMano.isSimilar(itemI) && itemMano.getType() == Material.QUARTZ_BLOCK) {
                cantidad = itemMano.getAmount();
                posicion = i;
                done = true;

                break;
            }
        }

        //tienes un bloque de cuarzo en la mano
        if (done == true) {
            in.clear(posicion);
            int dinero = 0;
            int nventas = 0;
            int ingresos = 0;
            int gastos = 0;
            int beneficios = 0;

            try {
                String consulta1 = "SELECT * FROM jugadores ";
                Statement st = conexion.createStatement();
                ResultSet rs;
                rs = st.executeQuery(consulta1);
                String nombreActual = "";

                //comprobar si el jugador esta registrado en la base de datos y si si sacar sus PC actuales
                while (rs.next()) {
                    nombreActual = rs.getString("nombre");
                    if (nombreActual.equalsIgnoreCase(nombreJugador)) {
                        espaciosActuales = rs.getInt("espacios");
                        actuales = rs.getInt("pixelcoin");
                        nventas = rs.getInt("nventas");
                        ingresos = rs.getInt("ingresos");
                        gastos = rs.getInt("gastos");
                        beneficios = rs.getInt("beneficios");

                        done2 = true;
                        break;
                    }
                }
                //esta registrado
                if (done2 == true) {
                    dinero = (cantidad * cambioCuarzo) + actuales;
                    String consulta3 = "UPDATE jugadores SET pixelcoin = ?, espacios = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
                    PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta3);
                    pst.setInt(1, dinero);
                    pst.setInt(2, espaciosActuales);
                    pst.setInt(3, nventas);
                    pst.setInt(4, ingresos);
                    pst.setInt(5, gastos);
                    pst.setInt(6, beneficios);
                    pst.setString(7, nombreJugador);

                    pst.executeUpdate();

                    p.sendMessage(ChatColor.GOLD + "Se ha a�adido: " + ChatColor.GREEN + formatea.format(cantidad * cambioCuarzo) + " PC " + ChatColor.GOLD + "Ahora tienes: " + ChatColor.GREEN +
                            formatea.format(dinero) + " PC");
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                    //no esta registrado
                } else {
                    dinero = (cantidad * cambioCuarzo) + actuales;
                    String consulta2 = "INSERT INTO jugadores (nombre, pixelcoin, espacios, nventas, ingresos, gastos, beneficios) VALUES ('" + nombreJugador + "','" + dinero + "','" + espaciosActuales + "','" + nventas + "','" + ingresos + "','" + gastos + "','" + beneficios + "')";
                    Statement st2 = (Statement) conexion.createStatement();
                    st2.executeUpdate(consulta2);
                    p.sendMessage(ChatColor.GOLD + "Se ha a�adido: " + ChatColor.GREEN + formatea.format(cantidad * cambioCuarzo) + " PC " + ChatColor.GOLD + "Ahora tienes: " + ChatColor.GREEN +
                            formatea.format(dinero) + " PC");
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                }

            } catch (SQLException e) {
                p.sendMessage(ChatColor.RED + "Error en Metodos.cambiarCuarzo, hablar con el admin");
            }
        } else {
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            p.sendMessage(ChatColor.DARK_RED + "Necesitas tener bloques de cuarzo en la mano para intercambiarlas por pixel coins");
        }
    }

    //-------------Pixelcoins -> cuarzo--------------
    @SuppressWarnings("deprecation")
    public void cambiarPixelCoinsCua(Player p) {
        int actuales = 0;
        String nombreJugador = p.getName();
        Inventory in = p.getInventory();
        int espaciosLibres = f.espaciosLibres(in);
        boolean done = false;
        int aux = 0;
        int espaciosActuales = 0;
        int nventas = 0;
        int ingresos = 0;
        int gastos = 0;
        int beneficios = 0;

        //comprobar que tenga espacios libres en el inventario O que tenga un bloque de cuarzo y que tenga una cantidad inferior a 64
        if (espaciosLibres != 0 || (p.getItemInHand().getAmount() != 64 && p.getItemInHand().getType() == Material.QUARTZ_BLOCK)) {
            try {
                String consulta1 = "SELECT * FROM jugadores ";
                Statement st = conexion.createStatement();
                ResultSet rs;
                rs = st.executeQuery(consulta1);

                //comproba si el jugador esta registrado en la base de datos y si si sacar sus PC actuales
                while (rs.next()) {
                    String nombreActual = rs.getString("nombre");
                    if (nombreActual.equalsIgnoreCase(nombreJugador)) {
                        espaciosActuales = rs.getInt("espacios");
                        actuales = rs.getInt("pixelcoin");
                        nventas = rs.getInt("nventas");
                        ingresos = rs.getInt("ingresos");
                        gastos = rs.getInt("gastos");
                        beneficios = rs.getInt("beneficios");

                        done = true;
                        break;
                    }
                }

                //esta registrado y sus PC son superiores a 10 PC
                if (done == true && actuales >= cambioCuarzo) {
                    aux = 1;
                } else {
                    //tiene menos de 10 PC
                    p.sendMessage(ChatColor.DARK_RED + "Necesitas tener 10 o m�s pixel coins, para convertirlas a bloques de cuarzo");
                }

            } catch (SQLException e) {
                p.sendMessage(ChatColor.RED + "Error en Metodos.cambiarPixelCoinsCua, hablar con el admin");
            }
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Tines el inventario lleno");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }

        // si esta registrado y si su cantidad de pc es igual o superior a 10
        if (aux == 1) {
            try {
                in.addItem(new ItemStack(Material.QUARTZ_BLOCK, 1));
                String consulta2 = "UPDATE jugadores SET pixelcoin = ?, espacios = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
                PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);
                pst.setInt(1, (actuales - cambioCuarzo));
                pst.setInt(2, espaciosActuales);
                pst.setInt(3, nventas);
                pst.setInt(4, ingresos);
                pst.setInt(5, gastos);
                pst.setInt(6, beneficios);
                pst.setString(7, nombreJugador);
                pst.executeUpdate();

                p.sendMessage(ChatColor.GOLD + "Se ha a�adido un bloque de cuarzo. " + ChatColor.RED + "-10 PC " + ChatColor.GOLD + "Quedan " + ChatColor.GREEN + formatea.format(actuales - cambioCuarzo) + " PC");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            } catch (SQLException e) {
                p.sendMessage(ChatColor.RED + "Error en Metodos.cambiarPixelCoinsCua, hablar con el admin");
            }
        }
    }

    //----------Maximas pixelcoins-> Maximos bloques de cuarzo-------
    public void cambiarMaxPixelCoinsCuarzo(Player p) {
        Inventory in = p.getInventory();
        String nombreJugador = p.getName();
        int espaciosLibres = f.espaciosLibres(in);
        int actuales = 0;
        int espaciosActuales = 0;
        int nventas = 0;
        int ingresos = 0;
        int gastos = 0;
        int beneficios = 0;

        try {
            String consulta1 = "SELECT * FROM jugadores ";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta1);
            // sacar las PC actuales del jugador
            while (rs.next()) {
                String nombreActual = rs.getString("nombre");
                if (nombreActual.equalsIgnoreCase(nombreJugador)) {
                    espaciosActuales = rs.getInt("espacios");
                    actuales = rs.getInt("pixelcoin");
                    nventas = rs.getInt("nventas");
                    ingresos = rs.getInt("ingresos");
                    gastos = rs.getInt("gastos");
                    beneficios = rs.getInt("beneficios");

                    break;
                }
            }
        } catch (SQLException e) {
            p.sendMessage(ChatColor.RED + "Error en Metodos.cambiarBloquesDiamante, hablar con el admin");
        }

        // si sus PC actuales es superior a 10 y tiene espacios libres en el inventario 
        if (actuales >= cambioCuarzo && espaciosLibres > 0) {
            int bloques = (actuales - (actuales % 10)) / cambioCuarzo;

            int[] slotsBloques = f.slotsItem(bloques, espaciosLibres);
            int bloquesA�adidos = 0;

            //A�adir bloques
            for (int i = 0; i < slotsBloques.length; i++) {
                bloquesA�adidos = bloquesA�adidos + slotsBloques[i];
                in.addItem(new ItemStack(Material.QUARTZ_BLOCK, slotsBloques[i]));
            }

            int coste = bloquesA�adidos * cambioCuarzo;

            // poner el coste al juagdor en la base de datos
            try {
                String consulta2 = "UPDATE jugadores SET pixelcoin = ?, espacios = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
                PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta2);
                pst.setInt(1, (actuales - coste));
                pst.setInt(2, espaciosActuales);
                pst.setInt(3, nventas);
                pst.setInt(4, ingresos);
                pst.setInt(5, gastos);
                pst.setInt(6, beneficios);
                pst.setString(7, nombreJugador);
                pst.executeUpdate();

                p.sendMessage(ChatColor.GOLD + "Se ha a�adio: " + ChatColor.GRAY + "+" + formatea.format(bloquesA�adidos) + " bloques de cuarzo. " + ChatColor.RED + "-" + formatea.format(coste)
                        + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + formatea.format(actuales - coste) + " PC");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            } catch (SQLException e) {
                p.sendMessage(ChatColor.RED + "Error en Metodos.cambiarMaxPixelCoinsCuarzo, hablar con el admin");
            }
            //tiene menos de 10 PC
        } else if (actuales < 10) {
            p.sendMessage(ChatColor.DARK_RED + "Necesitas tener 10 o m�s pixel coins, para convertirlas a bloques de cuarzo");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
    }
}	