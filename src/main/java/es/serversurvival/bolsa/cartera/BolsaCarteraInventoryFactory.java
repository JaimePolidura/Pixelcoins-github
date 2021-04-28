package es.serversurvival.bolsa.cartera;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.bolsa.llamadasapi.mysql.LlamadaApi;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionesAbiertas;
import es.serversurvival.bolsa.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.utils.Funciones;
import es.serversurvival.shared.menus.Paginated;
import es.serversurvival.shared.menus.inventory.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.ChatColor.*;

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

        inventory.setItem(0, info);
        inventory.setItem(1, valores);
        inventory.setItem(8, buildItemResultado());

        for (int i = 0; i < posicionesAbiertasItems.size(); i++) {
            if (i < 43) {
                inventory.setItem(i + 9, posicionesAbiertasItems.get(i));
            } else {
                itemExcessInventory.add(posicionesAbiertasItems.get(i));
            }
        }

        if (itemExcessInventory.size() > 0) {
            inventory.setItem(52, back);
            inventory.setItem(53, buildItemFordward());
        } else {
            inventory.setItem(53, back);
        }

        return inventory;
    }

    public Inventory buildInventoryExecess() {
        Inventory inventory = Bukkit.createInventory(null, 54, BolsaCarteraMenu.titulo);

        List<ItemStack> copyOfItemExcessList = new ArrayList<>(itemExcessInventory);
        itemExcessInventory.clear();

        boolean addFordwardItem = false;

        for (int i = 0; i < copyOfItemExcessList.size(); i++) {
            if (i < 51) {
                inventory.addItem(copyOfItemExcessList.get(i));
            } else {
                itemExcessInventory.add(copyOfItemExcessList.get(i));
                addFordwardItem = true;
            }
        }

        inventory.setItem(53, buildItemInfo());
        if (addFordwardItem) {
            inventory.setItem(51, buildItemGoBack());
            inventory.setItem(52, buildItemFordward());
        } else {
            inventory.setItem(52, buildItemGoBack());
        }

        return inventory;
    }

    private List<ItemStack> buildItemsPosicionesAbiertas(String jugador) {
        List<ItemStack> posicionesAbiertasItems = new ArrayList<>();

        List<PosicionAbierta> posicionAbiertasJugador = posicionesAbiertasMySQL.getPosicionesAbiertasJugador(jugador);
        this.liquidezjugador = jugadoresMySQL.getJugador(jugador).getPixelcoins();
        rellenarLlamadasApi();
        rellenarPosicionesAbiertasPeso(posicionAbiertasJugador, getTotalInvertido(posicionAbiertasJugador));

        for (PosicionAbierta posicion : posicionAbiertasJugador) {
            if (posicion.getTipo_posicion() == TipoPosicion.LARGO) {
                posicionesAbiertasItems.add(buildPosicionAbiertaLarga(posicion));
            } else {
                posicionesAbiertasItems.add(buildPosicionAbiertaCorto(posicion));
            }
        }

        return posicionesAbiertasItems;
    }

    private void rellenarLlamadasApi() {
        List<LlamadaApi> llamadaApisList = llamadasApiMySQL.getTodasLlamadasApi();
        Map<String, LlamadaApi> llamadaApiMap = new HashMap<>();

        llamadaApisList.forEach((llamada) -> {
            llamadaApiMap.put(llamada.getSimbolo(), llamada);
        });

        this.llamadasApis = llamadaApiMap;
    }

    private void rellenarPosicionesAbiertasPeso(List<PosicionAbierta> posicionesJugador, double totalInverito) {
        Map<String, Integer> posicionesAbiertasConPeso = new HashMap<>();

        posicionesJugador.forEach((posicion) -> {
            posicionesAbiertasConPeso.put(posicion.getNombre_activo(), (int) Funciones.rentabilidad(totalInverito, posicion.getCantidad() * llamadasApis.get(posicion.getNombre_activo()).getPrecio()));
        });

        this.posicionesAbiertasPeso = posicionesAbiertasConPeso;
    }

    private double getTotalInvertido(List<PosicionAbierta> posicionAbiertas) {
        return posicionAbiertas.stream()
                .mapToDouble((pos) -> pos.getCantidad() * llamadasApis.get(pos.getNombre_activo()).getPrecio())
                .sum();
    }

    public ItemStack buildItemFordward() {
        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(Paginated.ITEM_NAME_GOFORDWARD)
                .build();
    }

    private ItemStack buildPosicionAbiertaLarga(PosicionAbierta posicion) {
        LlamadaApi llamada = llamadasApis.get(posicion.getNombre_activo());

        double precioAcutal = llamada.getPrecio();
        double perdidasOBeneficios = posicion.getCantidad() * (precioAcutal - posicion.getPrecio_apertura());
        double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(posicion.getPrecio_apertura(), precioAcutal), 2);
        double peso = posicionesAbiertasPeso.get(posicion.getNombre_activo());
        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(GOLD + "Empresa: " + llamada.getNombre_activo());

        if (!PosicionesAbiertas.getNombreSimbolo(posicion.getNombre_activo()).equalsIgnoreCase(posicion.getNombre_activo())) {
            lore.add(GOLD + "Ticker: " + posicion.getNombre_activo() + " (" + PosicionesAbiertas.getNombreSimbolo(posicion.getNombre_activo()) + ")");
        } else {
            lore.add(GOLD + "Ticker: " + posicion.getNombre_activo() + " (Acciones) ");
        }
        lore.add(GOLD + "Peso en cartera: " + peso + "%");
        lore.add("   ");
        lore.add(GOLD + "Acciones compradas: " + posicion.getCantidad());
        lore.add(GOLD + "Precio apertura: " + GREEN + formatea.format(posicion.getPrecio_apertura()) + " PC");
        lore.add(GOLD + "Precio actual: " + GREEN + formatea.format(precioAcutal));
        if (perdidasOBeneficios >= 0) {
            lore.add(GOLD + "Beneficios totales: " + GREEN + "+" + formatea.format(perdidasOBeneficios) + " PC");
            lore.add(GOLD + "Rentabilidad: " + GREEN + "+" + rentabilidad + "%");
        } else {
            lore.add(GOLD + "Perdidas totales: " + RED + formatea.format(perdidasOBeneficios) + " PC");
            lore.add(GOLD + "Rentabilidad: " + RED + rentabilidad + "%");
        }
        lore.add(GOLD + "Valor total: " + GREEN + formatea.format(precioAcutal * posicion.getCantidad()) + " PC");
        lore.add("   ");
        lore.add(GOLD + "Fecha de compra: " + posicion.getPrecio_apertura());
        lore.add(GOLD + "ID: " + posicion.getId());

        this.valorTotal = valorTotal + (precioAcutal * posicion.getCantidad());
        this.resultadoTotal = resultadoTotal + perdidasOBeneficios;

        return ItemBuilder.of(Material.NAME_TAG)
                .title(GOLD + "" + BOLD + UNDERLINE + "CLICK PARA VENDER")
                .lore(lore)
                .build();
    }

    private ItemStack buildPosicionAbiertaCorto(PosicionAbierta posicion) {
        LlamadaApi llamada = llamadasApis.get(posicion.getNombre_activo());

        double precioAcutal = llamada.getPrecio();
        double perdidasOBeneficios = posicion.getCantidad() * (posicion.getPrecio_apertura() - precioAcutal);
        double rentabilidad = Math.abs(Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(posicion.getPrecio_apertura(), precioAcutal), 2));

        double peso = posicionesAbiertasPeso.get(posicion.getNombre_activo());
        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(GOLD + "Empresa: " + llamada.getNombre_activo());

        if (!PosicionesAbiertas.getNombreSimbolo(posicion.getNombre_activo()).equalsIgnoreCase(posicion.getNombre_activo())) {
            lore.add(GOLD + "Ticker: " + posicion.getNombre_activo() + " (" + PosicionesAbiertas.getNombreSimbolo(posicion.getNombre_activo()) + ")");
        } else {
            lore.add(GOLD + "Ticker: " + posicion.getNombre_activo() + " (Acciones) ");
        }
        lore.add(GOLD + "Peso en cartera: " + peso + "%");
        lore.add("   ");
        lore.add(GOLD + "Acciones vendidas: " + posicion.getCantidad());
        lore.add(GOLD + "Precio apertura: " + GREEN + formatea.format(posicion.getPrecio_apertura()) + " PC");
        lore.add(GOLD + "Precio actual: " + GREEN + formatea.format(precioAcutal));
        if (perdidasOBeneficios >= 0) {
            lore.add(GOLD + "Beneficios totales: " + GREEN + "+" + formatea.format(perdidasOBeneficios) + " PC");
            lore.add(GOLD + "Rentabilidad: " + GREEN + "+" + rentabilidad + "%");
        } else {
            lore.add(GOLD + "Perdidas totales: " + RED + formatea.format(perdidasOBeneficios) + " PC");
            lore.add(GOLD + "Rentabilidad: " + RED + rentabilidad + "%");
        }
        lore.add(GOLD + "Valor total: " + GREEN + formatea.format(precioAcutal * posicion.getCantidad()) + " PC");
        lore.add("   ");
        lore.add(GOLD + "Fecha de compra: " + posicion.getFecha_apertura());
        lore.add(GOLD + "ID: " + posicion.getId());

        this.resultadoTotal = resultadoTotal + perdidasOBeneficios;

        return ItemBuilder.of(Material.REDSTONE_TORCH)
                .title(GOLD + "" + BOLD + UNDERLINE + "CLICK PARA COMPRAR " + RED + "" + BOLD + "(CORTO)")
                .lore(lore)
                .build();
    }

    private ItemStack buildItemInfo() {
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

        return ItemBuilder.of(Material.PAPER)
                .title(GOLD + "" + BOLD + "INFO")
                .lore(lore)
                .build();
    }

    private ItemStack buildItemResultado() {
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Estas al " + Funciones.redondeoDecimales(Funciones.rentabilidad(liquidezjugador + valorTotal, valorTotal), 1) + " % invertido");
        lore.add(GOLD + "Valor total: " + GREEN + formatea.format(valorTotal) + " PC");
        if (resultadoTotal >= 0)
            lore.add(GOLD + "Beneficios: " + GREEN + formatea.format(resultadoTotal) + " PC");
        else
            lore.add(GOLD + "Perdidas: " + RED + formatea.format(resultadoTotal) + " PC");

        double rentabilidad = Funciones.rentabilidad(valorTotal, resultadoTotal);
        if (rentabilidad > 0)
            lore.add(GOLD + "Estas ganando un: " + GREEN + "+" + rentabilidad + " %");
        else
            lore.add(GOLD + "Estas perdiendo un: " + RED + rentabilidad + " %");

        return ItemBuilder.of(Material.WRITABLE_BOOK)
                .title(GOLD + "" + BOLD + "REUSLTADO")
                .lore(lore)
                .build();
    }

    private ItemStack buildItemValores() {
        return ItemBuilder.of(Material.BOOK)
                .title(GOLD + "" + BOLD + "" + UNDERLINE + "CLICK PARA COMPRAR VALORES")
                .build();
    }
}
