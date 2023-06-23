package es.serversurvival.pixelcoins.bolsa._shared.activos.dominio;

import es.serversurvival.pixelcoins.bolsa._shared.activos.infrastructure.AccionesBolsaInformationAPIService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.infrastructure.CriptoMonedasBolsaInformationAPIService;
import lombok.Getter;
import org.bukkit.Material;

public enum TipoActivoBolsa {
    CRIPTOMONEDAS(CriptoMonedasBolsaInformationAPIService.class, "criptomonedas", "Moneda", Material.GOLD_INGOT),
    ACCION(AccionesBolsaInformationAPIService.class, "acciones", "Accion", Material.BOOK);

    @Getter private final Class<? extends ActivoBolsaInformationAPIService> activoInfoService;
    @Getter private final String nombreUnidadPlural;
    @Getter private final String nombreUnidadSingular;
    @Getter private final Material material;

    TipoActivoBolsa(Class<? extends ActivoBolsaInformationAPIService> activoInfoService, String nombreUnidad, String nombreUnidadSingular, Material material) {
        this.activoInfoService = activoInfoService;
        this.nombreUnidadPlural = nombreUnidad;
        this.nombreUnidadSingular = nombreUnidadSingular;
        this.material = material;
    }
}
