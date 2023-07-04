package es.serversurvival.pixelcoins.retos._shared;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival._shared.eventospixelcoins.PluginIniciado;
import es.serversurvival.pixelcoins.lootbox.LootboxTier;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.ModuloReto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetosRepository;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.TipoReto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.recompensas.TipoRecompensa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

import java.util.List;

import static es.serversurvival.pixelcoins.retos._shared.OnPluginIniciado.RecompensaRetoSeeder.*;
import static es.serversurvival.pixelcoins.retos._shared.OnPluginIniciado.RecompensaRetoSeeder.lootbox;

@EventHandler
@AllArgsConstructor
public final class OnPluginIniciado {
    private final RetosRepository retosRepository;

    private static RecompensaRetoSeeder MUY_FACIL = pixelcoins(10);

    private static RecompensaRetoSeeder FACIL = pixelcoins(20);

    private static RecompensaRetoSeeder NORMAL = pixelcoins(50);
    private static RecompensaRetoSeeder NORMAL_LOOTBOX = lootbox(LootboxTier.COMUN);

    private static RecompensaRetoSeeder DIFICIL = pixelcoins(250);
    private static RecompensaRetoSeeder DIFICIL_LOOTBOX = lootbox(LootboxTier.NORMAL);

    private static RecompensaRetoSeeder MUY_DIFICIL = pixelcoins(500);
    private static RecompensaRetoSeeder MUY_DIFICIL_LOOTBOX = lootbox(LootboxTier.RARO);

    private static RecompensaRetoSeeder MUY_MUY_DIFICIL = pixelcoins(20000);

