package es.serversurvival.menus.inventoryFactory.inventories;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import es.serversurvival.mySQL.tablasObjetos.PosicionCerrada;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static es.serversurvival.mySQL.enums.TipoPosicion.*;

public class TopInventoryFactory extends InventoryFactory {
    public static final java.lang.String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "              TOP";
    private List<InfoJugador> infoJugadores = new ArrayList<>();

    @Override
    protected Inventory buildInventory(java.lang.String jugador) {
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
        Map<java.lang.String, Double> listaRicos = Funciones.crearMapaTopPatrimonioPlayers(false);
        java.lang.String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP RICOS";

        List<java.lang.String> lore = new ArrayList<>();
        int pos = 1;
        for(Map.Entry<java.lang.String, Double> entry : listaRicos.entrySet()){
            if(pos == 6) break;

            lore.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + formatea.format(entry.getValue()) + " PC");
            pos++;
        }

        return ItemBuilder.of(Material.GOLD_BLOCK).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopPobresJugadoresItem () {
        Map<java.lang.String, Double> listaRicos = Funciones.crearMapaTopPatrimonioPlayers(true);

        java.lang.String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP POBRES";

        List<java.lang.String> lore = new ArrayList<>();
        int pos = 1;
        for(Map.Entry<java.lang.String, Double> entry : listaRicos.entrySet()){
            if(pos == 6) break;
            if(entry.getValue() == 0) continue;

            lore.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + formatea.format(entry.getValue()) + " PC");
            pos++;
        }

