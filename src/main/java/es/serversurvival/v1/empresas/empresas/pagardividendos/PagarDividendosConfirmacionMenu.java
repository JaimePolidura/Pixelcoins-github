package es.serversurvival.v1.empresas.empresas.pagardividendos;

import es.serversurvival.v1._shared.menus.NumberSelectorMenu;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.BOLD;

@AllArgsConstructor
public final class PagarDividendosConfirmacionMenu extends NumberSelectorMenu<Empresa> {
    private final PagarDividendosEmpresaServerUseCase pagarDividendosEmpresaServerUseCase;
    private final EnviadorMensajes enviadorMensajes;

    @Override
    public void onAccept(Player player, double dividendoPorAccion, InventoryClickEvent event) {
        this.pagarDividendosEmpresaServerUseCase.pagar(player.getName(), this.getState().getNombre(), dividendoPorAccion);
        enviadorMensajes.enviarMensajeYSonido(player, GOLD + "Se han pagado todos los dividendos", Sound.ENTITY_PLAYER_LEVELUP);
    }

    @Override
    protected String titulo() {
        return DARK_RED + "" + BOLD + "    REPARTIR DIVIDENDOS";
    }

    @Override
    public double maxValue() {
        return this.getState().getPixelcoins() / this.getState().getAccionesTotales();
    }

    @Override
    public double initialValue() {
        return 1;
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return List.of(
                GOLD + "Dividendo/Accion: " + GREEN + Funciones.FORMATEA.format(cantidad),
                GOLD + "Total a pagar: " + GREEN + Funciones.FORMATEA.format(cantidad*this.getState().getAccionesTotales()),
                GOLD + "Pixelcoins empresa: " + GREEN + Funciones.FORMATEA.format(this.getState().getPixelcoins())
        );
    }
}
