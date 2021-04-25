package es.serversurvival.nfs.webconnection.verificacioncuentas.enviarnumero;

import es.serversurvival.nfs.webconnection.socketmessages.SocketMessaggeExecutor;
import es.serversurvival.nfs.webconnection.verificacioncuentas.mysql.VerificacionCuentas;
import es.serversurvival.nfs.webconnection.socketmessages.SocketMessagge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Form of the messagge: sendnumber-name=jaimetruman&number=10
 */
public class SendNumberMessagge extends SocketMessaggeExecutor {
    private final String NAME = "sendnumber";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public SocketMessagge execute(SocketMessagge messagge) {
        Player player = Bukkit.getPlayer(messagge.get("name"));

        if(player != null){
            VerificacionCuentas verificacionCuentasMySQL = VerificacionCuentas.INSTANCE;
            int number = (int) (Math.random() * 9999);

            VerificacionCuentas.INSTANCE.removeVerificacionCuenta(player.getName());
            VerificacionCuentas.INSTANCE.nuevaVerificacionCuenta(player.getName(), number);

            player.sendMessage(ChatColor.GOLD + "El numero de verificacion: " + number);
        }

        return NO_RESPONSE;
    }
}
