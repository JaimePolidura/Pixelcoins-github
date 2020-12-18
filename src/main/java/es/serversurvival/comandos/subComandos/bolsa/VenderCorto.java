package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.apiHttp.IEXCloud_API;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class VenderCorto extends BolsaSubCommand{
    private final String scnombre = "vendercorto";
    private final String sintaxis = "/bolsa vendercorto <ticker> <nº acciones>";
    private final String ayuda = "Vender una accion para despues recomprarla. El jugador se le reembolsara la diferencia de precio, es decir ganara cuando el precio de la acicon baje. Se te cobra un 5% del valor total de la venta (precioPorAccion * cantidadDeAcciones) sobre tus ahorros";

    @Override
    public String getSCNombre() {
        return scnombre;
    }

    @Override
    public String getSintaxis() {
        return sintaxis;
    }

    @Override
    public String getAyuda() {
        return ayuda;
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args.length != 3){
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + sintaxis);
            return;
        }
        if(!Funciones.esInteger(args[2])){
            player.sendMessage(ChatColor.DARK_RED + "El numero de acciones a vender a de ser un numero natural");
            return;
        }
        int numeroAccionesAVender = Integer.parseInt(args[2]);
        if(numeroAccionesAVender <= 0){
            player.sendMessage(ChatColor.DARK_RED + "El numero de acciones a vender ha de ser positivo y no cero");
            return;
        }
        String tiker = args[1];

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            MySQL.conectar();
            String nombreValor;
            double precioAccion;

            if(llamadasApiMySQL.estaReg(tiker)){
                precioAccion = llamadasApiMySQL.getLlamadaAPI(tiker).getPrecio();
                nombreValor = llamadasApiMySQL.getLlamadaAPI(tiker).getNombre_activo();
            }else{
                try{
                    precioAccion = IEXCloud_API.getOnlyPrice(tiker);
                    nombreValor = IEXCloud_API.getNombreEmpresa(tiker);

                    nombreValor = Funciones.quitarCaracteres(nombreValor, '.', ',');
                    nombreValor = Funciones.quitarPalabrasEntreEspacios(nombreValor, "group", "inc", "co", "corp");
                }catch (Exception e) {
                    player.sendMessage(ChatColor.DARK_RED + "El nombre que has puesto no existe. Para consultar los tickers: /bolsa valores o en internet");
                    MySQL.desconectar();
                    return;
                }
            }
            transaccionesMySQL.venderEnCortoBolsa(player, tiker, nombreValor, numeroAccionesAVender, precioAccion);
            MySQL.desconectar();
        }, 0L);

    }
}
