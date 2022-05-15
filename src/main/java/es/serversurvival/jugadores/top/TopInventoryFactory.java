package es.serversurvival.jugadores.top;

import es.jaimetruman.ItemBuilder;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionescerradas._shared.application.PosicionesCerradasService;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerrada;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival._shared.menus.inventory.InventoryFactory;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival.bolsa.posicionescerradas._shared.application.PosicionesCerradasService.*;

public class TopInventoryFactory extends InventoryFactory {
    private final JugadoresService jugadoresService;
    private final PosicionesCerradasService posicionesCerradasService;

    public TopInventoryFactory(){
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.posicionesCerradasService = DependecyContainer.get(PosicionesCerradasService.class);
    }

    public static final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "              TOP";
    private final List<InfoJugador> infoJugadores = new ArrayList<>();

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, titulo);

        initInfoJugadores();

        inventory.setItem(10, buildTopRicosJugadoresItem());
        inventory.setItem(13, buildTopVendedoresJugadoresItem());
        inventory.setItem(16, buildTopPobresJugadoresItem());
        inventory.setItem(28, buildTopFiablesJugadoresItem());
        inventory.setItem(31, buildItemTopOperacionesBolsa());
        inventory.setItem(34, buildTopMenosFiablesJugadoresItem());
        inventory.setItem(46, buildItemPeoresOperacioensBolsa());
        inventory.setItem(49, buildMejoresComerciantes());

        return inventory;
    }

    private ItemStack buildTopRicosJugadoresItem () {
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

    private ItemStack buildTopPobresJugadoresItem () {
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

    private ItemStack buildTopVendedoresJugadoresItem () {
        List<Jugador> listaVendedores = this.jugadoresService.sortJugadoresBy((j1, j2) -> j2.getNVentas() - j1.getNVentas());

        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP VENDEDORES";
        List<String> lore = new ArrayList<>();

        int pos = 1;
        for(Jugador vendedor : listaVendedores){
            if(pos == 6) break;
            lore.add(ChatColor.GOLD + "" + pos + "º " + vendedor.getNombre() + ": " + ChatColor.GREEN + FORMATEA.format(vendedor.getNVentas()));

            pos++;
        }

        return ItemBuilder.of(Material.GOLD_INGOT).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopFiablesJugadoresItem () {
        List<Jugador> listaFiables = this.jugadoresService.sortJugadoresBy((j1, j2) -> j2.getNPagosDeuda() - j1.getNPagosDeuda());
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MENOS MOROSOS";
        List<String> lore = new ArrayList<>();

        int pos = 1;
        for(Jugador fiabe : listaFiables){
            if(pos == 6) break;
            lore.add(ChatColor.GOLD + "" + pos  + "º " + fiabe.getNombre() + ": " + ChatColor.GREEN + FORMATEA.format(fiabe.getNPagosDeuda()));

            pos++;
        }

        return ItemBuilder.of(Material.GREEN_WOOL).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopMenosFiablesJugadoresItem () {
        List<Jugador> listaMenosFiables = this.jugadoresService.sortJugadoresBy((j1, j2) -> j2.getNInpagosDeuda() - j1.getNInpagosDeuda());
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MOROSOS";
        List<String> lore = new ArrayList<>();

        int pos = 1;
        for(Jugador noFiable : listaMenosFiables){
            if(pos == 6) break;

            lore.add(ChatColor.GOLD + "" + pos  + "º " + noFiable.getNombre() + ": " + ChatColor.GREEN + FORMATEA.format(noFiable.getNInpagosDeuda()));
            pos++;
        }

        return ItemBuilder.of(Material.RED_WOOL).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemTopOperacionesBolsa () {
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MEJORES OPERAIONES BOLSA";

        List<PosicionCerrada> posicionCerradasNotDuplicadas = posicionesCerradasService.findAllByConditionAndSorted(
                pos -> pos.getTipoPosicion() == TipoPosicion.LARGO
        );

        posicionCerradasNotDuplicadas = getNotDuplicatedElements(posicionCerradasNotDuplicadas);
        List<String> lore = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            double rentabilidad = redondeoDecimales(posicionCerradasNotDuplicadas.get(i).calculateRentabildiad(), 3);

            if(rentabilidad > 0){
                if(posicionCerradasNotDuplicadas.get(i).getTipoPosicion() == TipoPosicion.CORTO){
                    lore.add("" + ChatColor.GOLD + (i + 1)  + "º (CORTO) " + posicionCerradasNotDuplicadas.get(i).getJugador() + ": "
                            + posicionCerradasNotDuplicadas.get(i).getNombreActivo() + ChatColor.GREEN + " +" + rentabilidad + "%");
                }else{
                    lore.add("" + ChatColor.GOLD + (i + 1)  + "º " + posicionCerradasNotDuplicadas.get(i).getJugador() + ": "
                            + posicionCerradasNotDuplicadas.get(i).getNombreActivo() + ChatColor.GREEN + " +" + rentabilidad + "%");
                }
            }
        }

        return ItemBuilder.of(Material.DIAMOND_BLOCK).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemPeoresOperacioensBolsa() {
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP PEORES OPERAIONES BOLSA";
        List<PosicionCerrada> posicionCerradasNotDuplicadas = posicionesCerradasService.findAll(SORT_BY_RENTABILIDADES_ASC);
        posicionCerradasNotDuplicadas = getNotDuplicatedElements(posicionCerradasNotDuplicadas);
        List<String> lore = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            double rentabilidad = redondeoDecimales(posicionCerradasNotDuplicadas.get(i).calculateRentabildiad(), 3);

            if(posicionCerradasNotDuplicadas.get(i).getTipoPosicion() == TipoPosicion.CORTO){
                lore.add("" + ChatColor.GOLD + (i + 1)  + "º (CORTO) " + posicionCerradasNotDuplicadas.get(i).getJugador() + ": " +
                        posicionCerradasNotDuplicadas.get(i).getNombreActivo() + ChatColor.RED + " " + rentabilidad + "%");
            }else{
                lore.add("" + ChatColor.GOLD + (i + 1)  + "º " + posicionCerradasNotDuplicadas.get(i).getJugador() + ": " +
                        posicionCerradasNotDuplicadas.get(i).getNombreActivo() + ChatColor.RED + " " + rentabilidad + "%");
            }
        }

        return ItemBuilder.of(Material.COAL_BLOCK).title(displayName).lore(lore).build();
    }

    private ItemStack buildMejoresComerciantes () {
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP COMERCIANTES MAS INTENSIVOS (MENOS MINAN)";
        infoJugadores.sort( (inf1, inf2) -> Double.compare(inf2.porcentajePatrimonioIngresos, inf1.porcentajePatrimonioIngresos) );
        List<String> lore = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            if(infoJugadores.get(i).porcentajePatrimonioIngresos > 0){
                lore.add(ChatColor.GOLD + "" + (i + 1) + " " + infoJugadores.get(i).nombre + ": " + FORMATEA.format(redondeoDecimales(infoJugadores.get(i).porcentajePatrimonioIngresos, 3)) + "%");
            }else{
                lore.add(ChatColor.GOLD + "" + (i + 1) + " " + infoJugadores.get(i).nombre + ": " + ChatColor.GREEN + "0%");
            }
        }
        lore.add(" ");
        lore.add(ChatColor.GOLD + "Este muestra el porcentaje de tu patrimonio");
        lore.add(ChatColor.GOLD + "(acciones,ahorrado,deudas etc) respecto los");
        lore.add(ChatColor.GOLD + "beneficios que has tenido al comerciar");
        lore.add(ChatColor.GOLD + "En otras palabras de cada 100 Pixelcoins que tienes");
        lore.add(ChatColor.GOLD + "Cuantas las has conseguido comerciando");

        return ItemBuilder.of(Material.EMERALD).title(displayName).lore(lore).build();
    }

    private List<PosicionCerrada> getNotDuplicatedElements (List<PosicionCerrada> list) {
        List<PosicionCerrada> toReturn = new ArrayList<>();
        List<PosicionCerrada> listCopy = new ArrayList<>(list);
        
        for (PosicionCerrada posicionBruta : listCopy) {
            if(toReturn.size() == 0){
                toReturn.add(posicionBruta);
            }else{
                boolean encontrado = false;

                for (PosicionCerrada posicionRefinada : toReturn) {
                    if (posicionRefinada.esSimilar(posicionBruta)) {
                        encontrado = true;
                        break;
                    }
                }

                if(!encontrado){
                    toReturn.add(posicionBruta);
                }
                if(toReturn.size() == 5){
                    break;
                }
            }
        }

        return new ArrayList<>(toReturn).subList(0, 5);
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
