package es.serversurvival.jugadores.cambio.sacarMaxItem;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.Pixelcoin;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.jugadores.cambio.TipoCambioPixelcoins;
import es.serversurvival._shared.utils.Funciones;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static es.serversurvival._shared.utils.CollectionUtils.*;

@UseCase
@AllArgsConstructor
public final class SacarMaxItemUseCase {
    private final JugadoresService jugadoresService;

    public void sacarMaxItem(Jugador jugador, TipoCambioPixelcoins tipoCambio) {
        switch (tipoCambio) {
            case DIAMOND, DIAMOND_BLOCK -> sacarMaxItemDiamond(jugador);
            case LAPIS_BLOCK, LAPIS_LAZULI -> sacarMaxItemLapisLazuli(jugador);
            case QUARTZ_BLOCK -> sacarMaxItemQuartzBlock(jugador);
        }
    }

    public void sacarMaxItemDiamond (Jugador jugador) {
        int dineroJugador = (int) jugador.getPixelcoins();
        Player player = Bukkit.getPlayer(jugador.getNombre());

        int convertibles = dineroJugador - (dineroJugador % TipoCambioPixelcoins.DIAMANTE);
        int items = (convertibles / TipoCambioPixelcoins.DIAMANTE) % 9;
        int bloques = ((convertibles / TipoCambioPixelcoins.DIAMANTE) - items) / 9;

        int bloquesAnadidos = 0;
        int[] slotsBloques = slotsItem(bloques, 36 - Funciones.getEspaciosOcupados(player.getInventory()));
        Inventory inventoryJugador = player.getInventory();

        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("DIAMOND_BLOCK"), slotsBloques[i]));
        }
        int[] slotsDiamantes = slotsItem(items, 36 - Funciones.getEspaciosOcupados(inventoryJugador));
        int diamantesAnadidos = 0;
        for (int i = 0; i < slotsDiamantes.length; i++) {
            diamantesAnadidos = diamantesAnadidos + slotsDiamantes[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("DIAMOND"), slotsDiamantes[i]));
        }

        int coste = (TipoCambioPixelcoins.DIAMANTE * bloquesAnadidos * 9) + (TipoCambioPixelcoins.DIAMANTE * diamantesAnadidos);
        this.jugadoresService.save(jugador.decrementPixelcoinsBy(coste));

        Pixelcoin.publish(new ItemSacadoMaxEvento(jugador, "DIAMOND", coste));
    }

    public void sacarMaxItemLapisLazuli (Jugador jugador) {
        int dineroJugador = (int) jugador.getPixelcoins();
        Player player = Bukkit.getPlayer(jugador.getNombre());

        int convertibles = dineroJugador - (dineroJugador % TipoCambioPixelcoins.LAPISLAZULI);
        int items = (convertibles / TipoCambioPixelcoins.LAPISLAZULI) % 9;
        int bloques = ((convertibles / TipoCambioPixelcoins.LAPISLAZULI) - items) / 9;

        int bloquesAnadidos = 0;
        int[] slotsBloques = slotsItem(bloques, 36 - Funciones.getEspaciosOcupados(player.getInventory()));
        Inventory inventoryJugador = player.getInventory();

        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("LAPIS_BLOCK"), slotsBloques[i]));
        }
        int[] slotsDiamantes = slotsItem(items, 36 - Funciones.getEspaciosOcupados(inventoryJugador));
        int diamantesAnadidos = 0;
        for (int i = 0; i < slotsDiamantes.length; i++) {
            diamantesAnadidos = diamantesAnadidos + slotsDiamantes[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("LAPIS_LAZULI"), slotsDiamantes[i]));
        }

        int coste = (TipoCambioPixelcoins.LAPISLAZULI * bloquesAnadidos * 9) + (TipoCambioPixelcoins.LAPISLAZULI * diamantesAnadidos);
        this.jugadoresService.save(jugador.decrementPixelcoinsBy(coste));

        Pixelcoin.publish(new ItemSacadoMaxEvento(jugador, "LAPIS_LAZULI", coste));
    }

    public void sacarMaxItemQuartzBlock (Jugador jugador) {
        int pixelcoinsJugador = (int) jugador.getPixelcoins();
        Player player = Bukkit.getPlayer(jugador.getNombre());

        int bloques = (pixelcoinsJugador - (pixelcoinsJugador % TipoCambioPixelcoins.CUARZO)) / TipoCambioPixelcoins.CUARZO;

        int[] slotsBloques = slotsItem(bloques, 36 - Funciones.getEspaciosOcupados(player.getInventory()));
        int bloquesAnadidos = 0;
        Inventory jugadorInventory = player.getInventory();
        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            jugadorInventory.addItem(new ItemStack(Material.getMaterial("QUARTZ_BLOCK"), slotsBloques[i]));
        }

        int coste = (TipoCambioPixelcoins.CUARZO * bloquesAnadidos);
        this.jugadoresService.save(jugador.decrementPixelcoinsBy(coste));

        Pixelcoin.publish(new ItemSacadoMaxEvento(jugador, "QUARTZ_BLOCK", coste));
    }
}
