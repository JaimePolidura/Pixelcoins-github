package es.serversurvival.legacy.mySQL.enums;

import es.serversurvival.legacy.apiHttp.IEXCloud_API;
import es.serversurvival.legacy.mySQL.tablasObjetos.PosicionAbierta;
import org.bukkit.Material;


public enum TipoActivo {
    ACCIONES(Material.NAME_TAG, "acciones") {
        @Override
        public double getPrecio(String simbolo) {
            try {
                return IEXCloud_API.getOnlyPrice(simbolo);
            } catch (Exception e) {
                return -1;
            }
        }
    },
    CRIPTOMONEDAS(Material.GOLD_BLOCK, "monedas"){
        @Override
        public double getPrecio(String simbolo) {
            try {
                return IEXCloud_API.getPrecioCriptomoneda(simbolo);
            } catch (Exception e) {
                return -1;
            }
        }
    },
    MATERIAS_PRIMAS(Material.COAL, "unidades"){
        @Override
        public double getPrecio(String simbolo) {
            try {
                return IEXCloud_API.getPrecioMateriaPrima(simbolo);
            } catch (Exception e) {
                return -1;
            }
        }
    },
    ACCIONES_SERVER(Material.GREEN_BANNER, "acciones"){
        @Override
        public double getPrecio(String simbolo) {
            return 0;
        }
    };

    public abstract double getPrecio (String simbolo);

    private final Material materialDisplay;
    private final String alias;

    TipoActivo(Material materialDisplay, String alias) {
        this.materialDisplay = materialDisplay;
        this.alias = alias;
    }

    public Material getMaterialDisplay() {
        return materialDisplay;
    }

    public String getAlias() {
        return alias;
    }

    public String getAliasUpperFirst() {
        return alias.substring(0, 1).toUpperCase() + alias.substring(1);
    }

    public static Material getMaterialFor (PosicionAbierta posicion) {
        return posicion.getTipo_posicion() == TipoPosicion.CORTO ?
                Material.REDSTONE_TORCH :
                posicion.getTipo_activo().materialDisplay;
    }
}
