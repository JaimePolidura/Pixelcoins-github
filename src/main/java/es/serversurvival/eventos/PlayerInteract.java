package es.serversurvival.eventos;

import es.serversurvival.mobs.InteractuableMob;
import es.serversurvival.mobs.InteractuableMobManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public final class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract (PlayerInteractEntityEvent event){
        EquipmentSlot tipoClick = event.getHand();

        if(tipoClick.equals(EquipmentSlot.HAND)){
            Location locationDeEntityClickeada = event.getRightClicked().getLocation();
            locationDeEntityClickeada = getFormattedLocation(locationDeEntityClickeada);

            InteractuableMob interactuableMobOfLocation = InteractuableMobManager.getInterectuableMob(locationDeEntityClickeada);

            if(interactuableMobOfLocation != null){
                interactuableMobOfLocation.onClick(event);
            }
        }
    }

    private Location getFormattedLocation(Location location) {
        return new Location(null, (int) location.getX(), (int) location.getY(), (int) location.getZ());
    }
}
