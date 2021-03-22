package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.LlamadasApi;
import es.serversurvival.objetos.mySQL.PosicionesAbiertas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CarteraBolsaSubComando extends BolsaSubCommand {
    private final String SCNombre = "cartera";
    private final String sintaxis = "/bolsa cartera";
    private final String ayuda = "ver todas las acciones que tienes en cartera";

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
        PosicionesAbiertas posicionesAbiertas = new PosicionesAbiertas();
        LlamadasApi llamadasApi = new LlamadasApi();
        posicionesAbiertas.conectar();

        player.sendMessage(ChatColor.GOLD + "---------------------------------------------------");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "                  TU CARTERA DE ACCIONES");

        posicionesAbiertas.getPosicionesJugador(player.getName()).forEach((posicionAbierta) -> {
            String simbolo = posicionAbierta.getNombre();
            String tipo = posicionAbierta.getTipo();
            int cantidad = posicionAbierta.getCantidad();
            double precioInicial = posicionAbierta.getPrecioApertura();
            double precioCotizado = llamadasApi.getPrecio(simbolo);

            double revalorizacion = (precioCotizado * cantidad) - (precioInicial * cantidad);
            double diferenciaPor = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioInicial, precioCotizado), 2);


            if (diferenciaPor > 0) {
                player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "ID: " + posicionAbierta.getId() + ": " + ChatColor.GOLD + tipo.toLowerCase() + " -> " +   simbolo + " Compraste " +
                        cantidad + " unidades (acciones, barriles lo que sea xd) a " + ChatColor.GREEN + formatea.format(precioInicial) + " PC/Unidad "
                        + ChatColor.GOLD + "cotiza a " + ChatColor.GREEN + formatea.format(precioInicial) + " PC/Unidad " + ChatColor.GOLD + " -> " + ChatColor.GREEN + "+" + formatea.format(diferenciaPor) + "% "
                        + ChatColor.GREEN + " : " + formatea.format(revalorizacion) + " PC" + ChatColor.GOLD + " de " + ChatColor.GREEN + formatea.format(precioInicial * cantidad) + " PC " + ChatColor.GOLD + " invertidas en el primer momento");
            } else {
                player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "ID:" + posicionAbierta.getId() + ": " + ChatColor.GOLD + tipo.toLowerCase() + " -> " + simbolo + " Compraste " +
                        cantidad + " unidades (acciones, barriles lo que sea xd) a " + ChatColor.GREEN + formatea.format(precioInicial) + " PC/Accion "
                        + ChatColor.GOLD + "cotiza a " + ChatColor.GREEN + formatea.format(precioInicial) + " PC/Unidad " + ChatColor.GOLD + " -> " + ChatColor.RED + formatea.format(diferenciaPor) + "% " +
                        ChatColor.RED + " : " + formatea.format(revalorizacion) + " PC" + ChatColor.GOLD + " de " + ChatColor.GREEN + formatea.format(precioInicial * cantidad) + " PC " + ChatColor.GOLD + " invertidas en el primer momento");
            }
            player.sendMessage("         ");
        } );
        player.sendMessage(ChatColor.GOLD + "--Mas info en http://serversurvival2.ddns.net/iniciarsesion.html--");
    }
}