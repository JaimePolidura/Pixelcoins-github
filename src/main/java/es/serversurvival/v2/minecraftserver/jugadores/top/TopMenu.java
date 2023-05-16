package es.serversurvival.v2.minecraftserver.jugadores.top;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.BeforeShow;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v1.bolsa.posicionescerradas._shared.application.PosicionesCerradasService;
import es.serversurvival.v1.bolsa.posicionescerradas._shared.domain.PosicionCerrada;
import es.serversurvival.v1.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival.v1.jugadores._shared.application.CalculadorPatrimonio;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import es.serversurvival.v1.jugadores.perfil.PerfilMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static es.serversurvival.v1._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class TopMenu extends Menu<Object> implements BeforeShow {
    private final PosicionesCerradasService posicionesCerradasService;
    private final CalculadorPatrimonio calculadorPatrimonio;
    private final JugadoresService jugadoresService;
    private final MenuService menuService;

    private List<InfoJugador> infoJugadores = new ArrayList<>();

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
                .item(4, buildTopFiablesJugadoresItem())
                .item(5, buildItemTopOperacionesBolsa())
                .item(6, buildTopMenosFiablesJugadoresItem())
                .item(7, buildItemPeoresOperacioensBolsa())
                .item(8, buildMejoresComerciantes())
                .item(9, buildItemGoBackToProfile(), (p,e) -> this.menuService.open(p, PerfilMenu.class, jugadoresService.getByNombre(p.getName())))
                .build();
    }

    @Override
    public void beforeShow(Player player) {
        this.initInfoJugadores();
    }

    private ItemStack buildItemGoBackToProfile() {
        return ItemBuilder.of(Material.RED_BANNER)
                .title(RED + "<-")
                .build();
    }

    private ItemStack buildMejoresComerciantes() {
        String displayName = GREEN + "" + BOLD + "TOP COMERCIANTES MAS INTENSIVOS (MENOS MINAN)";
        infoJugadores.sort( (inf1, inf2) -> Double.compare(inf2.porcentajePatrimonioIngresos, inf1.porcentajePatrimonioIngresos) );
        List<String> lore = new ArrayList<>();

        for (int i = 0; i < infoJugadores.size(); i++) {
            if(i == 5) break;

            lore.add(GOLD + "" + (i + 1) + " " + infoJugadores.get(i).nombre + ": " + FORMATEA.format(redondeoDecimales(infoJugadores.get(i)
                    .porcentajePatrimonioIngresos, 3)) + "%");
        }
        lore.add(" ");
        lore.add(GOLD + "Este muestra el porcentaje de tu patrimonio");
        lore.add(GOLD + "(cantidad,ahorrado,deudas etc) respecto los");
        lore.add(GOLD + "beneficios que has tenido al comerciar");
        lore.add(GOLD + "En otras palabras de cada 100 Pixelcoins que tienes");
        lore.add(GOLD + "Cuantas las has conseguido comerciando");

        return ItemBuilder.of(Material.EMERALD).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemPeoresOperacioensBolsa() {
        String displayName = GREEN + "" + BOLD + "TOP PEORES OPERAIONES BOLSA";
        List<PosicionCerrada> posicionesNotDuplicadas = getPosicionesCerradasNotDuplicated(posicionesCerradasService.findAll(PosicionesCerradasService.SORT_BY_RENTABILIDADES_ASC));

        List<String> lore = new ArrayList<>();

        for(int i = 0; i < posicionesNotDuplicadas.size(); i++){
            if(i == 5) break;

            PosicionCerrada posicion = posicionesNotDuplicadas.get(i);
            double rentabilidad = redondeoDecimales(posicion.calculateRentabildiad(), 3);
            if(rentabilidad > 0) break;

            lore.add("" + GOLD + (i + 1)  + "º "+(posicion.getTipoPosicion() == TipoPosicion.CORTO ? "(CORTO) " : "") + posicion.getJugador() + ": " +
                    posicion.getNombreActivo() + RED + " " + rentabilidad + "%");
        }

        return ItemBuilder.of(Material.COAL_BLOCK).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopMenosFiablesJugadoresItem() {
        List<Jugador> listaMenosFiables = this.jugadoresService.sortJugadoresBy((j1, j2) -> j2.getNinpagosDeuda() - j1.getNinpagosDeuda());
        String displayName = GREEN + "" + BOLD + "TOP MOROSOS";
        List<String> lore = new ArrayList<>();

        for (int i = 0; i < listaMenosFiables.size(); i++) {
            if(i == 5) break;

            Jugador jugador = listaMenosFiables.get(i);

            lore.add(GOLD + "" + i  + "º " + jugador.getNombre() + ": " + GREEN + FORMATEA.format(jugador.getNinpagosDeuda()));
        }

        return ItemBuilder.of(Material.RED_WOOL).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemTopOperacionesBolsa() {
        String displayName = GREEN + "" + BOLD + "TOP MEJORES OPERAIONES BOLSA";

        List<PosicionCerrada> allPosicionesCerradas = posicionesCerradasService.findAll(PosicionesCerradasService.SORT_BY_RENTABILIDADES_DESC);
        List<PosicionCerrada> posicionesnotduplicadas = getPosicionesCerradasNotDuplicated(allPosicionesCerradas);

        List<String> lore = new ArrayList<>();

        for(int i = 0; i < posicionesnotduplicadas.size(); i++){
            if(i == 5) break;

            PosicionCerrada posicion = posicionesnotduplicadas.get(i);
            double rentabilidad = redondeoDecimales(posicion.calculateRentabildiad(), 3);
            if(rentabilidad < 0) break;

            lore.add("" + GOLD + (i + 1)  + "º " + (posicion.getTipoPosicion() == TipoPosicion.CORTO ? "(corto)" : "") + posicion.getJugador() + ": "
                    + posicion + GREEN + " +" + rentabilidad + "%");
        }

        return ItemBuilder.of(Material.DIAMOND_BLOCK).title(displayName).lore(lore).build();
    }

    private List<PosicionCerrada> getPosicionesCerradasNotDuplicated(List<PosicionCerrada> allPosicionesCerradas) {
        return allPosicionesCerradas.stream()
                .filter(posicionCerrada -> allPosicionesCerradas.stream()
                        .anyMatch(posicion -> posicion.esSimilar(posicionCerrada)))
                .limit(5)
                .toList();
    }

    private ItemStack buildTopFiablesJugadoresItem() {
        List<Jugador> listaFiables = this.jugadoresService.sortJugadoresBy((j1, j2) -> j2.getNpagosDeuda() - j1.getNpagosDeuda());
        String displayName = GREEN + "" + BOLD + "TOP MENOS MOROSOS";
        List<String> lore = new ArrayList<>();

        for (int i = 0; i < listaFiables.size(); i++) {
            if(i == 5) break;

            Jugador fiable = listaFiables.get(i);
            lore.add(GOLD + "" + i  + "º " + fiable.getNombre() + ": " + GREEN + FORMATEA.format(fiable.getNpagosDeuda()));
        }

        return ItemBuilder.of(Material.GREEN_WOOL).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopPobresJugadoresItem() {
        Map<String, Double> listaRicos = calculadorPatrimonio.calcularTopJugadores(true, 5);
        String displayName = GREEN + "" + BOLD + "TOP POBRES";
        List<String> lore = new ArrayList<>();
        int pos = 1;

        for(Map.Entry<String, Double> entry : listaRicos.entrySet()){
            if(pos == 6) break;
            if(entry.getValue() == 0) continue;

            lore.add(GOLD + "" + pos + "º " + entry.getKey() + ": " + GREEN + FORMATEA.format(entry.getValue()) + " PC");
            pos++;
        }

        return ItemBuilder.of(Material.DIRT).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopVendedoresJugadoresItem() {
        List<Jugador> listaVendedores = this.jugadoresService.sortJugadoresBy((j1, j2) -> j2.getNventas() - j1.getNventas());

        String displayName = GREEN + "" + BOLD + "TOP VENDEDORES";
        List<String> lore = new ArrayList<>();

        for (int i = 0; i < listaVendedores.size(); i++) {
            if(i == 5) break;

            Jugador vendedor = listaVendedores.get(i);
            lore.add(GOLD + "" + i + "º " + vendedor.getNombre() + ": " + GREEN + FORMATEA.format(vendedor.getNventas()));
        }

        return ItemBuilder.of(Material.GOLD_INGOT).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopRicosJugadoresItem() {
        Map<String, Double> listaRicos = calculadorPatrimonio.calcularTopJugadores(false, 5);
        String displayName = GREEN + "" + BOLD + "TOP RICOS";
        List<String> lore = new ArrayList<>();
        int pos = 1;

        for(Map.Entry<String, Double> entry : listaRicos.entrySet()){
            if(pos == 6) break;

            lore.add(GOLD + "" + pos + "º " + entry.getKey() + ": " + GREEN + FORMATEA.format(entry.getValue()) + " PC");
            pos++;
        }

        return ItemBuilder.of(Material.GOLD_BLOCK).title(displayName).lore(lore).build();
    }

    private void initInfoJugadores () {
        List<Jugador> jugadores = jugadoresService.findAll();
        Map<String, Double> mapPatrimonio = calculadorPatrimonio.calcularTopJugadores(false, 5);

        for (Jugador jugador : jugadores) {
            String nombreJugador = jugador.getNombre();
            double patrimonio = mapPatrimonio.get(nombreJugador);

            infoJugadores.add(new InfoJugador(nombreJugador, jugador, patrimonio));
        }
    }

    private static class InfoJugador {
        String nombre;
        Jugador jugador;
        double patrimonio;
        double porcentajePatrimonioIngresos;

        public InfoJugador (String nombre, Jugador jugador, double patrimonio) {
            this.jugador = jugador;
            this.nombre = nombre;
            this.patrimonio = patrimonio;
            this.porcentajePatrimonioIngresos = rentabilidad(patrimonio, jugador.getIngresos() - jugador.getGastos());
        }
    }
}
