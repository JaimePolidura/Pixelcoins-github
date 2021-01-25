package es.serversurvival.mySQL.tablasObjetos;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Deuda implements TablaObjeto{
    @Getter private final int id;
    @Getter private final String deudor;
    @Getter private final String acredor;
    @Getter private final int pixelcoins_restantes;
    @Getter private final int tiempo_restante;
    @Getter private final int interes;
    @Getter private final int couta;
    @Getter private final String fecha_ultimapaga;
}
