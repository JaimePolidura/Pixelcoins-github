package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.main.Pixelcoin;
import es.serversurvival.objetos.apiHttp.IEXCloud_API;
import es.serversurvival.objetos.mySQL.LlamadasApi;
import es.serversurvival.objetos.mySQL.PosicionesAbiertas;
import es.serversurvival.objetos.mySQL.Transacciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class VenderBolsaSubComando extends BolsaSubCommand {
    private final String SCNombre = "vender";
    private final String sintaxis = "/bolsa vender <id> [numero a vender]";
    private final String ayuda = "Vender acciones, bitcoin, barriles etc con una id que se ven en /bolsa cartera y un numero de acciones a vender";
    private int aVender;

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        if (args.length != 3 && args.length != 2) {
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        int id;
        int cantidad = 0;

        try {
            id = Integer.parseInt(args[1]);
            if(args.length == 3){
                cantidad = Integer.parseInt(args[2]);
            }
        } catch (Exception e) {
            player.sendMessage(ChatColor.DARK_RED + "En la id y en el numero de unidades a vender necesitas meter numeros que no sean texto ni decimales");
            return;
        }
        PosicionesAbiertas posicionesAbiertas = new PosicionesAbiertas();
        posicionesAbiertas.conectar();
        if(args.length == 2){
            cantidad = posicionesAbiertas.getCantidad(id);
        }
        if (id <= 0 || cantidad <= 0) {
            player.sendMessage(ChatColor.DARK_RED + "Necesitas introducir numeros que no sean negativos o que sean cero");
            posicionesAbiertas.desconectar();
            return;
        }
        boolean existe;
        existe = posicionesAbiertas.existe(id);
        if (!existe) {
            player.sendMessage(ChatColor.DARK_RED + "No existe esa id, para verlas /bolsa cartera");
            posicionesAbiertas.desconectar();
            return;
        }
        if (!posicionesAbiertas.getJugador(id).equalsIgnoreCase(player.getName())) {
            player.sendMessage(ChatColor.DARK_RED + "No tienes cantidad invertidas en cartera con esa ID, para ver las ids: /bolsa cartera");
            posicionesAbiertas.desconectar();
            return;
        }
        int cantidadEnID = posicionesAbiertas.getCantidad(id);
        if (cantidadEnID < cantidad) {
            player.sendMessage(ChatColor.DARK_RED + "No puedes vender mas cantidad de las que tienes en cartera");
            posicionesAbiertas.desconectar();
            return;
        }
        aVender = cantidad;
        String nombre = posicionesAbiertas.getNombre(id);
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            String tipo = posicionesAbiertas.getTipo(id);
            double precio = 0;
            try {
                switch (tipo){
                    case "ACCIONES":
                        precio = IEXCloud_API.getOnlyPrice(nombre);
                        break;
                    case "CRIPTOMONEDAS":
                        precio = IEXCloud_API.getPrecioCriptomoneda(nombre);
                        break;
                    case "MATERIAS_PRIMAS":
                        precio = IEXCloud_API.getPrecioMateriaPrima(nombre);
                        break;
                }
                Transacciones t = new Transacciones();
                t.venderPosicion(precio, aVender, id, player);
                posicionesAbiertas.desconectar();

                LlamadasApi llamadasApi = new LlamadasApi();
                llamadasApi.conectar();
                llamadasApi.borrarLlamada(nombre);
                llamadasApi.desconectar();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0L);
    }
}