    @EventListener
    public void on(PluginIniciado pluginIniciado) {
        List<RetoSeeder> retos = List.of(
                RetoSeeder.individual("Hacer un pago", "Haz un pago con /pagar a otro jugador", ModuloReto.JUGADORES, MUY_FACIL),
                RetoSeeder.individual("Vende a un jugador algo", "Vende un jugador un objeto con /venderjugador", ModuloReto.JUGADORES, FACIL),
                RetoSeeder.individual("Ingresa un diamante", "Cambia un diamante por pixelcoins con /cambio ingresar", ModuloReto.JUGADORES, NORMAL),
                RetoSeeder.individual("Ingresa cuarzo", "Cambia un bloque de cuarzo por pixelcoins con /cambio ingresar", ModuloReto.JUGADORES, NORMAL),
                RetoSeeder.individual("Ingresa lapislazuli", "Cambia lapislazuli por pixelcoins con /cambio ingresar", ModuloReto.JUGADORES, NORMAL),
                RetoSeeder.progresivoInicio("Dinero", "Ten el maximo dinero posible. El dinero se son todas las pixelcoins que tienes en el servidor: empresas + pixelcoins + bolsa + pixelcoins que te deben - pixelcoins que debes", ModuloReto.JUGADORES, List.of(
                        RetoSeeder.progresivo(100, MUY_FACIL),
                        RetoSeeder.progresivo(1000, FACIL),
                        RetoSeeder.progresivo(5000, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(10000, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(25000, MUY_DIFICIL),
                        RetoSeeder.progresivo(50000, DIFICIL_LOOTBOX),
                        RetoSeeder.progresivo(100000, DIFICIL_LOOTBOX.withCantidad(3)),
                        RetoSeeder.progresivo(500000, MUY_DIFICIL_LOOTBOX),
                        RetoSeeder.progresivo(1000000, MUY_DIFICIL_LOOTBOX.withCantidad(5))
                )),

                RetoSeeder.progresivoInicio("Paga a una empresa", "Haz un total de pagos a las empresas con /empresas pagar", ModuloReto.EMPRESAS, List.of(
                        RetoSeeder.progresivo(100, MUY_FACIL),
                        RetoSeeder.progresivo(250, MUY_FACIL),
                        RetoSeeder.progresivo(500, FACIL),
                        RetoSeeder.progresivo(1000, FACIL),
                        RetoSeeder.progresivo(5000, NORMAL),
                        RetoSeeder.progresivo(10000, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(25000, DIFICIL_LOOTBOX),
                        RetoSeeder.progresivo(50000, DIFICIL_LOOTBOX.withCantidad(2))
                )),

                RetoSeeder.individual("Crea una empresa", "Crea una empresa con /empresas crear", ModuloReto.EMPRESAS, NORMAL, List.of(
                        RetoSeeder.progresivoInicio("Facturacion empresa", "Haz facturar todas las pixelcons posibles a una empresa en la que seas director", ModuloReto.EMPRESAS, List.of(
                                RetoSeeder.progresivo(100, MUY_FACIL),
                                RetoSeeder.progresivo(1000, FACIL),
                                RetoSeeder.progresivo(5000, NORMAL_LOOTBOX),
                                RetoSeeder.progresivo(10000, NORMAL_LOOTBOX),
                                RetoSeeder.progresivo(25000, MUY_DIFICIL),
                                RetoSeeder.progresivo(50000, DIFICIL_LOOTBOX),
                                RetoSeeder.progresivo(100000, DIFICIL_LOOTBOX.withCantidad(3)),
                                RetoSeeder.progresivo(500000, MUY_DIFICIL_LOOTBOX),
                                RetoSeeder.progresivo(1000000, MUY_DIFICIL_LOOTBOX.withCantidad(3))
                        )),
                        RetoSeeder.individual("Contratar a un jugador", "Contrata a un jugador con /empresas contratar", ModuloReto.EMPRESAS, NORMAL, List.of(
                                RetoSeeder.individual("Pagar el sueldo a un jugador", "El sueldo se paga automaticamente", ModuloReto.EMPRESAS, FACIL))
                        ),
                        RetoSeeder.individual("Depositar pixelcoins empresa", "Deposita tus pixelcoins en tu empresa con /empresas depositar", ModuloReto.EMPRESAS, FACIL),
                        RetoSeeder.individual("Sacar a la bolsa tu empresa", "Saca a la bolsa tu empresa con /empresas ipo", ModuloReto.EMPRESAS, NORMAL, List.of(
                                RetoSeeder.individual("Emitir acciones", "Emite acciones de tu empresa en el mercado con /empresas emitir", ModuloReto.EMPRESAS, NORMAL, List.of(
                                        RetoSeeder.individual("Recaudar emision", "Una vez que hayas emitido acciones de tu empresa, un jugador tendra que comprarlas en el emrcado", ModuloReto.EMPRESAS, NORMAL)
                                )),
                                RetoSeeder.individual("Repartir dividendos", "Reparte pixelcoins de tu empresa con los accionistas /empresas repartirdividendos", ModuloReto.EMPRESAS, NORMAL),
                                RetoSeeder.individual("Recaudar IPO", "Una vez que hayas sacado a bolsa tu empresa, un jugador tendra que comprarlas en el mercado para que la empresa recaude el dinero", ModuloReto.EMPRESAS, NORMAL)
                        ))
                )),
                RetoSeeder.individual("Compra acciones servidor", "Compra acciones de empresas del servidor en /bolsa mercado", ModuloReto.EMPRESAS, FACIL, List.of(
                        RetoSeeder.individual("Inicia votacion", "Una vez seas accionista podras iniciar votaciones sobre la empresa donde el resto de accionistas podran votar", ModuloReto.EMPRESAS, NORMAL),
                        RetoSeeder.individual("Vota", "Vota en una votacion propuesta por otro accionista /empresas votaciones", ModuloReto.EMPRESAS, NORMAL),
                        RetoSeeder.individual("Vende acciones", "Vende las acciones que tengas de una empresa al mercado", ModuloReto.EMPRESAS, FACIL),
                        RetoSeeder.individual("Recibir dividendo", "Recibe un dividendo de una empresa en la que seas accionista", ModuloReto.EMPRESAS, FACIL)
                )),
                RetoSeeder.individual("Ser contratado", "Se contratado por otra empresa del servidor", ModuloReto.EMPRESAS, FACIL, List.of(
                        RetoSeeder.individual("Ser pagado un sueldo", "El sueldo se paga automaticamente en la empresa donde estes contratado", ModuloReto.EMPRESAS, FACIL)
                )),

                RetoSeeder.progresivoInicio("Prestar pixelcoins", "Haz que los jugadores te deban todas estas pixelcoins a la vez con /deudas prestar", ModuloReto.DEUDAS, List.of(
                        RetoSeeder.progresivo(100, FACIL),
                        RetoSeeder.progresivo(250, FACIL),
                        RetoSeeder.progresivo(500, FACIL),
                        RetoSeeder.progresivo(1000, NORMAL),
                        RetoSeeder.progresivo(5000, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(10000, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(25000, DIFICIL),
                        RetoSeeder.progresivo(50000, DIFICIL_LOOTBOX)
                )),
                RetoSeeder.individual("Acepta un prestamo", "Acepta un prestamo de pixelcoins", ModuloReto.DEUDAS, FACIL),
                RetoSeeder.individual("Compra deuda", "Compra deuda de otro jugador en el mercado /deudas mercado", ModuloReto.DEUDAS, NORMAL),
                RetoSeeder.individual("Vende deuda", "Si un jugador te debe dinero, puedes vender esa deuda por pixelcoins en el mercado. El comprador recibira los intereses", ModuloReto.DEUDAS, NORMAL),
                RetoSeeder.individual("No impages la deuda", "Paga todas las cuotas de la deuda sin hacer ningun impago", ModuloReto.DEUDAS, DIFICIL),
                RetoSeeder.progresivoInicio("Cobra intereses de la deuda", "Ten cobrado en total estas pixelcoins de intereses de deudas que te deban otros jugadores", ModuloReto.DEUDAS, List.of(
                        RetoSeeder.progresivo(100, FACIL),
                        RetoSeeder.progresivo(250, FACIL),
                        RetoSeeder.progresivo(500, NORMAL),
                        RetoSeeder.progresivo(1000, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(5000, DIFICIL),
                        RetoSeeder.progresivo(10000, DIFICIL_LOOTBOX),
                        RetoSeeder.progresivo(25000, MUY_DIFICIL),
                        RetoSeeder.progresivo(50000, MUY_DIFICIL_LOOTBOX)
                )),

                RetoSeeder.individual("Compra acciones en bolsa", "Puedes comprar acciones de la bolsa de la vida real en /bolsa valores", ModuloReto.BOLSA, NORMAL),
                RetoSeeder.individual("Vender en corto", "Puedes apostar en contra de acciones de la bolsa de la vida real con /bolsa invertir", ModuloReto.BOLSA, DIFICIL),
                RetoSeeder.progresivoInicio("Obtener rentabilidad", "Obten la maxima rentabilidad vendiendo acciones", ModuloReto.BOLSA, List.of(
                        RetoSeeder.progresivo(1.05d, FACIL),
                        RetoSeeder.progresivo(1.15d, NORMAL),
                        RetoSeeder.progresivo(1.25d, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(1.5d, DIFICIL),
                        RetoSeeder.progresivo(1.75d, DIFICIL_LOOTBOX),
                        RetoSeeder.progresivo(2d, MUY_DIFICIL_LOOTBOX),
                        RetoSeeder.progresivo(2.5d, MUY_DIFICIL_LOOTBOX.withCantidad(3)),
                        RetoSeeder.progresivo(3d, MUY_MUY_DIFICIL)
                )),
                RetoSeeder.progresivoInicio("Vende en la tienda (volumen)", "Obten el maximo volumen de ventas de pixelcoins en la tienda con /tienda vender", ModuloReto.TIENDA, List.of(
                        RetoSeeder.progresivo(100, MUY_FACIL),
                        RetoSeeder.progresivo(250, FACIL),
                        RetoSeeder.progresivo(500, FACIL),
                        RetoSeeder.progresivo(1000, NORMAL),
                        RetoSeeder.progresivo(5000, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(10000, DIFICIL),
                        RetoSeeder.progresivo(25000, DIFICIL_LOOTBOX),
                        RetoSeeder.progresivo(50000, MUY_DIFICIL_LOOTBOX.withCantidad(2))
                )),
                RetoSeeder.progresivoInicio("Vende en la tienda (cantidad)", "Ten el maximo numero de objetos vendidos en la tienda", ModuloReto.TIENDA, List.of(
                        RetoSeeder.progresivo(1, MUY_FACIL),
                        RetoSeeder.progresivo(64, FACIL),
                        RetoSeeder.progresivo(128, FACIL),
                        RetoSeeder.progresivo(256, NORMAL),
                        RetoSeeder.progresivo(512, DIFICIL),
                        RetoSeeder.progresivo(1024, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(2048, DIFICIL_LOOTBOX.withCantidad(2)),
                        RetoSeeder.progresivo(4096, DIFICIL_LOOTBOX.withCantidad(5))
                ))
        );
    }

    @AllArgsConstructor
    public static class RetoSeeder {
        @Getter private String nombre;
        @Getter private String descripccion;
        @Getter private ModuloReto modulo;
        @Getter private TipoReto tipo;
        @Getter private double cantidadRequerida;
        @Getter private List<RetoSeeder> hijos;
        @Getter private List<RetoSeeder> progresion;
        @Getter private RecompensaRetoSeeder recompensa;

        public static RetoSeeder individual(String nombre, String desc, ModuloReto moduloReto, RecompensaRetoSeeder recompensa, List<RetoSeeder> hijos) {
            return new RetoSeeder(nombre, desc, moduloReto, TipoReto.INDEPENDIENTE, 0, hijos, null, recompensa);
        }

        public static RetoSeeder individual(String nombre, String desc, ModuloReto moduloReto, RecompensaRetoSeeder recompensa) {
            return new RetoSeeder(nombre, desc, moduloReto, TipoReto.INDEPENDIENTE, 0, null, null, recompensa);
        }

        public static RetoSeeder progresivoInicio(String nombre, String desc, ModuloReto moduloReto, List<RetoSeeder> progresion) {
            return new RetoSeeder(nombre, desc, moduloReto, TipoReto.PROGRESIVO, 0, null, progresion, null);
        }

        public static RetoSeeder progresivo(double cantidadRequerida, RecompensaRetoSeeder recompensa) {
            return new RetoSeeder(null, null, null ,TipoReto.PROGRESIVO, cantidadRequerida, null, null, recompensa);
        }
    }

    @AllArgsConstructor
    public static class RecompensaRetoSeeder {
        @Getter private TipoRecompensa tipoRecompensa;
        @Getter private double pixelcoins;
        @Getter private LootboxTier lootboxTier;
        @Getter @With private int cantidad;

        public static RecompensaRetoSeeder pixelcoins(double pixelcoins) {
            return new RecompensaRetoSeeder(TipoRecompensa.PIXELCOINS, pixelcoins, null, 0);
        }

        public static RecompensaRetoSeeder lootbox(LootboxTier tier) {
            return new RecompensaRetoSeeder(TipoRecompensa.LOOTBOX, 0, tier, 1);
        }
    }
}
