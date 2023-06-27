package es.serversurvival.minecraftserver.deudas._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BinaryOperator;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@Service
@AllArgsConstructor
public final class DeudaItemLore {
    private final JugadoresService jugadoresService;

    public List<String> buildNuevoPrestamoInfo(UUID acredorJugadorId, double nominal, double interes, long periodoPagoCuotasMs,
                                               int nCuotasTotales) {
        return List.of(
                GOLD + "Acredor: " + jugadoresService.getNombreById(acredorJugadorId),
                GOLD + "Nominal/Pixelcoins: " + GREEN + Funciones.formatNumero(nominal) + " PC",
                GOLD + "Interes: " + FORMATEA.format(interes * 100) + "%",
                GOLD + "Cuotas: " + GREEN + FORMATEA.format(nominal * interes) + " PC " + GOLD + " / " + Funciones.millisToDias(periodoPagoCuotasMs) + " dias",
                GOLD + "Nº Cuotas totales: " + nCuotasTotales,
                GOLD + "Pixelcoins totales a devolver: " + GREEN + FORMATEA.format(nominal * interes * nCuotasTotales + nominal) + " PC"
        );
    }

    public List<String> buildOfertaDeudaMercado(double precio, String vendedorNombre, double interes, double nominal, long periodoPagoCuotasMs,
                                                int nCuotasRestantes, int nCuotasImpagados, double pixelcoinsTotalesDevolver, boolean esMercadoPrimario,
                                                String deudorNombre) {
        List<String> lore = new ArrayList<>(List.of(
                GOLD + "Precio: " + GREEN + FORMATEA.format(precio) + " PC",
                GOLD + "Vendedor: " + vendedorNombre,
                GOLD + "Deudor: " + deudorNombre,
                GOLD + " ",
                GOLD + "Nominal/Pixelcoins: " + GREEN + FORMATEA.format(nominal) + " PC",
                GOLD + "Pagos/Cuotas: " + GREEN + FORMATEA.format(interes * nominal) + " PC / " + GOLD + Funciones.millisToDias(periodoPagoCuotasMs) + " dias",
                GOLD + "Interes: " + FORMATEA.format(interes * 100) + "%",
                GOLD + "Nº Pagos restantes: " + nCuotasRestantes,
                GOLD + "Nº Pagos impagados: " + (nCuotasImpagados > 0 ? RED : GOLD) + " " + nCuotasImpagados,
                GOLD + "Total pixelcoins a devolver: " + GREEN + FORMATEA.format(pixelcoinsTotalesDevolver) + " PC"
        ));
        if(esMercadoPrimario){
            lore.add(GOLD + "Mercado primario");
        }

        return lore;
    }

    public List<String> buildDescDeuda(Deuda deuda) {
        return List.of(
                GOLD + "Acredor: " + jugadoresService.getNombreById(deuda.getAcredorJugadorId()),
                GOLD + "Deudor: " + jugadoresService.getNombreById(deuda.getDeudorJugadorId()),
                GOLD + "Nomimal/Pixelcoins: " + GREEN + FORMATEA.format(deuda.getNominal()) + " PC",
                GOLD + "Interes: " + FORMATEA.format(deuda.getInteres() * 100) + "%",
                GOLD + "Cuotas: " + GREEN + FORMATEA.format(deuda.getCuota()) + " PC " + GOLD + " / " + Funciones.millisToDias(deuda.getPeriodoPagoCuotaMs()) + " dias",
                GOLD + "Pixelcoins restantes: " + GREEN + FORMATEA.format(deuda.getPixelcoinsRestantesDePagar()) + " PC",
                " ",
                GOLD + "Nº Cuotas restantes: " + (deuda.getNCuotasTotales() - deuda.getNCuotasPagadas()),
                GOLD + "Nº Cuotas pagadas: " + deuda.getNCuotasPagadas(),
                GOLD + "Nº Cuotas impagadas: " + deuda.getNCuotasImpagadas(),
                "   ",
                String.valueOf(deuda.getDeudaId())
        );
    }
}
