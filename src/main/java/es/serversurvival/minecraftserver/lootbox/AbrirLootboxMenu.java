package es.serversurvival.minecraftserver.lootbox;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.AfterShow;
import es.bukkitbettermenus.menustate.BeforeShow;
import es.bukkitbettermenus.modules.timers.MenuTimer;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.lootbox._shared.items.application.LootboxItemsService;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxItem;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxTier;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.application.LootboxEnPropiedadService;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.domain.LootboxEnPropiedad;
import es.serversurvival.pixelcoins.lootbox.abrir.AbrirLootboxParametros;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.function.BiConsumer;

import static es.bukkitbettermenus.modules.timers.MenuTimer.createTimer;
import static es.bukkitbettermenus.modules.timers.TimerExecutionType.*;

@RequiredArgsConstructor
public final class AbrirLootboxMenu extends Menu<UUID> implements BeforeShow, AfterShow {
    private final LootboxEnPropiedadService lootboxEnPropiedadService;
    private final LootboxItemsService lootboxItemsService;
    private final UseCaseBus useCaseBus;

    private List<Integer> turnoTimerNItemsAMostrar = List.of(15, 4,  2,  1);
    private List<Integer> turnoTimerTicks =          List.of(3,  7, 11, 21);

    private Map<Integer, Integer> numeroItemsMostradosPorTurnoTimer = new HashMap<>();
    private List<ItemStack> itemsAMostrar = new ArrayList<>();
    private int numeroItemsMostradosTotalesIndex = 0;
    private volatile int turnoTimerId = 0;
    private ItemStack itemLootbox;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 1, 1, 1, 2, 1, 1, 1, 1 },
                {4, 3, 3, 3, 3, 3, 3, 3, 3 },
                {1, 1, 1, 1, 2, 1, 1, 1, 1 }
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(MenuItems.TITULO_MENU + "        Abrir lootbox")
                .fixedItems()
                .item(1, Material.BLACK_STAINED_GLASS_PANE)
                .item(2, Material.RED_STAINED_GLASS_PANE)
                .timers(crearTimers())
                .onClose(this::onClose)
                .build();
    }

    private void onClose(InventoryCloseEvent inventoryCloseEvent) {
        boolean seHanAcabadoLosTurnos = turnoTimerId == Integer.MAX_VALUE;

        if(!seHanAcabadoLosTurnos){
            darLootboxItemAlJugador();
        }
    }

    private BiConsumer<BukkitRunnable, Integer> onTickMenuTimer(int timerId) {
        return (bukkitRunnable, acc) -> onTick(bukkitRunnable, timerId);
    }

    private void onTick(BukkitRunnable self, int selfTimerId) {
        if(turnoTimerId > selfTimerId){
            self.cancel();
            return;
        }
        if(turnoTimerId < selfTimerId) {
            return;
        }

        int nItemsAMostrar = turnoTimerNItemsAMostrar.get(selfTimerId);
        int nItemsMostrados = getNItemMostradorTimerTurnoId(selfTimerId);
        boolean ultimoTurno = selfTimerId + 1 >= turnoTimerTicks.size();

        if(nItemsAMostrar == nItemsMostrados && !ultimoTurno){
            turnoTimerId++;
            return;
        }

        moverItemsIzquierda();

        numeroItemsMostradosPorTurnoTimer.put(selfTimerId, nItemsMostrados + 1);
        numeroItemsMostradosTotalesIndex++;
        playSoundItemMovido();

        if(nItemsMostrados == nItemsAMostrar && ultimoTurno){
            turnoTimerId = Integer.MAX_VALUE;

            playSoundFinal();
            darLootboxItemAlJugador();
        }
    }

    private void darLootboxItemAlJugador() {
        MinecraftUtils.darItem(getPlayer(), itemLootbox);
    }

    private void moverItemsIzquierda() {
        int slotInicial = super.getActualSlotByItemNum(4);
        int nColumns = getNColumns();

        for (int i = 0; i < nColumns; i++) {
            int slotDestinoItem = slotInicial + i;
            ItemStack itemAMover = getItemMoverIzquierda(slotDestinoItem, i);
            super.setActualItem(slotDestinoItem, itemAMover, i == 0 ? 4 : 3);
        }
    }

    private ItemStack getItemMoverIzquierda(int slotDestinoItem, int i) {
        int slotOrigenItem = slotDestinoItem + 1;

        return slotOrigenItem < getNColumns() ?
                super.getInventory().getItem(slotOrigenItem) :
                itemsAMostrar.get(numeroItemsMostradosTotalesIndex - getNColumns() + i);
    }

    private int getNItemMostradorTimerTurnoId(int timerId) {
        numeroItemsMostradosPorTurnoTimer.putIfAbsent(timerId, 0);

        return numeroItemsMostradosPorTurnoTimer.get(timerId);
    }

    private List<MenuTimer> crearTimers() {
        List<MenuTimer> timers = new ArrayList<>(turnoTimerTicks.size());

        for (int timerId = 0; timerId < turnoTimerTicks.size(); timerId++) {
            timers.add(createTimer(RUN_PERIODICALLY, turnoTimerTicks.get(timerId), onTickMenuTimer(timerId)));
        }

        return timers;
    }

    @Override
    public void beforeShow(Player player) {
        useCaseBus.handle(AbrirLootboxParametros.builder()
                .jugadorId(player.getUniqueId())
                .lootboxEnPropiedadId(getState())
                .build());

        LootboxEnPropiedad lootboxEnPropiedad = lootboxEnPropiedadService.getById(getState());
        LootboxTier tier = lootboxEnPropiedad.getTier();
        ItemStack lootboxItemAbierto = getItemAbierto(lootboxEnPropiedad);

        int nItemsTotalesAMostrar = turnoTimerNItemsAMostrar.stream()
                .mapToInt(a -> a)
                .sum();

        this.itemLootbox = lootboxItemAbierto;

        itemsAMostrar.addAll(lootboxItemsService.findByTierLimitSorByRandom(tier, nItemsTotalesAMostrar - 1 + getNColumns() - 4).stream()
                .map(LootboxItem::toItemStack)
                .toList());
        itemsAMostrar.add(lootboxItemAbierto);
        itemsAMostrar.addAll(lootboxItemsService.findByTierLimitSorByRandom(tier, 4).stream()
                .map(LootboxItem::toItemStack)
                .toList());
    }

    private ItemStack getItemAbierto(LootboxEnPropiedad lootboxEnPropiedad) {
        ItemStack lootboxItemAbierto = lootboxItemsService.getById(lootboxEnPropiedad.getLootboxItemAbierto())
                .toItemStack();
        lootboxItemAbierto.setAmount(lootboxEnPropiedad.getCantidadResultado());
        return lootboxItemAbierto;
    }

    @Override
    public void afterShow(Player player) {
        int slotInicial = super.getActualSlotByItemNum(4);

        for (int i = 0; i < getNColumns(); i++) {
            super.setActualItem(slotInicial + i, itemsAMostrar.get(numeroItemsMostradosTotalesIndex), i == 0 ? 4 : 3);
            numeroItemsMostradosTotalesIndex++;
        }
    }

    private int getNColumns() {
        return super.getBaseItemNums()[0].length;
    }

    private void playSoundItemMovido() {
        getPlayer().playSound(getPlayer(), Sound.UI_BUTTON_CLICK, 10, 1);
    }

    private void playSoundFinal() {
        getPlayer().playSound(getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        getPlayer().playSound(getPlayer(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 10, 1);
    }
}
