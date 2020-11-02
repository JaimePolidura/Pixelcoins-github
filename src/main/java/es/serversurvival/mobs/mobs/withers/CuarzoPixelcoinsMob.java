package es.serversurvival.mobs.mobs.withers;

import es.serversurvival.mobs.InteractuableMob;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.Transacciones;
import es.serversurvival.util.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class CuarzoPixelcoinsMob extends InteractuableMob {
    @Override
    public void onClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack itemEnMano = player.getInventory().getItemInMainHand();

        if(itemEnMano == null || Funciones.noEsDeTipoItem(itemEnMano, "QUARTZ_BLOCK")){
            player.sendMessage(ChatColor.DARK_RED + "Debes de tener un bloque de cuarzo en la mano para intecambiarlo con pixelcoins");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        MySQL.conectar();
        transaccionesMySQL.ingresarItem(itemEnMano, player, player.getInventory().getHeldItemSlot());
        MySQL.desconectar();
    }

    @Override
    public Location getLocation() {
        return new Location(null, 252, 64, -216);
    }
}
