package es.serversurvival.minecraftserver.bolsa.vervalores;

import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.minecraftserver._shared.menus.NumberSelectorMenu;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoBolsaApuesta;
import es.serversurvival.pixelcoins.bolsa.abrir.AbrirPosicoinBolsaParametros;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class SeleccionarCantidadComprarBolsaMenu extends NumberSelectorMenu<SeleccionarCantidadComprarBolsaMenu.SeleccionarCantidadComprarBolsaMenuState> {
    private final UseCaseBus useCaseBus;

    @Override
    public void onAccept(Player player, double cantidad, InventoryClickEvent event) {
        useCaseBus.handle(AbrirPosicoinBolsaParametros.builder()
                        .activoBolsaId(getState().activoBolsa.getActivoBolsaId())
                        .cantidad((int) cantidad)
                        .jugadorId(player.getUniqueId())
                        .tipoApuesta(TipoBolsaApuesta.LARGO)
                .build());
    }

    @Override
    public double maxValue() {
        return (int) getState().pixelcoinsJugador / getState().getPrecioPorUnidad();
    }

    @Override
    public double initialValue() {
        return 1;
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return List.of(
                GOLD + "Comprar: " + cantidad + " " + getState().getNombreUnidadTipoActivo(),
                GOLD + "Precio/Unidad: " + GREEN + Funciones.FORMATEA.format(getState().precioPorUnidad) + " PC",
                GOLD + "Precio/Total: " + GREEN + Funciones.FORMATEA.format(getState().precioPorUnidad * cantidad) + " PC",
                GOLD + "Tus pixelcoins: " + GREEN + Funciones.FORMATEA.format(getState().pixelcoinsJugador) + " PC"
        );
    }

    public static SeleccionarCantidadComprarBolsaMenuState of(ActivoBolsa activoBolsa, double precioProUnidad, double pixelcoinsJugador) {
        return new SeleccionarCantidadComprarBolsaMenuState(activoBolsa, precioProUnidad, pixelcoinsJugador);
    }

    @AllArgsConstructor
    public static class SeleccionarCantidadComprarBolsaMenuState {
        @Getter private final ActivoBolsa activoBolsa;
        @Getter private final double precioPorUnidad;
        @Getter private final double pixelcoinsJugador;

        public String getNombreUnidadTipoActivo() {
            return activoBolsa.getTipoActivo().getNombreUnidadPlural();
        }
    }
}
