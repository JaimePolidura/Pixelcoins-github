package es.serversurvival.jugadores.top;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionescerradas._shared.application.PosicionesCerradasService;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerrada;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.bolsa.posicionescerradas._shared.application.PosicionesCerradasService.SORT_BY_RENTABILIDADES_ASC;
import static es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion.*;

public final class TopMenu extends Menu {
    public static final String TITULO = ChatColor.DARK_RED + "" + ChatColor.BOLD + "              TOP";

    private final List<InfoJugador> infoJugadores = new ArrayList<>();
    private final PosicionesCerradasService posicionesCerradasService;
    private final JugadoresService jugadoresService;

    public TopMenu(){
        this.initInfoJugadores();
        this.posicionesCerradasService = DependecyContainer.get(PosicionesCerradasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    @Override
    public int[][] items() {
        return new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 2, 0, 0, 3, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 4, 0, 0, 5, 0, 0, 6, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 7, 0, 0, 0, 8, 0, 0}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(TITULO)
                .fixedItems()
                .item(1, buildTopRicosJugadoresItem())
                .item(2, buildTopPobresJugadoresItem())
                .item(3, buildTopVendedoresJugadoresItem())
                .item(4, buildTopFiablesJugadoresItem())
                .item(5, buildItemTopOperacionesBolsa())
                .item(6, buildTopMenosFiablesJugadoresItem())
                .item(7, buildItemPeoresOperacioensBolsa())
                .item(8, buildMejoresComerciantes())
                .build();
    }

    private ItemStack buildMejoresComerciantes() {
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP COMERCIANTES MAS INTENSIVOS (MENOS MINAN)";
        infoJugadores.sort( (inf1, inf2) -> Double.compare(inf2.porcentajePatrimonioIngresos, inf1.porcentajePatrimonioIngresos) );
        List<String> lore = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            lore.add(ChatColor.GOLD + "" + (i + 1) + " " + infoJugadores.get(i).nombre + ": " + FORMATEA.format(redondeoDecimales(infoJugadores.get(i)
                    .porcentajePatrimonioIngresos, 3)) + "%");
        }
        lore.add(" ");
        lore.add(ChatColor.GOLD + "Este muestra el porcentaje de tu patrimonio");
        lore.add(ChatColor.GOLD + "(acciones,ahorrado,deudas etc) respecto los");
        lore.add(ChatColor.GOLD + "beneficios que has tenido al comerciar");
        lore.add(ChatColor.GOLD + "En otras palabras de cada 100 Pixelcoins que tienes");
        lore.add(ChatColor.GOLD + "Cuantas las has conseguido comerciando");

        return ItemBuilder.of(Material.EMERALD).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemPeoresOperacioensBolsa() {
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP PEORES OPERAIONES BOLSA";
        List<PosicionCerrada> posicionesNotDuplicadas = getPosicionesCerradasNotDuplicated(posicionesCerradasService.findAll(SORT_BY_RENTABILIDADES_ASC));

        List<String> lore = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            PosicionCerrada posicion = posicionesNotDuplicadas.get(i);
            double rentabilidad = redondeoDecimales(posicion.calculateRentabildiad(), 3);
            if(rentabilidad > 0) break;

            lore.add("" + ChatColor.GOLD + (i + 1)  + "º "+(posicion.getTipoPosicion() == CORTO ? "(CORTO) " : "") + posicion.getJugador() + ": " +
                    posicion.getNombreActivo() + ChatColor.RED + " " + rentabilidad + "%");
        }

        return ItemBuilder.of(Material.COAL_BLOCK).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopMenosFiablesJugadoresItem() {
        List<Jugador> listaMenosFiables = this.jugadoresService.sortJugadoresBy((j1, j2) -> j2.getNInpagosDeuda() - j1.getNInpagosDeuda());
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MOROSOS";
        List<String> lore = new ArrayList<>();

        for (int i = 0; i < 5 || i < listaMenosFiables.size(); i++) {
            Jugador jugador = listaMenosFiables.get(i);

            lore.add(ChatColor.GOLD + "" + i  + "º " + jugador.getNombre() + ": " + ChatColor.GREEN + FORMATEA.format(jugador.getNInpagosDeuda()));
        }

        return ItemBuilder.of(Material.RED_WOOL).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemTopOperacionesBolsa() {
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MEJORES OPERAIONES BOLSA";

        List<PosicionCerrada> allPosicionesCerradas = posicionesCerradasService.findAll(PosicionesCerradasService.SORT_BY_RENTABILIDADES_DESC);
        List<PosicionCerrada> posicionesnotduplicadas = getPosicionesCerradasNotDuplicated(allPosicionesCerradas);

        List<String> lore = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            PosicionCerrada posicion = posicionesnotduplicadas.get(i);
            double rentabilidad = redondeoDecimales(posicion.calculateRentabildiad(), 3);
            if(rentabilidad < 0) break;

            lore.add("" + ChatColor.GOLD + (i + 1)  + "º " + (posicion.getTipoPosicion() == CORTO ? "(corto)" : "") + posicion.getJugador() + ": "
                    + posicion + ChatColor.GREEN + " +" + rentabilidad + "%");
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
        List<Jugador> listaFiables = this.jugadoresService.sortJugadoresBy((j1, j2) -> j2.getNPagosDeuda() - j1.getNPagosDeuda());
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MENOS MOROSOS";
        List<String> lore = new ArrayList<>();

        for (int i = 0; i < listaFiables.size(); i++) {
            Jugador fiable = listaFiables.get(i);
            lore.add(ChatColor.GOLD + "" + i  + "º " + fiable.getNombre() + ": " + ChatColor.GREEN + FORMATEA.format(fiable.getNPagosDeuda()));
        }

        return ItemBuilder.of(Material.GREEN_WOOL).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopPobresJugadoresItem() {
        Map<String, Double> listaRicos = crearMapaTopPatrimonioPlayers(true);
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP POBRES";
        List<String> lore = new ArrayList<>();
        int pos = 1;

        for(Map.Entry<String, Double> entry : listaRicos.entrySet()){
            if(pos == 6) break;
            if(entry.getValue() == 0) continue;

            lore.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + FORMATEA.format(entry.getValue()) + " PC");
            pos++;
        }

        return ItemBuilder.of(Material.DIRT).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopVendedoresJugadoresItem() {
        List<Jugador> listaVendedores = this.jugadoresService.sortJugadoresBy((j1, j2) -> j2.getNVentas() - j1.getNVentas());

        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP VENDEDORES";
        List<String> lore = new ArrayList<>();

        for (int i = 0; i < listaVendedores.size(); i++) {
            Jugador vendedor = listaVendedores.get(i);
            lore.add(ChatColor.GOLD + "" + i + "º " + vendedor.getNombre() + ": " + ChatColor.GREEN + FORMATEA.format(vendedor.getNVentas()));
        }

        return ItemBuilder.of(Material.GOLD_INGOT).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopRicosJugadoresItem() {
        Map<String, Double> listaRicos = crearMapaTopPatrimonioPlayers(false);
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP RICOS";
        List<String> lore = new ArrayList<>();
        int pos = 1;

        for(Map.Entry<String, Double> entry : listaRicos.entrySet()){
            if(pos == 6) break;

            lore.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + FORMATEA.format(entry.getValue()) + " PC");
            pos++;
        }

        return ItemBuilder.of(Material.GOLD_BLOCK).title(displayName).lore(lore).build();
    }

    private void initInfoJugadores () {
        List<Jugador> jugadores = jugadoresService.findAll();
        Map<String, Double> mapPatrimonio = crearMapaTopPatrimonioPlayers(false);

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
