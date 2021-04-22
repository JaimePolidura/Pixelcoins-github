package es.serversurvival.comandos.comandos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.mySQL.enums.TipoTransaccion;
import es.serversurvival.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
import es.serversurvival.util.Funciones;
import org.bukkit.command.CommandSender;

@Command(name = "bolsa prueba")
public final class Prueba implements CommandRunner {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args[0].equalsIgnoreCase("a")){
            Funciones.POOL.execute(() -> {
                Pixelcoin.publish(new MyOwnEvent());
            });
        }else{
            Pixelcoin.publish(new MyOwnEvent());
        }
    }
}

class MyOwnEvent extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), "jaimetruman", "", 1, "", TipoTransaccion.TIENDA_VENTA);
    }
}
