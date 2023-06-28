package es.serversurvival.minecraftserver.empresas.votaciones;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver.empresas.miempresa.MiEmpresaMenu;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver.empresas.votaciones.lorevotacionitem.VotacionItemLoreBuilderService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.Votacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.application.VotacionesService;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.domain.Voto;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.application.VotosService;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.DARK_RED;

@RequiredArgsConstructor
public final class VerVotacionesEmpresaMenu extends Menu<Empresa> {
    private final VotacionItemLoreBuilderService votacionItemLoreBuilderService;
    private final VotacionesService votacionesService;
    private final JugadoresService jugadoresService;
    private final VotosService votosService;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 0, 0, 0, 0, 0, 0, 0, 0 },
                {2, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 7, 8, 9 }
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .title(DARK_RED + "" + BOLD + "    VOTACIONES " + getState().getNombre())
                .item(1, buildItemInfo())
                .items(2, buildItemVotaciones(), this::abrirVotarMenu)
                .breakpoint(7, MenuItems.GO_BACK, this::irAMiEmpresaMenu)
                .paginated(PaginationConfiguration.builder()
                        .forward(9, Material.GREEN_WOOL)
                        .backward(8, Material.RED_WOOL)
                        .build())
                .build();
    }

    private void abrirVotarMenu(Player player, InventoryClickEvent event) {
        UUID votacionId = MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 0);

        if(!votosService.haVotado(player.getUniqueId(), votacionId)){
            menuService.open(player, VotarMenu.class, votacionesService.getById(votacionId));
        }
    }

    private List<ItemStack> buildItemVotaciones() {
        return votacionesService.findByEmpresaId(getState().getEmpresaId()).stream()
                .sorted(Votacion.sortByPrioridad())
                .map(this::buildItemFromVotacion)
                .collect(Collectors.toList());
    }

    private ItemStack buildItemFromVotacion(Votacion votacion) {
        return ItemBuilder.of(getMaterialFromVotacion(votacion))
                .title(getTituloFromVotacion(votacion))
                .lore(buildLoreForVotacio(votacion))
                .build();
    }

    private String getTituloFromVotacion(Votacion votacion) {
        return switch (votacion.getEstado()) {
            case ACEPTADO -> GREEN + "" + BOLD + "ACEPTADO";
            case RECHAZADO -> RED + "" + BOLD + "RECHARZADO";
            case EMPATE -> YELLOW + "" + BOLD + "EMPTATE";
            case ABIERTA -> {
                Optional<Voto> voto = votosService.findByJugadorIdAndVotacionId(getPlayer().getUniqueId(), votacion.getVotacionId());

                if(voto.isPresent())
                    yield GOLD + "" + BOLD + "HAS VOTADO " + (voto.get().isAfavor() ? "A FAVOR" : "EN CONTRA");
                else
                    yield MenuItems.CLICKEABLE + "VOTAR";
            }
        };
    }

    private Material getMaterialFromVotacion(Votacion votacion) {
        return switch (votacion.getEstado()) {
            case ACEPTADO -> Material.GREEN_BANNER;
            case RECHAZADO -> Material.RED_BANNER;
            case EMPATE -> Material.YELLOW_BANNER;
            case ABIERTA -> Material.BLUE_BANNER;
        };
    }

    private List<String> buildLoreForVotacio(Votacion votacion) {
        List<String> lore = new ArrayList<>(){{
            add(GOLD + "Tipo: " + votacion.getTipo().getNombre());
            add(GOLD + "Estado: " + votacion.getEstado().toString().toLowerCase());
            add(GOLD + "Iniciado por: " + jugadoresService.getNombreById(votacion.getIniciadoPorJugadorId()));
            add(GOLD + "Fecha inicio: " + votacion.getFechaInicio().toString());
            add(GOLD + "Fecha fin: " + (votacion.estaAbierta() ? "N/A" : votacion.getFechaFinalizacion()));
            add(GOLD + "Numero votos a favor: " + GREEN + votosService.getVotosFavor(votacion.getVotacionId()));
            add(GOLD + "Votos en contra: " + RED + votosService.getVotosContra(votacion.getVotacionId()));
            add(GOLD + "% Nº totla acciones votadas para finalizar: " + Votacion.DEFAULT_PORCENTAJE_ACCIONES_TOTALES_VOTACION * 100 + "%");
            add(GOLD + "Nº Total acciones empresa: " + getState().getNTotalAcciones());
            add("  ");
        }};

        lore.addAll(votacionItemLoreBuilderService.buildLore(votacion));

        lore.add(" ");
        lore.add(votacion.getVotacionId().toString());

        return lore;
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER).build();
    }

    private void irAMiEmpresaMenu(Player player, InventoryClickEvent event) {
        menuService.open(player, MiEmpresaMenu.class, getState());
    }
}
