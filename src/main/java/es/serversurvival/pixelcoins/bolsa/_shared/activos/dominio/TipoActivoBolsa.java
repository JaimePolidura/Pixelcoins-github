package es.serversurvival.pixelcoins.bolsa._shared.activos.dominio;

import es.serversurvival.pixelcoins.bolsa._shared.activos.infraestroctura.AccionesBolsaInformationAPIService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.infraestroctura.CriptoMonedasBolsaInformationAPIService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.infraestroctura.MateriasPrimasBolsaInfomationAPIService;
import lombok.Getter;
import org.bukkit.Material;

public enum TipoActivoBolsa {
    CRIPTOMONEDAS(CriptoMonedasBolsaInformationAPIService.class, "criptomonedas", Material.GOLD_INGOT),
    MATERIA_PRIMA(MateriasPrimasBolsaInfomationAPIService.class, "unidades", Material.CHARCOAL),
    ACCION(AccionesBolsaInformationAPIService.class, "acciones", Material.BOOK);

    @Getter private final Class<? extends ActivoBolsaInformationAPIService> activoInfoService;
    @Getter private final String nombreUnidad;
    @Getter private final Material material;

    TipoActivoBolsa(Class<? extends ActivoBolsaInformationAPIService> activoInfoService, String nombreUnidad, Material material) {
        this.activoInfoService = activoInfoService;
        this.nombreUnidad = nombreUnidad;
        this.material = material;
    }
}
