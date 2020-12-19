package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import es.serversurvival.mySQL.tablasObjetos.PosicionCerrada;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TopInventoryFactory extends InventoryFactory {
    public static final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "              TOP";
    private List<InfoJugador> infoJugadores = new ArrayList<>();

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, titulo);

        MySQL.conectar();
        initInfoJugadores();

        ItemStack itemStackTopRicos = buildTopRicosJugadoresItem();
        ItemStack itemStackTopPobres = buildTopPobresJugadoresItem();
        ItemStack itemStackTopVendedores = buildTopVendedoresJugadoresItem();
        ItemStack itemStackTopFiables = buildTopFiablesJugadoresItem();
        ItemStack itemStackTopMenosFiables = buildTopMenosFiablesJugadoresItem();
        ItemStack itemStackTopOperaciones = buildItemTopOperacionesBolsa();
        ItemStack itemStackPeoresOperaciones = buildItemPeoresOperacioensBolsa();
        ItemStack itemStackMejoresComerciantes = buildMejoresComerciantes();

        MySQL.desconectar();

        inventory.setItem(10, itemStackTopRicos);
        inventory.setItem(13, itemStackTopVendedores);
        inventory.setItem(16, itemStackTopPobres);
        inventory.setItem(28, itemStackTopFiables);
        inventory.setItem(31, itemStackTopOperaciones);
        inventory.setItem(34, itemStackTopMenosFiables);
        inventory.setItem(46, itemStackPeoresOperaciones);
        inventory.setItem(49, itemStackMejoresComerciantes);

        return inventory;
    }

    private ItemStack buildTopRicosJugadoresItem () {
        MySQL.conectar();
        Map<String, Double> listaRicos = Funciones.crearMapaTopPatrimonioPlayers(false);

        ItemStack topRicos = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta topRicosItemMeta = topRicos.getItemMeta();

        topRicosItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP RICOS");

        List<String> lore = new ArrayList<>();
        int pos = 1;
        for(Map.Entry<String, Double> entry : listaRicos.entrySet()){
            if(pos == 6) break;

            lore.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + formatea.format(entry.getValue()) + " PC");
            pos++;
        }
        topRicosItemMeta.setLore(lore);
        topRicos.setItemMeta(topRicosItemMeta);

        return topRicos;
    }

    private ItemStack buildTopPobresJugadoresItem () {
        MySQL.conectar();
        Map<String, Double> listaRicos = Funciones.crearMapaTopPatrimonioPlayers(true);

        ItemStack topPobres = new ItemStack(Material.DIRT);
        ItemMeta topPobresItemMeta = topPobres.getItemMeta();

        topPobresItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP POBRES");

        List<String> lore = new ArrayList<>();
        int pos = 1;
        for(Map.Entry<String, Double> entry : listaRicos.entrySet()){
            if(pos == 6) break;
            if(entry.getValue() == 0) continue;

            lore.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + formatea.format(entry.getValue()) + " PC");
            pos++;
        }
        topPobresItemMeta.setLore(lore);
        topPobres.setItemMeta(topPobresItemMeta);

        return topPobres;
    }

    private ItemStack buildTopVendedoresJugadoresItem () {
        ItemStack topVendedores = new ItemStack(Material.GOLD_INGOT);
        ItemMeta topVendedoresItemMeta = topVendedores.getItemMeta();
        List<Jugador> listaVendedores = jugadoresMySQL.getTopVendedores();

        topVendedoresItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP VENDEDORES");
        List<String> lore = new ArrayList<>();

        int pos = 1;
        for(Jugador vendedor : listaVendedores){
            if(pos == 6) break;
            lore.add(ChatColor.GOLD + "" + pos + "º " + vendedor.getNombre() + ": " + ChatColor.GREEN + formatea.format(vendedor.getNventas()));

            pos++;
        }

        topVendedoresItemMeta.setLore(lore);
        topVendedores.setItemMeta(topVendedoresItemMeta);

        return topVendedores;
    }

    private ItemStack buildTopFiablesJugadoresItem () {
        ItemStack topFiables = new ItemStack(Material.GREEN_WOOL);
        ItemMeta topFiablesItemMeta = topFiables.getItemMeta();
        List<Jugador> listaFiables = jugadoresMySQL.getTopFiables();

        topFiablesItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MENOS MOROSOS");
        List<String> lore = new ArrayList<>();

        int pos = 1;
        for(Jugador fiabe : listaFiables){
            if(pos == 6) break;
            lore.add(ChatColor.GOLD + "" + pos  + "º " + fiabe.getNombre() + ": " + ChatColor.GREEN + formatea.format(fiabe.getNpagos()));

            pos++;
        }

        topFiablesItemMeta.setLore(lore);
        topFiables.setItemMeta(topFiablesItemMeta);

        return topFiables;
    }

    private ItemStack buildTopMenosFiablesJugadoresItem () {
        ItemStack topMenosFiables = new ItemStack(Material.RED_WOOL);
        ItemMeta topMenosFiablesItemMeta = topMenosFiables.getItemMeta();
        List<Jugador> listaMenosFiables = jugadoresMySQL.getTopMenosFiables();

        topMenosFiablesItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MOROSOS");
        List<String> lore = new ArrayList<>();

        int pos = 1;
        for(Jugador noFiable : listaMenosFiables){
            if(pos == 6) break;

            lore.add(ChatColor.GOLD + "" + pos  + "º " + noFiable.getNombre() + ": " + ChatColor.GREEN + formatea.format(noFiable.getNpagos()));
            pos++;
        }
        topMenosFiablesItemMeta.setLore(lore);
        topMenosFiables.setItemMeta(topMenosFiablesItemMeta);

        return topMenosFiables;
    }

    private ItemStack buildItemTopOperacionesBolsa () {
        ItemStack topOperaciones = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta topOperacionesItemMeta = topOperaciones.getItemMeta();
        topOperacionesItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MEJORES OPERAIONES BOLSA");

        List<PosicionCerrada> posicionCerradasNotDuplicadas = posicionesCerradasMySQL.getTopRentabilidades();
        posicionCerradasNotDuplicadas = getNotDuplicatedElements(posicionCerradasNotDuplicadas);
        List<String> lore6 = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            double rentabilidad = Funciones.redondeoDecimales(posicionCerradasNotDuplicadas.get(i).getRentabilidad(), 3);

            if(rentabilidad > 0){
                if(posicionCerradasNotDuplicadas.get(i).getTipo_posicion().equalsIgnoreCase("CORTO")){
                    lore6.add("" + ChatColor.GOLD + (i + 1)  + "º (CORTO) " + posicionCerradasNotDuplicadas.get(i).getJugador() + ": " + posicionCerradasNotDuplicadas.get(i).getSimbolo() + ChatColor.GREEN + " +" + rentabilidad + "%");
                }else{
                    lore6.add("" + ChatColor.GOLD + (i + 1)  + "º " + posicionCerradasNotDuplicadas.get(i).getJugador() + ": " + posicionCerradasNotDuplicadas.get(i).getSimbolo() + ChatColor.GREEN + " +" + rentabilidad + "%");
                }
            }
        }

        topOperacionesItemMeta.setLore(lore6);
        topOperaciones.setItemMeta(topOperacionesItemMeta);

        return topOperaciones;
    }

    private ItemStack buildItemPeoresOperacioensBolsa() {
        ItemStack topOperaciones = new ItemStack(Material.COAL_BLOCK);
        ItemMeta topOperacionesItemMeta = topOperaciones.getItemMeta();
        topOperacionesItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP PEORES OPERAIONES BOLSA");

        List<PosicionCerrada> posicionCerradasNotDuplicadas = posicionesCerradasMySQL.getPeoresRentabilidades();
        posicionCerradasNotDuplicadas = getNotDuplicatedElements(posicionCerradasNotDuplicadas);

        List<String> lore6 = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            double rentabilidad = Funciones.redondeoDecimales(posicionCerradasNotDuplicadas.get(i).getRentabilidad(), 3);

            if(posicionCerradasNotDuplicadas.get(i).getTipo_posicion().equalsIgnoreCase("CORTO")){
                lore6.add("" + ChatColor.GOLD + (i + 1)  + "º (CORTO) " + posicionCerradasNotDuplicadas.get(i).getJugador() + ": " + posicionCerradasNotDuplicadas.get(i).getSimbolo() + ChatColor.RED + " " + rentabilidad + "%");
            }else{
                lore6.add("" + ChatColor.GOLD + (i + 1)  + "º " + posicionCerradasNotDuplicadas.get(i).getJugador() + ": " + posicionCerradasNotDuplicadas.get(i).getSimbolo() + ChatColor.RED + " " + rentabilidad + "%");
            }
        }

        topOperacionesItemMeta.setLore(lore6);
        topOperaciones.setItemMeta(topOperacionesItemMeta);

        return topOperaciones;
    }

    private ItemStack buildMejoresComerciantes () {
        ItemStack topComerciantes = new ItemStack(Material.EMERALD);
        ItemMeta meta = topComerciantes.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP COMERCIANTES MAS INTENSIVOS (MENOS MINAN)");

        infoJugadores.sort( (inf1, inf2) -> Double.compare(inf2.porcentajePatrimonioIngresos, inf1.porcentajePatrimonioIngresos) );
        List<String> lore = new ArrayList<>();
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

        meta.setLore(lore);
        topComerciantes.setItemMeta(meta);

        return topComerciantes;
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
        Map<String, Double> mapPatrimonio = Funciones.crearMapaTopPatrimonioPlayers(false);

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
            this.porcentajePatrimonioIngresos = Funciones.rentabilidad(patrimonio, jugador.getIngresos() - jugador.getGastos());
        }
    }
}
