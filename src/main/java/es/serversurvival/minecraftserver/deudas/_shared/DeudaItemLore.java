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

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@Service
@AllArgsConstructor
public final class DeudaItemLore {
    private final JugadoresService jugadoresService;

    public List<String> buildNuevoPrestamoInfo(UUID acredorJugadorId, double nominal, double interes, long periodoPagoCuotasMs,
                                               int nCuotasTotales) {
        return List.of(
                GOLD + "Acredor: " + jugadoresService.getNombreById(acredorJugadorId),
                GOLD + "Nominal/Pixelcoins: " + formatPixelcoins(nominal),
                GOLD + "Interes: " + formatPorcentaje(interes),
                GOLD + "Cuotas: " + formatPixelcoins(nominal * interes) + " / " + Funciones.millisToDias(periodoPagoCuotasMs) + " dias",
                GOLD + "Nº Cuotas totales: " + nCuotasTotales,
                GOLD + "Pixelcoins totales a devolver: " + formatPixelcoins(nominal * interes * nCuotasTotales + nominal)
        );
    }

    public List<String> buildOfertaDeudaMercado(double precio, String vendedorNombre, double interes, double nominal, long periodoPagoCuotasMs,
                                                int nCuotasRestantes, int nCuotasImpagados, double pixelcoinsTotalesDevolver, boolean esMercadoPrimario,
                                                String deudorNombre) {
        List<String> lore = new ArrayList<>(List.of(
                GOLD + "Precio: " + formatPixelcoins(precio),
                GOLD + "Vendedor: " + vendedorNombre,
                GOLD + "Deudor: " + deudorNombre,
                GOLD + " ",
                GOLD + "Nominal/Pixelcoins: " + formatPixelcoins(nominal),
                GOLD + "Pagos/Cuotas: " + formatPixelcoins(interes * nominal) + "/ " + Funciones.millisToDias(periodoPagoCuotasMs) + " dias",
                GOLD + "Interes: " + formatPorcentaje(interes),
                GOLD + "Nº Pagos restantes: " + nCuotasRestantes,
                GOLD + "Nº Pagos impagados: " + (nCuotasImpagados > 0 ? RED : GOLD) + " " + nCuotasImpagados,
                GOLD + "Total pixelcoins a devolver: " + formatPixelcoins(pixelcoinsTotalesDevolver)
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
                GOLD + "Nomimal/Pixelcoins: " + formatPixelcoins(deuda.getNominal()),
                GOLD + "Interes: " + formatPorcentaje(deuda.getInteres()),
                GOLD + "Cuotas: " + formatPixelcoins(deuda.getCuota()) + "/ " + Funciones.millisToDias(deuda.getPeriodoPagoCuotaMs()) + " dias",
                GOLD + "Pixelcoins restantes: " + formatPixelcoins(deuda.getPixelcoinsRestantesDePagar()),
                " ",
                GOLD + "Nº Cuotas restantes: " + (deuda.getNCuotasTotales() - deuda.getNCuotasPagadas()),
                GOLD + "Nº Cuotas pagadas: " + deuda.getNCuotasPagadas(),
                GOLD + "Nº Cuotas impagadas: " + deuda.getNCuotasImpagadas(),
                "   ",
                String.valueOf(deuda.getDeudaId())
        );
    }
}
