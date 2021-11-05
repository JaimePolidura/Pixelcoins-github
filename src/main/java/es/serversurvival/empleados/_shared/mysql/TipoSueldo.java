package es.serversurvival.empleados._shared.mysql;

import java.util.Arrays;

public enum TipoSueldo {
    DIA("d", "dia", 1),
    SEMANA("s", "semana", 7),
    SEMANA_2("2s", "2 semanas", 14),
    MES("m", "mes", 30);

    public final String codigo;
    public final String nombre;
    public final int dias;

    TipoSueldo(String codigo, String nombre, int dias) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.dias = dias;
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

    public static boolean codigoCorrecto (String codigo) {
        return Arrays.stream(TipoSueldo.values()).anyMatch(t -> t.codigo.equals(codigo));
    }

    public static boolean dentroDeLosDias (String codigo, int dias) {
        return TipoSueldo.ofCodigo(codigo).dias >= dias;
    }
}
