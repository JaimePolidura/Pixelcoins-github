package es.serversurvival.empresas._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import es.serversurvival._shared.mysql.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.UUID;

@AllArgsConstructor
public final class Empresa extends Aggregate implements TablaObjeto {
    @Getter private final UUID empresaId;
    @Getter private final String nombre;
    @Getter private final String owner;
    @Getter private final double pixelcoins;
    @Getter private final double ingresos;
    @Getter private final double gastos;
    @Getter private final String icono;
    @Getter private final String descripcion;
    @Getter private final boolean cotizada;

    public Empresa withOwner(String owner){
        return new Empresa(empresaId, nombre, owner, pixelcoins, ingresos,
                gastos, icono, descripcion, cotizada);
    }

    public Empresa incrementPixelcoinsBy(double pixelcoinsToIncrement){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins + pixelcoinsToIncrement, ingresos,
                gastos, icono, descripcion, cotizada);
    }

    public Empresa decrementPixelcoinsBy(double pixelcoinsToDecrement){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins - pixelcoinsToDecrement, ingresos,
                gastos, icono, descripcion, cotizada);
    }

    public Empresa setCotizadaToTrue(){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins, this.ingresos,
                gastos, icono, descripcion, true);
    }

    public Empresa withDescripccion(String desc){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins, this.ingresos,
                gastos, icono, desc, cotizada);
    }

    public Empresa withNombre(String nombre){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins, this.ingresos,
                gastos, icono, descripcion, cotizada);
    }

    public Empresa incrementIngresosBy(double ingresosToIncrement){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins, this.ingresos - ingresosToIncrement,
                gastos, icono, descripcion, cotizada);
    }

    public Empresa incrementGastosBy(double gastosToIncrement){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins, this.ingresos,
                gastos + gastosToIncrement, icono, descripcion, cotizada);
    }

    public Empresa withIcono(Material material){
        return new Empresa(empresaId, nombre, owner, this.pixelcoins, this.ingresos,
                gastos, material.toString(), descripcion, cotizada);
    }
}
