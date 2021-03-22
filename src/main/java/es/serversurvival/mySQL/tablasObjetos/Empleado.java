package es.serversurvival.mySQL.tablasObjetos;

import es.serversurvival.mySQL.Empleados;

import java.text.DecimalFormat;

public final class Empleado implements TablaObjeto{
    private final int id;
    private final String empleado;
    private final String empresa;
    private final double sueldo;
    private final String cargo;
    private final String tipo;
    private final String fechaPaga;

    public Empleado(int id, String empleado, String empresa, double sueldo, String cargo, String tipo, String fechaPaga) {
        this.id = id;
        this.empleado = empleado;
        this.empresa = empresa;
        this.sueldo = sueldo;
        this.cargo = cargo;
        this.tipo = tipo;
        this.fechaPaga = fechaPaga;
    }

    public int getId() {
        return id;
    }

    public String getEmpleado() {
        return empleado;
    }

    public String getEmpresa() {
        return empresa;
    }

    public double getSueldo() {
        return sueldo;
    }

    public String getCargo() {
        return cargo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getFechaPaga() {
        return fechaPaga;
    }

    public String getSueldoFormateado(){
        return formatea.format(sueldo);
    }

    public String getFrequenciaPago(){
        return Empleados.toStringTipoSueldo(tipo);
    }
}