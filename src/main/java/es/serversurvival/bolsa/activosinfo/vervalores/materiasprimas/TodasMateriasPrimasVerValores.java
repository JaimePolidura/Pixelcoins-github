package es.serversurvival.bolsa.activosinfo.vervalores.materiasprimas;

import java.util.Map;

public final class TodasMateriasPrimasVerValores {
    public static final Map<String, String> MATERIAS_PRIMAS;

    static {
        MATERIAS_PRIMAS = Map.of(
                "DCOILBRENTEU", "Petroleo (Brent)",
                "DHHNGSP", "Gas natural",
                "DJFUELUSGULF", "Queroseno (Combustible aviones)",
                "GASDESW","Diesel"
        );
    }
}
