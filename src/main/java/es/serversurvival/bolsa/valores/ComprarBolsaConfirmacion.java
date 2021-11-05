package es.serversurvival.bolsa.valores;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.ofertasmercadoserver.comprar.ComprarOfertaMercadoUseCase;
import es.serversurvival.bolsa.ordenespremarket.mysql.AccionOrden;
import es.serversurvival.shared.menus.Menu;
import es.serversurvival.shared.menus.AumentoConfirmacion;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;
import es.serversurvival.bolsa.posicionesabiertas.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.shared.utils.Funciones;
import es.serversurvival.shared.menus.inventory.InventoryCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.*;

public class ComprarBolsaConfirmacion extends Menu implements AumentoConfirmacion {
    private final ComprarOfertaMercadoUseCase comprarOfertaUseCase = ComprarOfertaMercadoUseCase.INSTANCE;
    private final ComprarLargoUseCase comprarLargoUseCase = ComprarLargoUseCase.INSTANCE;
    private final AbrirOrdenUseCase abrirOrdenUseCase = AbrirOrdenUseCase.INSTANCE;

    private final String simbolo;
    private double precioUnidad;
    private double precioTotal;
    private final TipoActivo tipoActivo;
    private final String alias;
    private final Inventory inventory;
    private final Player player;
    private int cantidadAComprar = 1;
    private double dineroJugador;
    private final String nombreValor;
    private int id;

    public ComprarBolsaConfirmacion(String simbolo, String nombreValor, TipoActivo tipoActivo, String alias, Player player, double precioUnidad) {
        this.nombreValor = nombreValor;
        this.alias = alias;
        this.tipoActivo = tipoActivo;
        this.simbolo = simbolo;
        this.precioUnidad = precioUnidad;
        this.player = player;
        this.precioTotal = precioUnidad;

        String titulo = DARK_RED + "" + BOLD + "   SELECCIONA " + alias.toUpperCase();
        String tituloAceptar = GREEN + "" + BOLD + "COMPRAR " + alias.toUpperCase();
        String tituloCancelar = RED + "" + BOLD + "CANCELAR";
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Comprar 1 " + alias + " de " + simbolo + " a " + GREEN + AllMySQLTablesInstances.formatea.format(precioUnidad) + " PC");

        this.inventory = InventoryCreator.createConfirmacionAumento(titulo, tituloAceptar, lore, tituloCancelar);

        this.dineroJugador = AllMySQLTablesInstances.jugadoresMySQL.getJugador(player.getName()).getPixelcoins();

        openMenu();
    }

    @Override
    public void onChangeAumento(int var) {
        this.cantidadAComprar = cantidadAComprar + var;

        precioTotal = precioUnidad * cantidadAComprar;
        if(precioTotal > dineroJugador){
            this.cantidadAComprar = cantidadAComprar - var;
            return;
        }

        if (precioTotal <= 0) {
            cantidadAComprar = cantidadAComprar - var;
            precioTotal = precioUnidad * cantidadAComprar;
            return;
        }
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "COMPRAR " + alias.toUpperCase();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Comprar " + cantidadAComprar + " " +  alias  + " " + simbolo + " a " + ChatColor.GREEN + precioUnidad + " PC -> total: " + AllMySQLTablesInstances.formatea.format(Funciones.redondeoDecimales(precioTotal, 3)) + " PC");

        ItemBuilder.of(Material.GREEN_WOOL).title(displayName).lore(lore).buildAddInventory(inventory, 14);
    }

    @Override
    public void confirmar() {
        if (dineroJugador < precioTotal) {
            player.sendMessage(ChatColor.DARK_RED + "No tienes el suficiente dinero");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            closeMenu();
            return;
        }

        if(tipoActivo == TipoActivo.ACCIONES_SERVER){
            //TODO
            comprarOfertaUseCase.comprarOfertaMercadoAccionServer(player.getName(), id, cantidadAComprar);
        }else if(Funciones.mercadoEstaAbierto()){
            comprarLargoUseCase.abrir(tipoActivo, simbolo, nombreValor, alias, precioUnidad, cantidadAComprar, player.getName());
        }else{
            abrirOrdenUseCase.abrirOrden(player.getName(), simbolo, cantidadAComprar, AccionOrden.LARGO_COMPRA, -1);
        }

        closeMenu();
    }

    @Override
    public void cancelar() {
        player.sendMessage(ChatColor.GOLD + "Has cancelado la compra");

        closeMenu();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
