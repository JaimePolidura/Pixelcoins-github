package es.serversurvival.minecraftserver.jugadores.top;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.Posicion;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.PosicionesService;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.TipoBolsaApuesta;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.JugadorEstadisticas;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.JugadorTipoContadorEstadistica;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.JugadoresEstadisticasService;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.Jugador;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.jugadores.patrimonio.CalculadorPatrimonioService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.bukkit.ChatColor.*;

//TODO Remove duplicated code
@RequiredArgsConstructor
public final class TopMenu extends Menu {
    private final JugadoresEstadisticasService estadisticas;
    private final CalculadorPatrimonioService calculadorPatrimonioService;
    private final ActivosBolsaService activosBolsaService;
    private final PosicionesService posicionesService;
    private final JugadoresService jugadoresService;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 2, 0, 0, 3, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 4, 0, 0, 5, 0, 0, 6, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 7, 0, 0, 0, 0, 0, 8, 9}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title( DARK_RED + "" + BOLD + "              TOP")
                .fixedItems()
                .item(1, buildTopRicosJugadoresItem())
                .item(2, buildTopPobresJugadoresItem())
                .item(3, buildTopVendedoresJugadoresItem())
                .item(4, buildTopCompradoresTienda())
                .item(5, buildItemTopOperacionesBolsa())
                .item(6, buildTopMenosFiablesJugadoresItem())
                .item(7, buildItemPeoresOperacioensBolsa())
                .item(8, buildItemTopActivosBolsa())
                .item(9, buildItemGoBackToProfile(), (p,e) -> this.menuService.open(p, PerfilMenu.class))
                .build();
    }

    private ItemStack buildItemGoBackToProfile() {
        return ItemBuilder.of(Material.RED_BANNER).title(RED + "<-").build();
    }

    private ItemStack buildItemTopActivosBolsa() {
        String displayName = GREEN + "" + BOLD + "TOP MAS ACTIVOS BOLSA";
        List<JugadorEstadisticas> topJugadores = estadisticas.sortBy(JugadorTipoContadorEstadistica.BOLSA_N_COMPRA_VENTAS, false, 5);
        List<String> lore = new ArrayList<>();

        for (int i = 0; i < topJugadores.size(); i++) {
            if(i == 5) break;

            JugadorEstadisticas estadistica = topJugadores.get(i);
            String jugadorNombre = jugadoresService.getById(estadistica.getJugadorId()).getNombre();

            lore.add(GOLD + "" + (i + 1) + "º " + jugadorNombre + ": " + GREEN + Funciones.FORMATEA.format(estadistica.getNumeroCompraVentasBolsa()) +
                    " nº operaciones bolsa");
        }

        return ItemBuilder.of(Material.COPPER_INGOT).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemPeoresOperacioensBolsa() {
        String displayName = GREEN + "" + BOLD + "TOP PEORES OPERAIONES BOLSA";
        List<Posicion> allPosicionesCerradas = posicionesService.findPosicionesCerradasSortByRentabilidad();

        List<String> lore = new ArrayList<>();

        for(int i = 0; i < allPosicionesCerradas.size(); i++){
            if(i == 5) break;

            Posicion posicion = allPosicionesCerradas.get(i);
            double rentabilidad = Funciones.redondeoDecimales(posicion.getRentabilidad(), 1);
            String nombreActivo = activosBolsaService.getById(posicion.getActivoBolsaId()).getNombreLargo();
            Jugador jugador = jugadoresService.getById(posicion.getJugadorId());

            if(rentabilidad > 0) break;

            lore.add("" + GOLD + (i + 1)  + "º "+(posicion.getTipoApuesta() == TipoBolsaApuesta.CORTO ? "(CORTO) " : "") + jugador.getNombre() + ": " +
                    nombreActivo + RED + " " + rentabilidad + "%");
        }

        return ItemBuilder.of(Material.COAL_BLOCK).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopMenosFiablesJugadoresItem() {
        List<JugadorEstadisticas> topMorosos = estadisticas.sortBy(JugadorTipoContadorEstadistica.N_DEUDA_INPAGOS, false, 5);

        String displayName = GREEN + "" + BOLD + "TOP MOROSOS";
        List<String> lore = new ArrayList<>();

        for (int i = 0; i < topMorosos.size(); i++) {
            if(i == 5) break;

            JugadorEstadisticas jugadorEstadisticas = topMorosos.get(i);
            Jugador jugador = jugadoresService.getById(jugadorEstadisticas.getJugadorId());

            lore.add(GOLD + "" + i  + "º " + jugador.getNombre() + ": " + GREEN + Funciones.FORMATEA.format(jugadorEstadisticas.getNDeudaInpagos()));
        }

        return ItemBuilder.of(Material.RED_WOOL).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemTopOperacionesBolsa() {
        String displayName = GREEN + "" + BOLD + "TOP MEJORES OPERAIONES BOLSA";

        List<Posicion> allPosicionesCerradas = posicionesService.findPosicionesCerradasSortByRentabilidad();

        List<String> lore = new ArrayList<>();

        for(int i = 0; i < allPosicionesCerradas.size(); i++){
            if(i == 5) break;

            Posicion posicion = allPosicionesCerradas.get(i);
            double rentabilidad = Funciones.redondeoDecimales(posicion.getRentabilidad(), 1);
            String nombreActivo = activosBolsaService.getById(posicion.getActivoBolsaId()).getNombreLargo();
            String jugador = jugadoresService.getById(posicion.getJugadorId()).getNombre();

            if(rentabilidad < 0) break;

            lore.add("" + GOLD + (i + 1)  + "º " + (posicion.getTipoApuesta() == TipoBolsaApuesta.CORTO ? "(CORTO)" : "") + jugador + ": "
                    + posicion + GREEN + " +" + rentabilidad + "% " + nombreActivo);
        }

        return ItemBuilder.of(Material.DIAMOND_BLOCK).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopPobresJugadoresItem() {
        Map<String, Double> listaRicos = calculadorPatrimonioService.calcularTopJugadores(true, 5);
        String displayName = GREEN + "" + BOLD + "TOP POBRES";
        List<String> lore = new ArrayList<>();
        int pos = 1;

        for(Map.Entry<String, Double> entry : listaRicos.entrySet()){
            if(pos == 6) break;
            if(entry.getValue() == 0) continue;

            lore.add(GOLD + "" + pos + "º " + entry.getKey() + ": " + GREEN + Funciones.FORMATEA.format(entry.getValue()) + " PC");
            pos++;
        }

        return ItemBuilder.of(Material.DIRT).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopCompradoresTienda() {
        List<JugadorEstadisticas> listaCompradores = estadisticas.sortBy(JugadorTipoContadorEstadistica.TIENDA_VALOR_COMPRAS, false, 5);
        String displayName = GREEN + "" + BOLD + "TOP COMPRADORES";
        List<String> lore = new ArrayList<>();

        for (int i = 0; i < listaCompradores.size(); i++) {
            JugadorEstadisticas estadisticaJugador = listaCompradores.get(i);
            String nombre = jugadoresService.getById(estadisticaJugador.getJugadorId()).getNombre();

            lore.add(GOLD + "" + (i + 1) + "º " + nombre + ": " + GREEN + Funciones.FORMATEA.format(estadisticaJugador.getValorPixelcoinsComprasTienda()) +
                    " PC " + GOLD + "nº comrpas: " + estadisticaJugador.getNComprasTineda());
        }

        return ItemBuilder.of(Material.COPPER_INGOT).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopVendedoresJugadoresItem() {
        List<JugadorEstadisticas> listaVendedores = estadisticas.sortBy(JugadorTipoContadorEstadistica.TIENDA_VALOR_VENTAS, false, 5);

        String displayName = GREEN + "" + BOLD + "TOP VENDEDORES";
        List<String> lore = new ArrayList<>();

        for (int i = 0; i < listaVendedores.size(); i++) {
            JugadorEstadisticas estadisticaJugador = listaVendedores.get(i);
            String nombre = jugadoresService.getById(estadisticaJugador.getJugadorId()).getNombre();

            lore.add(GOLD + "" + (i + 1) + "º " + nombre + ": " + GREEN + Funciones.FORMATEA.format(estadisticaJugador.getValorPixelcoinsVentasTienda()) +
                    " PC " + GOLD + "nº ventas: " + estadisticaJugador.getNVentasTienda());
        }

        return ItemBuilder.of(Material.GOLD_INGOT).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopRicosJugadoresItem() {
        Map<String, Double> listaRicos = calculadorPatrimonioService.calcularTopJugadores(false, 5);
        String displayName = GREEN + "" + BOLD + "TOP RICOS";
        List<String> lore = new ArrayList<>();
        int pos = 1;

        for(Map.Entry<String, Double> entry : listaRicos.entrySet()){
            if(pos == 6) break;

            lore.add(GOLD + "" + pos + "º " + entry.getKey() + ": " + GREEN + Funciones.FORMATEA.format(entry.getValue()) + " PC");
            pos++;
        }

        return ItemBuilder.of(Material.GOLD_BLOCK).title(displayName).lore(lore).build();
    }
}
