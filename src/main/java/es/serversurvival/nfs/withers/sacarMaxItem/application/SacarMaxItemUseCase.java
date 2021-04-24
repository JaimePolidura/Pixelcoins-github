package es.serversurvival.nfs.withers.sacarMaxItem.application;

import es.serversurvival.legacy.main.Pixelcoin;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.enums.CambioPixelcoins;
import es.serversurvival.legacy.mySQL.eventos.withers.ItemSacadoMaxEvento;
import es.serversurvival.legacy.mySQL.tablasObjetos.Jugador;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static es.serversurvival.legacy.mySQL.enums.CambioPixelcoins.*;
import static es.serversurvival.legacy.mySQL.enums.CambioPixelcoins.CUARZO;
import static es.serversurvival.legacy.util.Funciones.getEspaciosOcupados;
import static es.serversurvival.legacy.util.Funciones.slotsItem;

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

        int convertibles = dineroJugador - (dineroJugador % DIAMANTE);
        int items = (convertibles / DIAMANTE) % 9;
        int bloques = ((convertibles / DIAMANTE) - items) / 9;

        int bloquesAnadidos = 0;
        int[] slotsBloques = slotsItem(bloques, 36 - getEspaciosOcupados(player.getInventory()));
        Inventory inventoryJugador = player.getInventory();

        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("DIAMOND_BLOCK"), slotsBloques[i]));
        }
        int[] slotsDiamantes = slotsItem(items, 36 - getEspaciosOcupados(inventoryJugador));
        int diamantesAnadidos = 0;
        for (int i = 0; i < slotsDiamantes.length; i++) {
            diamantesAnadidos = diamantesAnadidos + slotsDiamantes[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("DIAMOND"), slotsDiamantes[i]));
        }

        int coste = (DIAMANTE * bloquesAnadidos * 9) + (DIAMANTE * diamantesAnadidos);
        jugadoresMySQL.setPixelcoin(player.getName(), dineroJugador - coste);

        Pixelcoin.publish(new ItemSacadoMaxEvento(jugador, "DIAMOND", coste));
    }

    public void sacarMaxItemLapisLazuli (Jugador jugador, String playerName) {
        int dineroJugador = (int) jugador.getPixelcoins();
        Player player = Bukkit.getPlayer(playerName);

        int convertibles = dineroJugador - (dineroJugador % LAPISLAZULI);
        int items = (convertibles / LAPISLAZULI) % 9;
        int bloques = ((convertibles / LAPISLAZULI) - items) / 9;

        int bloquesAnadidos = 0;
        int[] slotsBloques = slotsItem(bloques, 36 - getEspaciosOcupados(player.getInventory()));
        Inventory inventoryJugador = player.getInventory();

        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("LAPIS_BLOCK"), slotsBloques[i]));
        }
        int[] slotsDiamantes = slotsItem(items, 36 - getEspaciosOcupados(inventoryJugador));
        int diamantesAnadidos = 0;
        for (int i = 0; i < slotsDiamantes.length; i++) {
            diamantesAnadidos = diamantesAnadidos + slotsDiamantes[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("LAPIS_LAZULI"), slotsDiamantes[i]));
        }

        int coste = (LAPISLAZULI * bloquesAnadidos * 9) + (LAPISLAZULI * diamantesAnadidos);
        jugadoresMySQL.setPixelcoin(player.getName(), dineroJugador - coste);

        Pixelcoin.publish(new ItemSacadoMaxEvento(jugador, "LAPIS_LAZULI", coste));
    }

    public void sacarMaxItemQuartzBlock (Jugador jugador, String playerName) {
        int pixelcoinsJugador = (int) jugador.getPixelcoins();
        Player player = Bukkit.getPlayer(playerName);

        int bloques = (pixelcoinsJugador - (pixelcoinsJugador % CUARZO)) / CUARZO;

        int[] slotsBloques = slotsItem(bloques, 36 - getEspaciosOcupados(player.getInventory()));
        int bloquesAnadidos = 0;
        Inventory jugadorInventory = player.getInventory();
        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            jugadorInventory.addItem(new ItemStack(Material.getMaterial("QUARTZ_BLOCK"), slotsBloques[i]));
        }

        int coste = (CUARZO * bloquesAnadidos);
        jugadoresMySQL.setPixelcoin(player.getName(), pixelcoinsJugador - coste);

        Pixelcoin.publish(new ItemSacadoMaxEvento(jugador, "QUARTZ_BLOCK", coste));
    }
}
