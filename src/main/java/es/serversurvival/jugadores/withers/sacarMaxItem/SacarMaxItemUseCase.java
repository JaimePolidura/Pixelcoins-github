package es.serversurvival.jugadores.withers.sacarMaxItem;

import es.serversurvival.Pixelcoin;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival.jugadores.withers.CambioPixelcoins;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class SacarMaxItemUseCase implements AllMySQLTablesInstances {
    public static final SacarMaxItemUseCase INSTANCE = new SacarMaxItemUseCase();

    private SacarMaxItemUseCase () {}

    public void sacarMaxItem(String tipo, Jugador jugador) {
        int pixelcoinsJugador = (int) jugadoresMySQL.getJugador(jugador.getNombre()).getPixelcoins();

        CambioPixelcoins.sacarMaxItem(tipo, jugador, jugador.getNombre());
    }

    public void sacarMaxItemDiamond (Jugador jugador, String playerName) {
        int dineroJugador = (int) jugador.getPixelcoins();
        Player player = Bukkit.getPlayer(playerName);

        int convertibles = dineroJugador - (dineroJugador % CambioPixelcoins.DIAMANTE);
        int items = (convertibles / CambioPixelcoins.DIAMANTE) % 9;
        int bloques = ((convertibles / CambioPixelcoins.DIAMANTE) - items) / 9;

        int bloquesAnadidos = 0;
        int[] slotsBloques = Funciones.slotsItem(bloques, 36 - Funciones.getEspaciosOcupados(player.getInventory()));
        Inventory inventoryJugador = player.getInventory();

        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("DIAMOND_BLOCK"), slotsBloques[i]));
        }
        int[] slotsDiamantes = Funciones.slotsItem(items, 36 - Funciones.getEspaciosOcupados(inventoryJugador));
        int diamantesAnadidos = 0;
        for (int i = 0; i < slotsDiamantes.length; i++) {
            diamantesAnadidos = diamantesAnadidos + slotsDiamantes[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("DIAMOND"), slotsDiamantes[i]));
        }

        int coste = (CambioPixelcoins.DIAMANTE * bloquesAnadidos * 9) + (CambioPixelcoins.DIAMANTE * diamantesAnadidos);
        jugadoresMySQL.setPixelcoin(player.getName(), dineroJugador - coste);

        Pixelcoin.publish(new ItemSacadoMaxEvento(jugador, "DIAMOND", coste));
    }

    public void sacarMaxItemLapisLazuli (Jugador jugador, String playerName) {
        int dineroJugador = (int) jugador.getPixelcoins();
        Player player = Bukkit.getPlayer(playerName);

        int convertibles = dineroJugador - (dineroJugador % CambioPixelcoins.LAPISLAZULI);
        int items = (convertibles / CambioPixelcoins.LAPISLAZULI) % 9;
        int bloques = ((convertibles / CambioPixelcoins.LAPISLAZULI) - items) / 9;

        int bloquesAnadidos = 0;
        int[] slotsBloques = Funciones.slotsItem(bloques, 36 - Funciones.getEspaciosOcupados(player.getInventory()));
        Inventory inventoryJugador = player.getInventory();

        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("LAPIS_BLOCK"), slotsBloques[i]));
        }
        int[] slotsDiamantes = Funciones.slotsItem(items, 36 - Funciones.getEspaciosOcupados(inventoryJugador));
        int diamantesAnadidos = 0;
        for (int i = 0; i < slotsDiamantes.length; i++) {
            diamantesAnadidos = diamantesAnadidos + slotsDiamantes[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("LAPIS_LAZULI"), slotsDiamantes[i]));
        }

        int coste = (CambioPixelcoins.LAPISLAZULI * bloquesAnadidos * 9) + (CambioPixelcoins.LAPISLAZULI * diamantesAnadidos);
        jugadoresMySQL.setPixelcoin(player.getName(), dineroJugador - coste);

        Pixelcoin.publish(new ItemSacadoMaxEvento(jugador, "LAPIS_LAZULI", coste));
    }

    public void sacarMaxItemQuartzBlock (Jugador jugador, String playerName) {
        int pixelcoinsJugador = (int) jugador.getPixelcoins();
        Player player = Bukkit.getPlayer(playerName);

        int bloques = (pixelcoinsJugador - (pixelcoinsJugador % CambioPixelcoins.CUARZO)) / CambioPixelcoins.CUARZO;

        int[] slotsBloques = Funciones.slotsItem(bloques, 36 - Funciones.getEspaciosOcupados(player.getInventory()));
        int bloquesAnadidos = 0;
        Inventory jugadorInventory = player.getInventory();
        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            jugadorInventory.addItem(new ItemStack(Material.getMaterial("QUARTZ_BLOCK"), slotsBloques[i]));
        }

        int coste = (CambioPixelcoins.CUARZO * bloquesAnadidos);
        jugadoresMySQL.setPixelcoin(player.getName(), pixelcoinsJugador - coste);

        Pixelcoin.publish(new ItemSacadoMaxEvento(jugador, "QUARTZ_BLOCK", coste));
    }
}
