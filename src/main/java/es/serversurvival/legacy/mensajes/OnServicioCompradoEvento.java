package es.serversurvival.legacy.mensajes;

import es.jaime.EventListener;
import es.serversurvival.nfs.empresas.comprarservicio.EmpresaServicioCompradoEvento;
import es.serversurvival.nfs.empresas.mysql.Empresa;
import es.serversurvival.nfs.utils.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static es.serversurvival.nfs.utils.Funciones.enviarMensaje;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

public final class OnServicioCompradoEvento {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onServicioComprado (EmpresaServicioCompradoEvento evento) {
        Player player = Bukkit.getPlayer(evento.getComprador());
        Empresa empresa = evento.getEmpresa();

        String mensajeOnline = GOLD + player.getName() + " ha comprado vuestro servicio de la empresa: " + empresa.getNombre() +
                " por " + GREEN + formatea.format(evento.getPrecio()) + " PC";

        enviarMensaje(evento.getEmpresa().getOwner(), mensajeOnline, mensajeOnline);

        player.sendMessage(GOLD + "Has pagado " + GREEN + evento.getPrecio() + " PC " + GOLD + " a la empresa: " + empresa + " por su servicio");

    }
}
