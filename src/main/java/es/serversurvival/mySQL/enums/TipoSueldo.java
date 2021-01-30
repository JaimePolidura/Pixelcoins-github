package es.serversurvival.mySQL.enums;

import java.util.Arrays;

public enum TipoSueldo {
    DIA("d", "dia"),
    SEMANA("s", "semana"),
    SEMANA_2("2s", "2 semanas"),
    MES("m", "mes");

    public final String codigo;
    public final String nombre;

    TipoSueldo(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return this.codigo;
    }

    public static TipoSueldo ofCodigo (String codigo) {
        return Arrays.stream(TipoSueldo.values())
                .filter(tipoSueldo -> tipoSueldo.codigo.equals(codigo))
                .findAny()
                .get();
    }
}
