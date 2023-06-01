package es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio;

import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.infraestroctura.AccionesBolsaInformationAPIService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.infraestroctura.CriptoMonedasBolsaInformationAPIService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.infraestroctura.MateriasPrimasBolsaInfomationAPIService;
import lombok.Getter;

public enum TipoActivoBolsa {
    ACCION(AccionesBolsaInformationAPIService.class, "acciones"),
    MATERIA_PRIMA(MateriasPrimasBolsaInfomationAPIService.class, "unidades"),
    CRIPTOMONEDAS(CriptoMonedasBolsaInformationAPIService.class, "criptomonedas");

    @Getter private final Class<? extends ActivoBolsaInformationAPIService> activoInfoService;
    @Getter private final String alias;

    TipoActivoBolsa(Class<? extends ActivoBolsaInformationAPIService> activoInfoService, String alias) {
        this.activoInfoService = activoInfoService;
        this.alias = alias;
    }
}
