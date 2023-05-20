package es.serversurvival.v2.pixelcoins.jugadores.cambiar.sacarMaxItem;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v2.pixelcoins.jugadores._shared.Jugador;
import es.serversurvival.v2.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@UseCase
@AllArgsConstructor
public final class SacarMaxItemUseCase {
    private final TransaccionesService transaccionesService;
    private final EventBus eventBus;

    public void sacarMaxItem(SacarMaxItemParametros parametros) {
        var pixelcoinsJugador = transaccionesService.getBalancePixelcions(parametros.getJugador().getJugadorId());

        switch (parametros.getTipoCambio()) {
            case DIAMOND, DIAMOND_BLOCK -> sacarMaxItemDiamond(parametros.getJugador(), pixelcoinsJugador);
            case LAPIS_BLOCK, LAPIS_LAZULI -> sacarMaxItemLapisLazuli(parametros.getJugador(), pixelcoinsJugador);
            case QUARTZ_BLOCK -> sacarMaxItemQuartzBlock(parametros.getJugador(), pixelcoinsJugador);
        }
    }

    public void sacarMaxItemDiamond (Jugador jugador, double pixelcoinsJugador) {
        Player player = Bukkit.getPlayer(jugador.getNombre());

        int convertibles = (int) (pixelcoinsJugador - (pixelcoinsJugador % TipoCambioPixelcoins.DIAMANTE));
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

        this.decrementarPixelcoins(jugador, pixelcoinsJugador, TipoCambioPixelcoins.DIAMOND);

        this.eventBus.publish(new ItemSacadoMaxEvento(jugador, "DIAMOND", coste));
    }

    public void sacarMaxItemLapisLazuli (Jugador jugador, double dineroJugador) {
        Player player = Bukkit.getPlayer(jugador.getNombre());

        int convertibles = (int) (dineroJugador - (dineroJugador % TipoCambioPixelcoins.LAPISLAZULI));
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

        decrementarPixelcoins(jugador, coste, TipoCambioPixelcoins.LAPIS_LAZULI);

        this.eventBus.publish(new ItemSacadoMaxEvento(jugador, "LAPIS_LAZULI", coste));
    }

    public void sacarMaxItemQuartzBlock (Jugador jugador, double dineroJugador) {
        Player player = Bukkit.getPlayer(jugador.getNombre());

        int bloques = (int) ((dineroJugador - (dineroJugador % TipoCambioPixelcoins.CUARZO)) / TipoCambioPixelcoins.CUARZO);

        int[] slotsBloques = slotsItem(bloques, 36 - Funciones.getEspaciosOcupados(player.getInventory()));
        int bloquesAnadidos = 0;
        Inventory jugadorInventory = player.getInventory();
        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            jugadorInventory.addItem(new ItemStack(Material.getMaterial("QUARTZ_BLOCK"), slotsBloques[i]));
        }

        int coste = (TipoCambioPixelcoins.CUARZO * bloquesAnadidos);

        decrementarPixelcoins(jugador, coste, TipoCambioPixelcoins.LAPIS_LAZULI);

        this.eventBus.publish(new ItemSacadoMaxEvento(jugador, "QUARTZ_BLOCK", coste));
    }

    private void decrementarPixelcoins(Jugador jugador, double pixelcoinsADecrementar, TipoCambioPixelcoins tipoCambioPixelcoins) {
        this.transaccionesService.save(Transaccion.builder()
                        .tipo(TipoTransaccion.JUGADORES_CAMBIO_SACAR_MAX_ITEM)
                        .objeto(tipoCambioPixelcoins.name())
                        .pagadorId(jugador.getJugadorId())
                        .pixelcoins(pixelcoinsADecrementar)
                .build());
    }

    private int[] slotsItem(int n, int slotsLibres) {
        int[] arr = new int[slotsLibres];

        for (int i = 0; i < slotsLibres; i++) {
            if (n - 64 > 0) {
                arr[i] = 64;
                n = n - 64;
            } else {
                arr[i] = n;
                break;
            }
        }
        return arr;
    }
}
