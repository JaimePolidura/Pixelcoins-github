package es.serversurvival.menus.inventoryFactory;

import es.serversurvival.menus.menus.Paginated;
import es.serversurvival.mySQL.*;
import es.serversurvival.util.Funciones;
import es.serversurvival.util.MinecraftUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;

public abstract class InventoryFactory {
    protected DecimalFormat formatea = Funciones.FORMATEA;
    protected static Cuentas cuentasMySQL = Cuentas.INSTANCE;
    protected static Empleados empleadosMySQL = Empleados.INSTANCE;
    protected static Empresas empresasMySQL = Empresas.INSTANCE;
    protected static Encantamientos encantamientosMySQL = Encantamientos.INSTANCE;
    protected static Jugadores jugadoresMySQL = Jugadores.INSTANCE;
    protected static LlamadasApi llamadasApiMySQL = LlamadasApi.INSTANCE;
    protected static Mensajes mensajesMySQL = Mensajes.INSTANCE;
    protected static Ofertas ofertasMySQL = Ofertas.INSTANCE;
    protected static PosicionesAbiertas posicionesAbiertasMySQL = PosicionesAbiertas.INSTANCE;
    protected static PosicionesCerradas posicionesCerradasMySQL = PosicionesCerradas.INSTANCE;
    protected static Transacciones transaccionesMySQL = Transacciones.INSTANCE;
    protected static Deudas deudasMySQL = Deudas.INSTANCE;
    protected static OrdenesPreMarket ordenesMySQL = OrdenesPreMarket.INSTANCE;
    protected static OfertasMercadoServer ofertasMercadoServerMySQL = OfertasMercadoServer.INSTANCE;

    protected abstract Inventory buildInventory (String jugador);

    protected ItemStack buildItemGoBack () {
        ItemStack back = new ItemStack(Material.RED_WOOL);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "<--");

        back.setItemMeta(backMeta);
        return back;
    }

    protected ItemStack buildItemFordward () {
        return MinecraftUtils.displayname(Material.GREEN_WOOL, Paginated.ITEM_NAME_GOFORDWARD);
    }
}
