package es.serversurvival.empresas.empresas.pagardividendos;

import es.serversurvival._shared.menus.NumberSelectorMenu;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.BOLD;

public final class PagarDividendosConfirmacionMenu extends NumberSelectorMenu {
    private final Empresa empresa;

    public PagarDividendosConfirmacionMenu(Empresa empresa) {
        this.empresa = empresa;
    }
    
    @Override
    public void onAccept(Player player, InventoryClickEvent event) {
        double dividendoPorAccion = super.getPropertyDouble("cantidad");
        (new PagarDividendosEmpresaServerUseCase()).pagar(player.getName(), this.empresa.getNombre(), dividendoPorAccion);
        Funciones.enviarMensajeYSonido(player, GOLD + "Se han pagado todos los dividendos", Sound.ENTITY_PLAYER_LEVELUP);
    }

    @Override
    protected String titulo() {
        return DARK_RED + "" + BOLD + "    REPARTIR DIVIDENDOS";
    }

    @Override
    public double maxValue() {
        return this.empresa.getPixelcoins() / this.empresa.getAccionesTotales();
    }

    @Override
    public double initialValue() {
        return 1;
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return List.of(
                GOLD + "Dividendo/Accion: " + GREEN + Funciones.FORMATEA.format(cantidad),
                GOLD + "Total a pagar: " + GREEN + Funciones.FORMATEA.format(cantidad*this.empresa.getAccionesTotales()),
                GOLD + "Pixelcoins empresa: " + GREEN + Funciones.FORMATEA.format(this.empresa.getPixelcoins())
        );
    }
}