        return ItemBuilder.of(Material.DIRT).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopVendedoresJugadoresItem () {
        List<Jugador> listaVendedores = jugadoresMySQL.getTopVendedores();
        java.lang.String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP VENDEDORES";
        List<java.lang.String> lore = new ArrayList<>();

        int pos = 1;
        for(Jugador vendedor : listaVendedores){
            if(pos == 6) break;
            lore.add(ChatColor.GOLD + "" + pos + "º " + vendedor.getNombre() + ": " + ChatColor.GREEN + formatea.format(vendedor.getNventas()));

            pos++;
        }

        return ItemBuilder.of(Material.GOLD_INGOT).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopFiablesJugadoresItem () {
        List<Jugador> listaFiables = jugadoresMySQL.getTopFiables();
        java.lang.String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MENOS MOROSOS";
        List<java.lang.String> lore = new ArrayList<>();

        int pos = 1;
        for(Jugador fiabe : listaFiables){
            if(pos == 6) break;
            lore.add(ChatColor.GOLD + "" + pos  + "º " + fiabe.getNombre() + ": " + ChatColor.GREEN + formatea.format(fiabe.getNpagos()));

            pos++;
        }

        return ItemBuilder.of(Material.GREEN_WOOL).title(displayName).lore(lore).build();
    }

    private ItemStack buildTopMenosFiablesJugadoresItem () {
        List<Jugador> listaMenosFiables = jugadoresMySQL.getTopMenosFiables();
        java.lang.String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MOROSOS";
        List<java.lang.String> lore = new ArrayList<>();

        int pos = 1;
        for(Jugador noFiable : listaMenosFiables){
            if(pos == 6) break;

            lore.add(ChatColor.GOLD + "" + pos  + "º " + noFiable.getNombre() + ": " + ChatColor.GREEN + formatea.format(noFiable.getNpagos()));
            pos++;
        }

        return ItemBuilder.of(Material.RED_WOOL).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemTopOperacionesBolsa () {
        java.lang.String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MEJORES OPERAIONES BOLSA";
        List<PosicionCerrada> posicionCerradasNotDuplicadas = posicionesCerradasMySQL.getTopRentabilidades();
        posicionCerradasNotDuplicadas = getNotDuplicatedElements(posicionCerradasNotDuplicadas);
        List<java.lang.String> lore = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            double rentabilidad = Funciones.redondeoDecimales(posicionCerradasNotDuplicadas.get(i).getRentabilidad(), 3);

            if(rentabilidad > 0){
                if(posicionCerradasNotDuplicadas.get(i).getTipo_posicion() == TipoPosicion.CORTO){
                    lore.add("" + ChatColor.GOLD + (i + 1)  + "º (CORTO) " + posicionCerradasNotDuplicadas.get(i).getJugador() + ": " + posicionCerradasNotDuplicadas.get(i).getSimbolo() + ChatColor.GREEN + " +" + rentabilidad + "%");
                }else{
                    lore.add("" + ChatColor.GOLD + (i + 1)  + "º " + posicionCerradasNotDuplicadas.get(i).getJugador() + ": " + posicionCerradasNotDuplicadas.get(i).getSimbolo() + ChatColor.GREEN + " +" + rentabilidad + "%");
                }
            }
        }

        return ItemBuilder.of(Material.DIAMOND_BLOCK).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemPeoresOperacioensBolsa() {
        java.lang.String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP PEORES OPERAIONES BOLSA";
        List<PosicionCerrada> posicionCerradasNotDuplicadas = posicionesCerradasMySQL.getPeoresRentabilidades();
        posicionCerradasNotDuplicadas = getNotDuplicatedElements(posicionCerradasNotDuplicadas);
        List<java.lang.String> lore = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            double rentabilidad = Funciones.redondeoDecimales(posicionCerradasNotDuplicadas.get(i).getRentabilidad(), 3);

            if(posicionCerradasNotDuplicadas.get(i).getTipo_posicion() == CORTO){
                lore.add("" + ChatColor.GOLD + (i + 1)  + "º (CORTO) " + posicionCerradasNotDuplicadas.get(i).getJugador() + ": " + posicionCerradasNotDuplicadas.get(i).getSimbolo() + ChatColor.RED + " " + rentabilidad + "%");
            }else{
                lore.add("" + ChatColor.GOLD + (i + 1)  + "º " + posicionCerradasNotDuplicadas.get(i).getJugador() + ": " + posicionCerradasNotDuplicadas.get(i).getSimbolo() + ChatColor.RED + " " + rentabilidad + "%");
            }
        }

        return ItemBuilder.of(Material.COAL_BLOCK).title(displayName).lore(lore).build();
    }

    private ItemStack buildMejoresComerciantes () {
        java.lang.String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "TOP COMERCIANTES MAS INTENSIVOS (MENOS MINAN)";
        infoJugadores.sort( (inf1, inf2) -> Double.compare(inf2.porcentajePatrimonioIngresos, inf1.porcentajePatrimonioIngresos) );
        List<java.lang.String> lore = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            if(infoJugadores.get(i).porcentajePatrimonioIngresos > 0){
                lore.add(ChatColor.GOLD + "" + (i + 1) + " " + infoJugadores.get(i).nombre + ": " + formatea.format(Funciones.redondeoDecimales(infoJugadores.get(i).porcentajePatrimonioIngresos, 3)) + "%");
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
        List<Jugador> jugadores = jugadoresMySQL.getAllJugadores();
        Map<java.lang.String, Double> mapPatrimonio = Funciones.crearMapaTopPatrimonioPlayers(false);

        for (Jugador jugador : jugadores) {
            java.lang.String nombreJugador = jugador.getNombre();
            double patrimonio = mapPatrimonio.get(nombreJugador);

            infoJugadores.add(new InfoJugador(nombreJugador, jugador, patrimonio));
        }
    }

    private static class InfoJugador {
        java.lang.String nombre;
        Jugador jugador;
        double patrimonio;
        double porcentajePatrimonioIngresos;

        public InfoJugador (java.lang.String nombre, Jugador jugador, double patrimonio) {
            this.jugador = jugador;
            this.nombre = nombre;
            this.patrimonio = patrimonio;
            this.porcentajePatrimonioIngresos = Funciones.rentabilidad(patrimonio, jugador.getIngresos() - jugador.getGastos());
        }
    }
}
