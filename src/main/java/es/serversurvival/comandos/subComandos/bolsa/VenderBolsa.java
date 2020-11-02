package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.util.Funciones;
import es.serversurvival.apiHttp.IEXCloud_API;
import es.serversurvival.mySQL.Transacciones;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class VenderBolsa extends BolsaSubCommand {
    private final String SCNombre = "vender";
    private final String sintaxis = "/bolsa vender <id> [numero a vender]";
    private final String ayuda = "Vender acciones, bitcoin, barriles etc con una id que se ven en /bolsa cartera y un numero de acciones a vender";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player jugadorPlayer, String[] args) {
        if (args.length != 3 && args.length != 2) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        if (!Funciones.esInteger(args[1])) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "En la id y en el numero de unidades a vender necesitas meter numeros que no sean texto ni decimales");
            return;
        }
        if (args.length == 3 && !Funciones.esInteger(args[2])) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Elnumero de unidades a vender necesitas meter numeros que no sean texto ni decimales");
            return;
        }
        int id = Integer.parseInt(args[1]);

        if (args.length == 3) {
            int cantidad = Integer.parseInt(args[2]);
            if (cantidad <= 0) {
                jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Necesitas introducir numeros que no sean negativos o que sean cero");
                return;
            }
        }
        if (id < 0) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "La id ha de ser positiva");
            return;
        }

        posicionesAbiertasMySQL.conectar();
        PosicionAbierta posicionAVender = posicionesAbiertasMySQL.getPosicionAbierta(id);
        if (posicionAVender == null) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No existe esa id, para verlas /bolsa cartera");
            posicionesAbiertasMySQL.desconectar();
            return;
        }
        if (!posicionAVender.getJugador().equalsIgnoreCase(jugadorPlayer.getName())) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No tienes cantidad invertidas en cartera con esa ID, para ver las ids: /bolsa cartera");
            posicionesAbiertasMySQL.desconectar();
            return;
        }

        int cantidad;
        if (args.length == 2) {
            cantidad = posicionAVender.getCantidad();
        } else {
            cantidad = Integer.parseInt(args[2]);
        }
        int cantidadEnID = posicionAVender.getCantidad();
        if (cantidadEnID < cantidad) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No puedes vender mas cantidad de las que tienes en cartera");
            posicionesAbiertasMySQL.desconectar();
            return;
        }

        String nombre = posicionAVender.getNombre();
        String tipo = posicionAVender.getTipo();

        transaccionesMySQL.venderPosicion(posicionAVender ,cantidad ,jugadorPlayer);
        posicionesAbiertasMySQL.desconectar();
    }

    private double getPrecio(String tipo, String simbolo) throws Exception {
        double precio;

        switch (tipo) {
            case "ACCIONES":
                precio = IEXCloud_API.getOnlyPrice(simbolo);
                break;
            case "CRIPTOMONEDAS":
                precio = IEXCloud_API.getPrecioCriptomoneda(simbolo);
                break;
            case "MATERIAS_PRIMAS":
                precio = IEXCloud_API.getPrecioMateriaPrima(simbolo);
                break;
            default:
                precio = -1;
                break;
        }
        return precio;
    }
}