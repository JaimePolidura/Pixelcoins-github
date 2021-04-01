package es.serversurvival.mobs;

import es.serversurvival.mySQL.*;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public abstract class InteractuableMob implements AllMySQLTablesInstances{

    public abstract void onClick (PlayerInteractEntityEvent event);

    public abstract Location getLocation();
}
