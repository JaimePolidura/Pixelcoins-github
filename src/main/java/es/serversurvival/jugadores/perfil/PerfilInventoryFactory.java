package es.serversurvival.jugadores.perfil;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.bolsa._shared.posicionescerradas.mysql.PosicionCerrada;
import es.serversurvival.cuentaweb.Cuenta;
import es.serversurvival.deudas._shared.mysql.Deuda;
import es.serversurvival.empleados._shared.mysql.Empleado;
import es.serversurvival.empresas.mysql.Empresa;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.menus.inventory.InventoryFactory;
import es.serversurvival.shared.utils.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PerfilInventoryFactory extends InventoryFactory {
    private final List<Integer> posicionesCristales = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46 , 47, 48, 49, 50, 51, 52, 53);

    @Override
    protected Inventory buildInventory(String player) {
        Inventory inventory = Bukkit.createInventory(null, 54, PerfilMenu.titulo);

        inventory.setItem(10, buildItemWEB(player));
        inventory.setItem(13, buildItemStats(player));
        inventory.setItem(16, buildItemTienda());
        inventory.setItem(28, buildItemDeudas(player));
        inventory.setItem(30, buildItemBolsa(player));
        inventory.setItem(32, buildItemEmpresa(player));
        inventory.setItem(34, buildItemEmpleos(player));

        rellenarCristales(inventory);

        return inventory;
    }

    private ItemStack buildItemTienda () {
        return ItemBuilder.of(Material.CHEST).title(ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER LA TIENDA").build();
    }

    private ItemStack buildItemEmpleos (String jugador) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER TUS EMPLEOS";

        List<String> lore = new ArrayList<>();
        lore.add("  ");
        List<Empleado> empleos = empleadosMySQL.getTrabajosJugador(jugador);
        empleos.forEach( (emp) -> {
            lore.add(ChatColor.GOLD + "" + emp.getEmpresa() + " " + ChatColor.GREEN + formatea.format(emp.getSueldo()) + " PC " + ChatColor.GOLD + "/ " + emp.getTipo_sueldo().nombre);
        });

        return ItemBuilder.of(Material.GOLDEN_APPLE).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemEmpresa (String jugador) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER TUS EMPRESAS";

        List<String> lore = new ArrayList<>();
        lore.add("  ");
        List<Empresa> empresas = empresasMySQL.getEmpresasOwner(jugador);
        empresas.forEach( (empresa) -> {
            lore.add(ChatColor.GOLD + "- " + empresa.getNombre() + " ( " + ChatColor.GREEN + formatea.format(empresa.getPixelcoins()) + " PC" +  ChatColor.GOLD + ")");
        });

        return ItemBuilder.of(Material.GOLD_BLOCK).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemBolsa (String jugador) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK VER TUS ACCIONES";

        List<PosicionCerrada> posicionCerradas = posicionesCerradasMySQL.getPosicionesCerradasJugador(jugador);
        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Tus posiciones cerradas:");

        for(int i = 0; i < posicionCerradas.size() || i < 7; i++){
            PosicionCerrada pos = posicionCerradas.get(i);

            if(pos.getRentabilidad() >= 0){
                lore.add(ChatColor.GOLD + "" + pos.getSimbolo() + " -> " + ChatColor.GREEN + pos.getRentabilidadString()
                        + "% : " +  ( (int) ((pos.getCantidad() * pos.getPrecio_apertura()) -  pos.getCantidad() * pos.getPrecio_cierre())) + " PC");
            }else{
                lore.add(ChatColor.GOLD + "" + pos.getSimbolo() + " -> " + ChatColor.RED + pos.getRentabilidadString()
                        + "% : " +  ( (int) ((pos.getCantidad() * pos.getPrecio_apertura()) -  pos.getCantidad() * pos.getPrecio_cierre())) + " PC");
            }
        }

        return ItemBuilder.of(Material.BOOK).title(displayName).lore(lore).build();
    }
    
    private ItemStack buildItemDeudas (String jugador) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER TUS DEUDAS";

        double totalQueLeDeben = deudasMySQL.getDeudasAcredor(jugador).stream().mapToInt(Deuda::getPixelcoins_restantes).sum();
        double totalQueDebe = deudasMySQL.getDeudasDeudor(jugador).stream().mapToInt(Deuda::getPixelcoins_restantes).sum();

        List<String> lore = new ArrayList<String>() {{
            add("    ");
            add(ChatColor.GOLD + "Total que debes: " + ChatColor.GREEN + totalQueDebe + " PC");
            add(ChatColor.GOLD + "Total que te deben: " + ChatColor.GREEN + totalQueLeDeben + " PC");
        }};

        return ItemBuilder.of(Material.DIAMOND_SWORD).title(displayName).lore(lore).build();
    }

    private ItemStack buildItemStats (String nombreJugador) {
        ItemStack stats = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta metaStats = (SkullMeta) stats.getItemMeta();
        metaStats.setOwningPlayer(Bukkit.getPlayer(nombreJugador));
        metaStats.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER EL TOP JUGADORES");

        Jugador jugador = jugadoresMySQL.getJugador(nombreJugador);

        if(jugador == null)
            return stats;

        double totalAhorrado = jugador.getPixelcoins();
        double totalDebe = deudasMySQL.getAllPixelcoinsDeudasDeudor(jugador.getNombre());
        double totalDeben = deudasMySQL.getAllPixelcoinsDeudasAcredor(jugador.getNombre());
        double totalAcciones = posicionesAbiertasMySQL.getAllPixeloinsEnAcciones(jugador.getNombre());
        double totalEmpresas = empresasMySQL.getAllPixelcoinsEnEmpresas(jugador.getNombre());
        double resultado = (totalAhorrado + totalDeben + totalAcciones + totalEmpresas) - totalDebe;

        double gastos = jugador.getGastos();
        double ingresos = jugador.getIngresos();
        double beneficios = ingresos - gastos;
        double rentabilidad;
        if(ingresos == 0){
            rentabilidad = -100;
        }else{
            rentabilidad = Funciones.rentabilidad(ingresos, beneficios);
        }

        int nventas = jugador.getNventas();
        int posTopRicps = jugadoresMySQL.getPosicionTopRicos(nombreJugador);
        int posTopVendedores = jugadoresMySQL.getPosicionTopVendedores(nombreJugador);
        List<String> lore = new ArrayList<>();
        lore.add("  ");
        lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "       TUS ESTADISTICAS");
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Liquidez (ahorrado): " + ChatColor.GREEN + formatea.format(totalAhorrado) + " PC");
        lore.add(ChatColor.GOLD + "Total en empresas: " + ChatColor.GREEN + formatea.format(totalEmpresas) + " PC");
        lore.add(ChatColor.GOLD + "Total en acciones: " + ChatColor.GREEN + formatea.format(totalAcciones) + " PC");
        lore.add(ChatColor.GOLD + "Total que te deben: " + ChatColor.GREEN + formatea.format(totalDeben) + " PC");
        lore.add(ChatColor.GOLD + "Total que debes: " + ChatColor.RED + "-" + formatea.format(totalDebe) + " PC");
        if(beneficios >= 0)
            lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Reultado: " + ChatColor.GREEN + formatea.format(resultado) + " PC");
        else
            lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Reultado: " + ChatColor.RED + "-" + formatea.format(resultado) + " PC");

        lore.add(ChatColor.GOLD + "Posicion top ricos: " + posTopRicps);
        lore.add("    ");

        lore.add(ChatColor.GOLD + "Ingresos: " + ChatColor.GREEN + formatea.format(ingresos) + " PC");
        lore.add(ChatColor.GOLD + "Gastos: " + ChatColor.GREEN + formatea.format(gastos) + " PC");
        if(beneficios >= 0){
            lore.add(ChatColor.GOLD + "Beneficios: " + ChatColor.GREEN + formatea.format(beneficios) + " PC");
            lore.add(ChatColor.GOLD + "Rentabilidad: " + ChatColor.GREEN + "+" + formatea.format((int) rentabilidad) + " %");
        }else{
            lore.add(ChatColor.GOLD + "Beneficios: " + ChatColor.RED + formatea.format(beneficios) + " PC");
            lore.add(ChatColor.GOLD + "Rentabilidad: " + ChatColor.RED + formatea.format((int) rentabilidad) + " %");
        }
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Nº de ventas en la tienda: " + nventas);
        lore.add(ChatColor.GOLD + "Posicion en top vendedores: " + posTopVendedores);
        lore.add("   ");
        Jugador jugadorAVer = jugadoresMySQL.getJugador(nombreJugador);
        lore.add(ChatColor.GOLD + "Numero de veces pagada la deuda: " + jugadorAVer.getNpagos());
        lore.add(ChatColor.GOLD + "Numero de veces de inpago de la deuda: " + ChatColor.RED +  jugadorAVer.getNinpagos());

        metaStats.setLore(lore);
        stats.setItemMeta(metaStats);

        return stats;
    }

    private ItemStack buildItemWEB (String jugador) {
        String displayName = ChatColor.AQUA + "" + ChatColor.BOLD + "      WEB http://serversurvival.ddns.net";
        List<String> lore = new ArrayList<>();
        lore.add("  ");

        Cuenta cuenta = cuentasMySQL.getCuenta(jugador);
        if(cuenta == null){
            int numeroCuenta = jugadoresMySQL.getJugador(jugador).getNumero_cuenta();
            lore.add(ChatColor.DARK_AQUA + "No tienes cuenta, para registrarse:");
            lore.add(ChatColor.DARK_AQUA + "" + ChatColor.UNDERLINE + "http://serversurvival.ddns.net/registrarse");
            lore.add(ChatColor.DARK_AQUA + "Tu numero de cuenta: " + ChatColor.BOLD + numeroCuenta);
        }else{
            lore.add(ChatColor.DARK_AQUA + "Ya tienes cuenta");
            lore.add(ChatColor.DARK_AQUA + "" + ChatColor.UNDERLINE + "http//serversurvival.ddns.net/iniciarsesion");
        }
        lore.add("  ");
        lore.add(ChatColor.DARK_AQUA + "Con la web podras acceder a todas tus estadisticas");
        lore.add(ChatColor.DARK_AQUA + "y comprar acciones, realizar transacciones etc.");

        return ItemBuilder.of(Material.PAPER).title(displayName).lore(lore).build();
    }

    private void rellenarCristales (Inventory inventory) {
        ItemStack cristal = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

        posicionesCristales.forEach( (posicion) -> inventory.setItem(posicion, cristal));
    }
}
