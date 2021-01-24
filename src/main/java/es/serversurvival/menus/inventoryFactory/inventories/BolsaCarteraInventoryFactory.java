package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.menus.Paginated;
import es.serversurvival.mySQL.enums.TipoValor;
import es.serversurvival.mySQL.tablasObjetos.LlamadaApi;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.menus.BolsaCarteraMenu;
import es.serversurvival.mySQL.PosicionesAbiertas;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import es.serversurvival.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static es.serversurvival.mySQL.enums.TipoPosicion.*;
import static es.serversurvival.util.Funciones.*;

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
        llamadasApiMySQL.conectar();
        this.liquidezjugador = jugadoresMySQL.getJugador(jugador).getPixelcoins();
        rellenarLlamadasApi();

        List<PosicionAbierta> posicionAbiertasJugador = posicionesAbiertasMySQL.getPosicionesAbiertasJugador(jugador);
        rellenarPosicionesAbiertasPeso(posicionAbiertasJugador, getTotalInvertido(posicionAbiertasJugador));

        List<ItemStack> posicionesAbiertasItems = new ArrayList<>();
        for (PosicionAbierta posicion : posicionAbiertasJugador) {
            posicionesAbiertasItems.add(caca(posicion));
        }

        llamadasApiMySQL.desconectar();

        return posicionesAbiertasItems;
    }

    private void rellenarLlamadasApi () {
        List<LlamadaApi> llamadaApisList = llamadasApiMySQL.getTodasLlamadasApi();
        Map<String, LlamadaApi> llamadaApiMap = new HashMap<>();

        llamadaApisList.forEach( (llamada) -> llamadaApiMap.put(llamada.getSimbolo(), llamada));

        this.llamadasApis = llamadaApiMap;
    }

    private void rellenarPosicionesAbiertasPeso(List<PosicionAbierta> posicionesJugador, double totalInverito) {
        Map<String, Integer> posicionesAbiertasConPeso = new HashMap<>();

        posicionesJugador.forEach( (posicion) -> {
            posicionesAbiertasConPeso.put(posicion.getNombre_activo(), (int) rentabilidad(totalInverito, posicion.getCantidad() * llamadasApis.get(posicion.getNombre_activo()).getPrecio()));
        });

        this.posicionesAbiertasPeso = posicionesAbiertasConPeso;
    }

    private double getTotalInvertido (List<PosicionAbierta> posicionAbiertas) {
        return getSumaTotalListDouble(posicionAbiertas, pos -> pos.getCantidad() * llamadasApis.get(pos.getNombre_activo()).getPrecio());
    }

    private ItemStack buildItemFordward () {
        return ItemBuilder.displayname(Material.GREEN_WOOL, Paginated.ITEM_NAME_GOFORDWARD);
    }

    private ItemStack caca (PosicionAbierta posicionAbierta) {
        String displayName = posicionAbierta.getTipo_posicion().equals(LARGO.toString()) ?
                ChatColor.GOLD + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "CLICK PARA VENDER" :
                ChatColor.GOLD + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "CLICK PARA COMPRAR " + ChatColor.RED + "" + ChatColor.BOLD + "(CORTO)";
        List<String> lore = buildLoreFromPosicionAbierta(posicionAbierta);

        return TipoPosicion.valueOf(posicionAbierta.getTipo_posicion()) == LARGO ?
                ItemBuilder.loreDisplayName(Material.NAME_TAG, displayName, lore) :
                ItemBuilder.loreDisplayName(Material.REDSTONE_TORCH, displayName, lore);
    }

    private List<String> buildLoreFromPosicionAbierta (PosicionAbierta posicion) {
        LlamadaApi llamada = llamadasApis.get(posicion.getNombre_activo());
        TipoPosicion tipoPosicion = TipoPosicion.valueOf(posicion.getTipo_posicion());

        double precioAcutal = llamada.getPrecio();
        double perdidasOBeneficios = tipoPosicion == LARGO ?
                posicion.getCantidad() * (precioAcutal - posicion.getPrecio_apertura()) :
                posicion.getCantidad() * (posicion.getPrecio_apertura() - precioAcutal);

        double rentabilidad = tipoPosicion == LARGO ?
                redondeoDecimales(diferenciaPorcntual(posicion.getPrecio_apertura(), precioAcutal), 2) :
                Math.abs(redondeoDecimales(diferenciaPorcntual(posicion.getPrecio_apertura() ,precioAcutal), 2));

        double peso = posicionesAbiertasPeso.get(posicion.getNombre_activo());
        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Empresa: " + llamada.getNombre_activo());

        if(!PosicionesAbiertas.getNombreSimbolo(posicion.getNombre_activo()).equalsIgnoreCase(posicion.getNombre_activo())) {
            lore.add(ChatColor.GOLD + "Ticker: " + posicion.getNombre_activo() + " (" + PosicionesAbiertas.getNombreSimbolo(posicion.getNombre_activo()) + ")");
        }else {
            lore.add(ChatColor.GOLD + "Ticker: " + posicion.getNombre_activo() + " (Acciones) ");
        }
        lore.add(ChatColor.GOLD + "Peso en cartera: " + peso + "%");
        lore.add("   ");

        if (tipoPosicion == LARGO) {
            lore.add(ChatColor.GOLD + "Acciones compradas: " + posicion.getCantidad());
        } else {
            lore.add(ChatColor.GOLD + "Acciones vendidas: " + posicion.getCantidad());
        }

        lore.add(ChatColor.GOLD + "Precio apertura: " + ChatColor.GREEN +formatea.format(posicion.getPrecio_apertura()) + " PC");
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

        if(tipoPosicion == LARGO){
            this.valorTotal = valorTotal + (precioAcutal * posicion.getCantidad());
            this.resultadoTotal = resultadoTotal + perdidasOBeneficios;
            lore.add(ChatColor.GOLD + "Fecha de compra: " + posicion.getFecha_apertura());
        }else{
            this.valorTotal = valorTotal + perdidasOBeneficios;
            this.resultadoTotal = resultadoTotal + perdidasOBeneficios;
            lore.add(ChatColor.GOLD + "Fecha de venta: " + posicion.getFecha_apertura());
        }

        lore.add(ChatColor.GOLD + "ID: " + posicion.getId());

        return lore;
    }

    private ItemStack buildItemInfo () {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "INFO";
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
        lore.add("http://serversurvival.ddns.net/perfil");

        return ItemBuilder.loreDisplayName(Material.PAPER, displayName, lore);
    }

    private ItemStack buildItemResultado() {
        List<String> lore = buildLoreFromResultado();
        String displayname = ChatColor.GOLD + "" + ChatColor.BOLD + "REUSLTADO";

        return ItemBuilder.loreDisplayName(Material.WRITABLE_BOOK, displayname, lore);
    }

    private List<String> buildLoreFromResultado () {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Estas al " + redondeoDecimales(rentabilidad(liquidezjugador + valorTotal, valorTotal),1)  + " % invertido");
        lore.add(ChatColor.GOLD + "Valor total: " + ChatColor.GREEN + formatea.format(valorTotal) + " PC");
        if(resultadoTotal >= 0)
            lore.add(ChatColor.GOLD + "Beneficios: " + ChatColor.GREEN + formatea.format(resultadoTotal) + " PC");
        else
            lore.add(ChatColor.GOLD + "Perdidas: " + ChatColor.RED + formatea.format(resultadoTotal) + " PC");

        double rentabilidad = rentabilidad(valorTotal, resultadoTotal);
        if(rentabilidad > 0)
            lore.add(ChatColor.GOLD + "Estas ganando un: " + ChatColor.GREEN + "+" +  rentabilidad + " %");
        else
            lore.add(ChatColor.GOLD + "Estas perdiendo un: " + ChatColor.RED + rentabilidad + " %");

        return lore;
    }

    private ItemStack buildItemValores () {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" +ChatColor.UNDERLINE + "CLICK PARA COMPRAR VALORES";

        return ItemBuilder.displayname(Material.BOOK, displayName);
    }
}
