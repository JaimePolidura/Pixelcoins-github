package es.serversurvival.objetos.mySQL.tablasObjetos;

import es.serversurvival.objetos.mySQL.Empleados;
import es.serversurvival.objetos.mySQL.Empresas;

import java.text.DecimalFormat;

public class Empleado {
    private DecimalFormat formatea = new DecimalFormat("###,###.##");

    private int id;
    private String empleado;
    private String empresa;
    private double sueldo;
    private String cargo;
    private String tipo;
    private String fechaPaga;

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