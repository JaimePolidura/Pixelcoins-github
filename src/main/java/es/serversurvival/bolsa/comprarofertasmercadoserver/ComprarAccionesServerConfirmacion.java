package es.serversurvival.bolsa.comprarofertasmercadoserver;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.bolsa.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.AumentoConfirmacion;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GREEN;

public class ComprarAccionesServerConfirmacion extends Menu implements AumentoConfirmacion {
    private final ComprarOfertaMercadoUseCase useCase = ComprarOfertaMercadoUseCase.INSTANCE;

    private final String nombreEmpresa;
    private double precioTotal;
    private final Inventory inventory;
    private final Player player;
    private final OfertaMercadoServer oferta;
    private final double dineroJugador;

    private int cantidadAComprar;

    public ComprarAccionesServerConfirmacion(Player player, int id) {
        this.player = player;

        OfertaMercadoServer oferta = AllMySQLTablesInstances.ofertasMercadoServerMySQL.get(id);

        this.oferta = oferta;
        this.dineroJugador = AllMySQLTablesInstances.jugadoresMySQL.getJugador(player.getName()).getPixelcoins();
        this.nombreEmpresa = oferta.getEmpresa();
        this.cantidadAComprar = 1;

        String titulo = DARK_RED + "" + BOLD + "   SELECCIONA ACCIONES";
        String tituloAceptar = GREEN + "" + BOLD + "COMPRAR ACCIONES";
        String tituloCancelar = RED + "" + BOLD + "CANCELAR";
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Comprar 1 acciones de " + nombreEmpresa + " a " + GREEN + AllMySQLTablesInstances.formatea.format(oferta.getPrecio()) + " PC");

        this.inventory = InventoryCreator.createConfirmacionAumento(titulo, tituloAceptar, lore, tituloCancelar);

        openMenu();
    }

    @Override
    public void onChangeAumento(int var) {
        int nuevaCantidad = var + this.cantidadAComprar;
        double nuevoPrecioTotal = oferta.getPrecio() * nuevaCantidad;

        if(nuevoPrecioTotal > dineroJugador || nuevoPrecioTotal <= 0 || nuevaCantidad > oferta.getCantidad() || nuevaCantidad <= 0){
            return;
        }

        this.cantidadAComprar = nuevaCantidad;
        this.precioTotal = nuevoPrecioTotal;

        String displayName = GREEN + "" + BOLD + "COMPRAR ACCIONES";
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "Comprar " + nuevaCantidad + " acciones " + nombreEmpresa + " a " + GREEN + oferta.getPrecio() + " PC -> total: " + AllMySQLTablesInstances.formatea.format(Funciones.redondeoDecimales(precioTotal, 3)) + " PC");

        ItemBuilder.of(Material.GREEN_WOOL).title(displayName).lore(lore).buildAddInventory(inventory, 14);
    }

    @Override
    public void confirmar() {
        if (dineroJugador < precioTotal) {
            player.sendMessage(DARK_RED + "No tienes el suficiente dinero");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            closeMenu();
            return;
        }

        useCase.comprarOfertaMercadoAccionServer(player.getName(), oferta.getId(), this.cantidadAComprar);

        sendMessage();

        closeMenu();
    }

    private void sendMessage () {
        Funciones.enviarMensajeYSonido(player, GOLD + "Has comprado " + AllMySQLTablesInstances.formatea.format(cantidadAComprar)  + " acciones a " + GREEN + AllMySQLTablesInstances.formatea.format(oferta.getPrecio()) + " PC" + GOLD + " que es un total de " +
                GREEN + AllMySQLTablesInstances.formatea.format(precioTotal) + " PC " + GOLD + " comandos: " + AQUA + "/bolsa vender /bolsa cartera", Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " ha comprado " + cantidadAComprar + " acciones de la empresa del server: " + nombreEmpresa + " a " + GREEN + AllMySQLTablesInstances.formatea.format(oferta.getPrecio()) + "PC");

        if(oferta.getTipo_ofertante() == TipoOfertante.EMPRESA){
            String mensajeOnline = GOLD + player.getName() + " ha comprado " + cantidadAComprar + " acciones de " + nombreEmpresa + "."+GREEN+" +" +
                    AllMySQLTablesInstances.formatea.format(precioTotal) + "PC";

            //TODO
            Funciones.enviarMensaje(AllMySQLTablesInstances.empresasMySQL.getEmpresa(nombreEmpresa).getOwner(), mensajeOnline, mensajeOnline);
        }else{
            double beneficiosPerdidas = (oferta.getPrecio() - oferta.getPrecio_apertura()) * cantidadAComprar;
            double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(oferta.getPrecio_apertura(), oferta.getPrecio()), 3);

            String mensajeOnline = beneficiosPerdidas >= 0 ?
                    GOLD + player.getName() + " te ha comprado " + cantidadAComprar + " acciones de " + nombreEmpresa + " con unos beneficios de " + GREEN + "+" + AllMySQLTablesInstances.formatea.format(beneficiosPerdidas) + " PC +" + AllMySQLTablesInstances.formatea.format(rentabilidad):
                    GOLD + player.getName() + " te ha comprado " + cantidadAComprar + " acciones de " + nombreEmpresa + " con unos beneficios de " + RED + AllMySQLTablesInstances.formatea.format(beneficiosPerdidas) + " PC " + AllMySQLTablesInstances.formatea.format(rentabilidad) ;
            String mensajeOffline = beneficiosPerdidas >= 0 ?
                    player.getName() + " te ha comprado " + cantidadAComprar + " acciones de " + nombreEmpresa + " con unos beneficios de " + "+" + AllMySQLTablesInstances.formatea.format(beneficiosPerdidas) + " PC +" + AllMySQLTablesInstances.formatea.format(rentabilidad) :
                    player.getName() + " te ha comprado " + cantidadAComprar + " acciones de " + nombreEmpresa + " con unos beneficios de " + AllMySQLTablesInstances.formatea.format(beneficiosPerdidas) + " PC" + AllMySQLTablesInstances.formatea.format(rentabilidad);

            Funciones.enviarMensaje(oferta.getJugador(), mensajeOnline, mensajeOffline);
        }
    }

    @Override
    public void cancelar() {
        player.sendMessage(GOLD + "Has cancelado la compra");

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
