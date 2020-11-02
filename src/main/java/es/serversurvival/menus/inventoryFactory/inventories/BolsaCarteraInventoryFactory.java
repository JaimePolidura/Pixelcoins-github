package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.menus.Paginated;
import es.serversurvival.mySQL.tablasObjetos.LlamadaApi;
import es.serversurvival.mySQL.enums.POSICION;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.menus.BolsaCarteraMenu;
import es.serversurvival.mySQL.LlamadasApi;
import es.serversurvival.mySQL.PosicionesAbiertas;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BolsaCarteraInventoryFactory extends InventoryFactory {
    private double resultadoTotal;
    private double valorTotal;
    private double liquidezjugador;
    private Map<String, LlamadaApi> llamadasApis;
    private Map<String, Integer> posicionesAbiertasPeso;
    private List<ItemStack> itemExcessInventory = new ArrayList<>();

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, BolsaCarteraMenu.titulo);

        ItemStack info = buildItemInfo();
        ItemStack valores = buildItemValores();
        ItemStack back = buildItemGoBack();
        List<ItemStack> posicionesAbiertasItems = buildItemsPosicionesAbiertas(jugador);
        ItemStack resultado = buildItemResultado();

        inventory.setItem(0, info);
        inventory.setItem(1, valores);
        inventory.setItem(8, buildItemResultado());

        for(int i = 0; i < posicionesAbiertasItems.size(); i++){
            if(i < 43){
                inventory.setItem(i + 9, posicionesAbiertasItems.get(i));
            }else{
                itemExcessInventory.add(posicionesAbiertasItems.get(i));
            }
        }

        if(itemExcessInventory.size() > 0){
            inventory.setItem(52, back);
            inventory.setItem(53, buildItemFordward());
        }else{
            inventory.setItem(53, back);
        }

        return inventory;
    }

    public Inventory buildInventoryExecess () {
        Inventory inventory = Bukkit.createInventory(null, 54, BolsaCarteraMenu.titulo);

        List<ItemStack> copyOfItemExcessList = new ArrayList<>(itemExcessInventory);
        itemExcessInventory.clear();

        boolean addFordwardItem = false;

        for(int i = 0; i < copyOfItemExcessList.size(); i++){
            if(i < 51){
                inventory.addItem(copyOfItemExcessList.get(i));
            }else{
                itemExcessInventory.add(copyOfItemExcessList.get(i));
                addFordwardItem = true;
            }
        }

        inventory.setItem(53, buildItemInfo());
        if(addFordwardItem){
            inventory.setItem(51, buildItemGoBack());
            inventory.setItem(52, buildItemFordward());
        }else{
            inventory.setItem(52, buildItemGoBack());
        }

        return inventory;
    }

    private List<ItemStack> buildItemsPosicionesAbiertas (String jugador) {
        List<ItemStack> posicionesAbiertasItems = new ArrayList<>();

        llamadasApiMySQL.conectar();
        List<PosicionAbierta> posicionAbiertasJugador = posicionesAbiertasMySQL.getPosicionesAbiertasJugador(jugador);
        this.liquidezjugador = jugadoresMySQL.getJugador(jugador).getPixelcoin();
        rellenarLlamadasApi();
        rellenarPosicionesAbiertasPeso(posicionAbiertasJugador, getTotalInvertido(posicionAbiertasJugador));

        for (PosicionAbierta posicion : posicionAbiertasJugador) {
            if(posicion.getTipoPosicion().equalsIgnoreCase(POSICION.LARGO.toString())){
                posicionesAbiertasItems.add(buildPosicionAbiertaLarga(posicion));
            }else{
                posicionesAbiertasItems.add(buildPosicionAbiertaCorto(posicion));
            }
        }
        llamadasApiMySQL.desconectar();

        return posicionesAbiertasItems;
    }

    private void rellenarLlamadasApi () {
        List<LlamadaApi> llamadaApisList = llamadasApiMySQL.getTodasLlamadasApi();
        Map<String, LlamadaApi> llamadaApiMap = new HashMap<>();

        llamadaApisList.forEach( (llamada) -> {
            llamadaApiMap.put(llamada.getSimbolo(), llamada);
        });

        this.llamadasApis = llamadaApiMap;
    }

    private void rellenarPosicionesAbiertasPeso(List<PosicionAbierta> posicionesJugador, double totalInverito) {
        Map<String, Integer> posicionesAbiertasConPeso = new HashMap<>();

        posicionesJugador.forEach( (posicion) -> {
            posicionesAbiertasConPeso.put(posicion.getNombre(), (int) Funciones.rentabilidad(totalInverito, posicion.getCantidad() * llamadasApis.get(posicion.getNombre()).getPrecio()));
        });

        this.posicionesAbiertasPeso = posicionesAbiertasConPeso;
    }

    private double getTotalInvertido (List<PosicionAbierta> posicionAbiertas) {
        return posicionAbiertas.stream()
                .mapToDouble((pos) -> pos.getCantidad() * llamadasApis.get(pos.getNombre()).getPrecio())
                .sum();
    }

    private ItemStack buildItemFordward () {
        ItemStack forward = new ItemStack(Material.GREEN_WOOL);
        ItemMeta forwadMeta = forward.getItemMeta();
        forwadMeta.setDisplayName(Paginated.ITEM_NAME_GOFORDWARD);
        forward.setItemMeta(forwadMeta);

        return forward;
    }

    private ItemStack buildPosicionAbiertaLarga (PosicionAbierta posicion) {
        ItemStack posicionItem = new ItemStack(Material.NAME_TAG);
        ItemMeta posicionMeta = posicionItem.getItemMeta();

        posicionMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "CLICK PARA VENDER");

        LlamadaApi llamada = llamadasApis.get(posicion.getNombre());

        double precioAcutal = llamada.getPrecio();
        double perdidasOBeneficios = posicion.getCantidad() * (precioAcutal - posicion.getPrecioApertura());
        double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(posicion.getPrecioApertura(), precioAcutal), 2);
        double peso = posicionesAbiertasPeso.get(posicion.getNombre());
        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Empresa: " + llamada.getNombreValor());

        if(!PosicionesAbiertas.getNombreSimbolo(posicion.getNombre()).equalsIgnoreCase(posicion.getNombre())) {
            lore.add(ChatColor.GOLD + "Ticker: " + posicion.getNombre() + " (" + PosicionesAbiertas.getNombreSimbolo(posicion.getNombre()) + ")");
        }else {
            lore.add(ChatColor.GOLD + "Ticker: " + posicion.getNombre() + " (Acciones) ");
        }
        lore.add(ChatColor.GOLD + "Peso en cartera: " + peso + "%");
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Acciones compradas: " + posicion.getCantidad());
        lore.add(ChatColor.GOLD + "Precio apertura: " + ChatColor.GREEN +formatea.format(posicion.getPrecioApertura()) + " PC");
        lore.add(ChatColor.GOLD + "Precio actual: " + ChatColor.GREEN + formatea.format(precioAcutal));
        if(perdidasOBeneficios >= 0){
            lore.add(ChatColor.GOLD + "Beneficios totales: " +ChatColor.GREEN + "+" + formatea.format(perdidasOBeneficios) + " PC");
            lore.add(ChatColor.GOLD + "Rentabilidad: " + ChatColor.GREEN + "+" + rentabilidad + "%");
        }else{
            lore.add(ChatColor.GOLD + "Perdidas totales: " + ChatColor.RED + formatea.format(perdidasOBeneficios) + " PC");
            lore.add(ChatColor.GOLD + "Rentabilidad: " + ChatColor.RED + rentabilidad + "%");
        }
        lore.add(ChatColor.GOLD + "Valor total: " + ChatColor.GREEN + formatea.format(precioAcutal * posicion.getCantidad()) + " PC");
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Fecha de compra: " + posicion.getFechaApertura());
        lore.add(ChatColor.GOLD + "ID: " + posicion.getId());

        posicionMeta.setLore(lore);
        posicionItem.setItemMeta(posicionMeta);

        this.valorTotal = valorTotal + (precioAcutal * posicion.getCantidad());
        this.resultadoTotal = resultadoTotal + perdidasOBeneficios;

        return posicionItem;
    }

    private ItemStack buildPosicionAbiertaCorto (PosicionAbierta posicion) {
        ItemStack posicionItem = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta posicionMeta = posicionItem.getItemMeta();

        posicionMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "CLICK PARA COMPRAR " + ChatColor.RED + "" + ChatColor.BOLD + "(CORTO)");

        LlamadaApi llamada = llamadasApis.get(posicion.getNombre());

        double precioAcutal = llamada.getPrecio();
        double perdidasOBeneficios = posicion.getCantidad() * (posicion.getPrecioApertura() - precioAcutal);
        double rentabilidad = Math.abs(Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(posicion.getPrecioApertura() ,precioAcutal), 2));

        double peso = posicionesAbiertasPeso.get(posicion.getNombre());
        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Empresa: " + llamada.getNombreValor());

        if(!PosicionesAbiertas.getNombreSimbolo(posicion.getNombre()).equalsIgnoreCase(posicion.getNombre())) {
            lore.add(ChatColor.GOLD + "Ticker: " + posicion.getNombre() + " (" + PosicionesAbiertas.getNombreSimbolo(posicion.getNombre()) + ")");
        }else {
            lore.add(ChatColor.GOLD + "Ticker: " + posicion.getNombre() + " (Acciones) ");
        }
        lore.add(ChatColor.GOLD + "Peso en cartera: " + peso + "%");
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Acciones vendidas: " + posicion.getCantidad());
        lore.add(ChatColor.GOLD + "Precio apertura: " + ChatColor.GREEN +formatea.format(posicion.getPrecioApertura()) + " PC");
        lore.add(ChatColor.GOLD + "Precio actual: " + ChatColor.GREEN + formatea.format(precioAcutal));
        if(perdidasOBeneficios >= 0){
            lore.add(ChatColor.GOLD + "Beneficios totales: " +ChatColor.GREEN + "+" + formatea.format(perdidasOBeneficios) + " PC");
            lore.add(ChatColor.GOLD + "Rentabilidad: " + ChatColor.GREEN + "+" + rentabilidad + "%");
        }else{
            lore.add(ChatColor.GOLD + "Perdidas totales: " + ChatColor.RED + formatea.format(perdidasOBeneficios) + " PC");
            lore.add(ChatColor.GOLD + "Rentabilidad: " + ChatColor.RED + rentabilidad + "%");
        }
        lore.add(ChatColor.GOLD + "Valor total: " + ChatColor.GREEN + formatea.format(precioAcutal * posicion.getCantidad()) + " PC");
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Fecha de compra: " + posicion.getFechaApertura());
        lore.add(ChatColor.GOLD + "ID: " + posicion.getId());

        posicionMeta.setLore(lore);
        posicionItem.setItemMeta(posicionMeta);

        this.resultadoTotal = resultadoTotal + perdidasOBeneficios;

        return posicionItem;
    }

    private ItemStack buildItemInfo () {
        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();

        infoMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "INFO");

        List<String> lore = new ArrayList<>();
        lore.add("Puedes comprar valores en la bolsa");
        lore.add("que cotizen en Estados Unidos");
        lore.add("Para buscar valores para invertir");
        lore.add("le das click al item de la derecha");
        lore.add("Si quieres otro valor que no este");
        lore.add("en la lista debes poner:");
        lore.add("/bolsa invertir <ticker> <nacciones>");
        lore.add("  <ticker> es la letra identificatoria");
        lore.add("    del valor: ejemplo Amazon: AMZN");
        lore.add("    (la empresa debe cotizar en USA)");
        lore.add("   <nacciones> numero de acciones a comprar");
        lore.add("   ");
        lore.add("Para consultar tus valores en carteras ");
        lore.add("y venderlas tienes tres vias: el menu actual,");
        lore.add("la web y en el comando /bolsa cartera");
        lore.add("   ");
        lore.add("Mas info en /bolsa ayuda o /ayuda bolsa o en:");
        lore.add("http://serversurvival2.ddns.net/perfil");

        infoMeta.setLore(lore);
        info.setItemMeta(infoMeta);

        return info;
    }

    private ItemStack buildItemResultado() {
        ItemStack resultado = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta resultadoMeta = resultado.getItemMeta();

        resultadoMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "REUSLTADO");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Estas al " + Funciones.redondeoDecimales(Funciones.rentabilidad(liquidezjugador + valorTotal, valorTotal),1)  + " % invertido");
        lore.add(ChatColor.GOLD + "Valor total: " + ChatColor.GREEN + formatea.format(valorTotal) + " PC");
        if(resultadoTotal >= 0)
            lore.add(ChatColor.GOLD + "Beneficios: " + ChatColor.GREEN + formatea.format(resultadoTotal) + " PC");
        else
            lore.add(ChatColor.GOLD + "Perdidas: " + ChatColor.RED + formatea.format(resultadoTotal) + " PC");

        double rentabilidad = Funciones.rentabilidad(valorTotal, resultadoTotal);
        if(rentabilidad > 0)
            lore.add(ChatColor.GOLD + "Estas ganando un: " + ChatColor.GREEN + "+" +  rentabilidad + " %");
        else
            lore.add(ChatColor.GOLD + "Estas perdiendo un: " + ChatColor.RED + rentabilidad + " %");

        resultadoMeta.setLore(lore);
        resultado.setItemMeta(resultadoMeta);
        return resultado;
    }

    private ItemStack buildItemValores () {
        ItemStack valores = new ItemStack(Material.BOOK);
        ItemMeta valoresMeta = valores.getItemMeta();

        valoresMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "" +ChatColor.UNDERLINE + "CLICK PARA COMPRAR VALORES");

        valores.setItemMeta(valoresMeta);
        return valores;
    }
}