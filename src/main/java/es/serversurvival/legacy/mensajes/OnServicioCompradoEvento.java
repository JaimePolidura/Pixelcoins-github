package es.serversurvival.legacy.mensajes;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.eventos.empresas.ServicioCompradoEvento;
import es.serversurvival.legacy.mySQL.tablasObjetos.Empresa;
import es.serversurvival.legacy.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static es.serversurvival.legacy.util.Funciones.enviarMensaje;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

public final class OnServicioCompradoEvento {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onServicioComprado (ServicioCompradoEvento evento) {
        Player player = Bukkit.getPlayer(evento.getComprador());
        Empresa empresa = evento.getEmpresa();

        String mensajeOnline = GOLD + player.getName() + " ha comprado vuestro servicio de la empresa: " + empresa.getNombre() +
                " por " + GREEN + formatea.format(evento.getPrecio()) + " PC";

        enviarMensaje(evento.getEmpresa().getOwner(), mensajeOnline, mensajeOnline);

        player.sendMessage(GOLD + "Has pagado " + GREEN + evento.getPrecio() + " PC " + GOLD + " a la empresa: " + empresa + " por su servicio");

    }
}
