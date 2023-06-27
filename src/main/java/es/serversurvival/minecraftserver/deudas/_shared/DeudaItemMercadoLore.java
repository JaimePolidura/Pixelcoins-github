package es.serversurvival.minecraftserver.deudas._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@Service
@AllArgsConstructor
public final class DeudaItemMercadoLore {
    private final JugadoresService jugadoresService;

    public List<String> buildNuevoPrestamoInfo(UUID acredorJugadorId, double nominal, double interes, long periodoPagoCuotasMs,
                                               int nCuotasTotales) {
        System.out.println(interes);

        return List.of(
                GOLD + "Acredor: " + jugadoresService.getNombreById(acredorJugadorId),
                GOLD + "Pagos/Cuotas: " + GREEN + FORMATEA.format(nominal * interes) + " PC " + GOLD + " / " + Funciones.millisToDias(periodoPagoCuotasMs) + " dias",
                GOLD + "Nº Cuotas: " + nCuotasTotales,
                GOLD + "Pixelcoins totales a devolver: " + GREEN + FORMATEA.format(nominal * interes * nCuotasTotales + nominal) + " PC",
                " ",
                GOLD + "Reembolso final/Nomina: " + GREEN + FORMATEA.format(nominal) + " PC",
                GOLD + "Interes: " + FORMATEA.format(interes * 100) + "%"
        );
    }

    public List<String> buildOfertaDeudaMercado(double precio, String vendedorNombre, double interes, double nominal, long periodoPagoCuotasMs,
                                                int nCuotasRestantes, int nCuotasImpagados, double pixelcoinsTotalesDevolver) {
        return List.of(
                GOLD + "Precio: " + GREEN + FORMATEA.format(precio) + " PC",
                GOLD + "Vendedor: " + vendedorNombre,
                GOLD + " ",
                GOLD + "Pagos/Cuotas: " + GREEN + FORMATEA.format(interes * nominal) + " PC",
                GOLD + "Interes: " + FORMATEA.format(interes * 100) + "%",
                GOLD + "Reembolso final/Nominal: " + GREEN + FORMATEA.format(nominal) + " PC",
                GOLD + "Pagaos cada: " + Funciones.millisToDias(periodoPagoCuotasMs) + " dias",
                GOLD + "Nº Pagos restantes: " + nCuotasRestantes,
                GOLD + "Nº Pagos impagados: " + (nCuotasImpagados > 0 ? RED : GOLD) + " " + nCuotasImpagados,
                GOLD + "Total pixelcoins a devolver: " + GREEN + FORMATEA.format(pixelcoinsTotalesDevolver) + " PC"
        );
    }

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
                GOLD + "Reembolso final/Nomina: " + GREEN + FORMATEA.format(deuda.getNominal()) + " PC",
                GOLD + "Interes: " + FORMATEA.format(deuda.getInteres() * 100) + "%",
                "   ",
                String.valueOf(deuda.getDeudaId())
        );
    }
}
