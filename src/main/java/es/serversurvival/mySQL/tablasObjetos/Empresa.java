package es.serversurvival.mySQL.tablasObjetos;

public final class Empresa implements TablaObjeto {
    private final int id;
    private final String nombre;
    private final String owner;
    private final double pixelcoins;
    private final double ingresos;
    private final double gastos;
    private final String icono;
    private final String descripcion;

    public Empresa(int id, String nombre, String owner, double pixelcoins, double ingresos, double gastos, String icono, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.owner = owner;
        this.pixelcoins = pixelcoins;
        this.ingresos = ingresos;
        this.gastos = gastos;
        this.icono = icono;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getOwner() {
        return owner;
    }

    public double getPixelcoins() {
        return pixelcoins;
    }

    public double getIngresos() {
        return ingresos;
    }

    public double getGastos() {
        return gastos;
    }

    public String getIcono() {
        return icono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPixelcoinsFormateado(){
        return formatea.format(pixelcoins);
    }

    public String getIngresosFormateado(){
        return formatea.format(ingresos);
    }

    public String getGastosFormateado(){
        return formatea.format(gastos);
    }

    public String getBeneficiosFormateado(){
        return formatea.format( ingresos - gastos );
    }
}
