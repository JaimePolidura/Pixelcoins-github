package es.serversurvival.jugadores.withers.ingresarItem.diamante;

import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import es.serversurvival.jugadores.withers.ingresarItem.IngresarItemUseCase;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.shared.utils.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

@Mob(x = 245, y = 64, z = -216)
public class IngresarDiamantesPixelcoinsMob implements OnPlayerInteractMob, AllMySQLTablesInstances {
    private final IngresarItemUseCase ingresarItemUseCase;

    public IngresarDiamantesPixelcoinsMob() {
        this.ingresarItemUseCase = new IngresarItemUseCase();
    }

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack itemEnMano = player.getInventory().getItemInMainHand();

        if(itemEnMano == null || Funciones.noCuincideNombre(itemEnMano.getType().toString(), "DIAMOND", "DIAMOND_BLOCK")){
            player.sendMessage(ChatColor.DARK_RED + "Debes de tener un diamante en la mano o un bloque de diamante");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        ingresarItemUseCase.ingresarItem(itemEnMano, player);
    }
}
