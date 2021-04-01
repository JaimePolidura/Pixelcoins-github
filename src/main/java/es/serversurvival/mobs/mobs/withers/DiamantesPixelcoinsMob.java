package es.serversurvival.mobs.mobs.withers;

import es.serversurvival.mobs.InteractuableMob;
import es.serversurvival.util.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DiamantesPixelcoinsMob extends InteractuableMob {
    @Override
    public void onClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack itemEnMano = player.getInventory().getItemInMainHand();

        if(itemEnMano == null || Funciones.noCuincideNombre(itemEnMano.getType().toString(), "DIAMOND", "DIAMOND_BLOCK")){
            player.sendMessage(ChatColor.DARK_RED + "Debes de tener un diamante en la mano o un bloque de diamante");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        transaccionesMySQL.ingresarItem(itemEnMano, player);
    }

    @Override
    public Location getLocation() {
        return new Location(null, 245, 64, -216);
    }
}
