package es.serversurvival.v2.minecraftserver.deudas._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v2.pixelcoins.deudas._shared.Deuda;
import es.serversurvival.v2.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;

import java.util.List;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

@Service
@AllArgsConstructor
public final class DeudaItemMercadoLore {
    private final JugadoresService jugadoresService;

    public List<String> build(Deuda deuda) {
        return List.of(
                GOLD + "Acredor: " + jugadoresService.getNombreById(deuda.getAcredorJugadorId()),
                GOLD + "Deudor: " + jugadoresService.getNombreById(deuda.getDeudorJugadorId()),
                GOLD + "Cuotas: " + GREEN + FORMATEA.format(deuda.getCuota()) + " PC " + GOLD + " / " + Funciones.millisToDias(deuda.getPeriodoPagoCuotaMs()) + " dias",
                GOLD + "Pixelcoins restantes: " + GREEN + FORMATEA.format(deuda.getPixelcoinsRestantesDePagar()) + " PC",
                " ",
                GOLD + "Nº Cuotas restantes: " + (deuda.getNCuotasTotales() - deuda.getNCuotasPagadas()),
                GOLD + "Nº Cuotas pagadas: " + deuda.getNCuotasPagadas(),
                GOLD + "Nº Cuotas impagadas: " + deuda.getNCuotasImpagadas(),
                GOLD + "Interes: " + FORMATEA.format(deuda.getInteres() * 100) + "%",
                GOLD + "Nominal: " + GREEN + FORMATEA.format(deuda.getNominal()) + " PC",
                "   ",
                String.valueOf(deuda.getDeudaId())
        );
    }
}
