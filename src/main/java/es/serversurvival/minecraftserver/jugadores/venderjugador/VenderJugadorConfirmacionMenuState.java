package es.serversurvival.minecraftserver.jugadores.venderjugador;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public record VenderJugadorConfirmacionMenuState(
        Player jugadorComprador,
        Player jugadorVendedor,
        ItemStack itemAVender,
        int slotItemVender,
        double precio
) { }
