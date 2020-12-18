package es.serversurvival.mySQL.tablasObjetos;

import es.serversurvival.mySQL.Empleados;

public final class Empleado implements TablaObjeto{
    private final int id;
    private final String jugador;
    private final String empresa;
    private final double sueldo;
    private final String cargo;
    private final String tipo_sueldo;
    private final String fecha_ultimapaga;

    public Empleado(int id, String jugador, String empresa, double sueldo, String cargo, String tipo_sueldo, String fecha_ultimapaga) {
        this.id = id;
        this.jugador = jugador;
        this.empresa = empresa;
        this.sueldo = sueldo;
        this.cargo = cargo;
        this.tipo_sueldo = tipo_sueldo;
        this.fecha_ultimapaga = fecha_ultimapaga;
    }

    public int getId() {
        return id;
    }

    public String getJugador() {
        return jugador;
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

    public String getTipo_sueldo() {
        return tipo_sueldo;
    }

    public String getFecha_ultimapaga() {
        return fecha_ultimapaga;
    }

    public String getSueldoFormateado(){
        return formatea.format(sueldo);
    }

    public String getFrequenciaPago(){
        return Empleados.toStringTipoSueldo(tipo_sueldo);
    }
}
