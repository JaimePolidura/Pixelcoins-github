package es.serversurvival.config;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Ayuda {
    public class Comandos {


    }

    //Ayuda
    public void MostrarAyudas(Player p) {
        p.sendMessage(ChatColor.YELLOW + "Puedes elegir entre:");
        p.sendMessage("         ");
        p.sendMessage(ChatColor.YELLOW + "/ayuda dinero");
        p.sendMessage(ChatColor.YELLOW + "/ayuda normas");
        p.sendMessage(ChatColor.YELLOW + "/ayuda jugar");
        p.sendMessage(ChatColor.YELLOW + "/ayuda tienda");
    }

    //ayuda dinero
    public void AyudaDinero(Player p) {
        p.sendMessage("          ");
        p.sendMessage(ChatColor.YELLOW + " Puedes intercambiar 1 diamante por 750 pixelcoins o 1 un bloque de cuarzo por 10 pixelcoins y viceversa  en los wither en " + ChatColor.WHITE + "/warp Spawn " + ChatColor.YELLOW +
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
        p.sendMessage("/topvendedores" + ChatColor.GOLD + " Ver los jugadores que mas objetos han vendido en tienda y mas veces han sido pagados");
        p.sendMessage("          ");
        p.sendMessage("/estadistcias" + ChatColor.GOLD + " Ver todos los objetos que te han comprado, tus ingresos (Todas las PC obtenidas en la tienda y que te han pagado), gastos (El numero de ventas que has hecho en la tienda), "
                + "y beneficios (ingresos - gastos)");

    }

    //ayuda normas
    public void AyudaNormas(Player p) {
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

    //ayuda jugar
    public void AyudaJugar(Player p) {
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

    //ayuda tienda
    public void AyudaTienda(Player p) {
        p.sendMessage("          ");
        p.sendMessage(ChatColor.YELLOW + "En la tienda puedes comprar y vender objetos de manera segura y rapida sin requerir de que el vendedor este online. Se accede con /tienda o /warp spawn y clickeando al NPC llamando tienda.");
        p.sendMessage("          ");
        p.sendMessage("/verOfertas " + ChatColor.GOLD + "Ver todos los objetos que tienes en la tienda, como maximo puedes tener 4 a la vez en la tienda.");
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
}
