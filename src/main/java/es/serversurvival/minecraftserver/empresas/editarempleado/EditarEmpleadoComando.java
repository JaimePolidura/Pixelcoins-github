package es.serversurvival.minecraftserver.empresas.editarempleado;

import lombok.Getter;

public final class EditarEmpleadoComando {
    @Getter private String empresa;
    @Getter private String empleado;
    @Getter private String queSeEdita;
    @Getter private String nuevoValor;

    public int nuevoValorToInt() {
        return Integer.parseInt(nuevoValor);
    }

    public long nuevoValorToLong() {
        return Long.parseLong(nuevoValor);
    }

    public double nuevoValorToDouble() {
        return Double.parseDouble(nuevoValor);
    }
}
