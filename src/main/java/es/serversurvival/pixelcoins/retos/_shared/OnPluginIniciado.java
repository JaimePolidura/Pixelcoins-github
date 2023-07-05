package es.serversurvival.pixelcoins.retos._shared;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival._shared.eventospixelcoins.PluginIniciado;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.lootbox.LootboxTier;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.*;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.recompensas.TipoRecompensa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

import java.util.*;
import java.util.stream.Collectors;

import static es.serversurvival.pixelcoins.retos._shared.OnPluginIniciado.RecompensaRetoSeeder.*;
import static es.serversurvival.pixelcoins.retos._shared.OnPluginIniciado.RecompensaRetoSeeder.lootbox;
import static es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping.*;
import static es.serversurvival.pixelcoins.retos._shared.retos.domain.FormatoCantidadRequerida.*;
import static es.serversurvival.pixelcoins.retos._shared.retos.domain.ModuloReto.*;

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
    public void on(PluginIniciado e) {
        if(!retosRepository.findAll().isEmpty()){
            return;
        }

        List<RetoSeeder> retosSeeder = crearRetos();
        saveRetosToRepository(retosSeeder);
    }

    private static List<RetoSeeder> crearRetos() {
        return List.of(
                RetoSeeder.individual(JUGADORES_PAGO_PAGADOR, "Hacer un pago", "Haz un pago con /pagar a otro jugador", JUGADORES, MUY_FACIL),
                RetoSeeder.individual(JUGADORES_VENDER_JUGADOR_VENDEDOR, "Vende a un jugador algo", "Vende un jugador un objeto con /venderjugador", JUGADORES, FACIL),
                RetoSeeder.individual(JUGADORES_CAMBIO_INGRESAR_DIAMANTE, "Ingresa un diamante", "Cambia un diamante por pixelcoins con /cambio ingresar", JUGADORES, NORMAL),
                RetoSeeder.individual(JUGADORES_CAMBIO_INGRESAR_CUARZO, "Ingresa cuarzo", "Cambia un bloque de cuarzo por pixelcoins con /cambio ingresar", JUGADORES, NORMAL),
                RetoSeeder.individual(JUGADORES_CAMBIO_INGRESAR_LAPISLAZULI, "Ingresa lapislazuli", "Cambia lapislazuli por pixelcoins con /cambio ingresar", JUGADORES, NORMAL),
                RetoSeeder.progresivoInicio(JUGADORES_PATRIMONIO, "Dinero", "Ten el maximo dinero posible. El dinero se son todas las pixelcoins que tienes en el servidor: empresas + pixelcoins + bolsa + pixelcoins que te deben - pixelcoins que debes", PIXELCOINS, "Pixelcoins", JUGADORES, List.of(
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

                RetoSeeder.progresivoInicio(EMPRESAS_PAGAR_PAGADOR, "Paga a una empresa", "Haz un total de pagos a las empresas con /empresas pagar", PIXELCOINS, "Pagos", EMPRESAS, List.of(
                        RetoSeeder.progresivo(100, MUY_FACIL),
                        RetoSeeder.progresivo(250, MUY_FACIL),
                        RetoSeeder.progresivo(500, FACIL),
                        RetoSeeder.progresivo(1000, FACIL),
                        RetoSeeder.progresivo(5000, NORMAL),
                        RetoSeeder.progresivo(10000, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(25000, DIFICIL_LOOTBOX),
                        RetoSeeder.progresivo(50000, DIFICIL_LOOTBOX.withCantidad(2))
                )),

                RetoSeeder.individual(EMPRESAS_CREAR, "Crea una empresa", "Crea una empresa con /empresas crear", EMPRESAS, NORMAL, List.of(
                        RetoSeeder.progresivoInicio(EMPRESAS_PAGAR_PAGADO,"Facturacion empresa", "Haz facturar todas las pixelcons posibles a una empresa en la que seas director", PIXELCOINS, "Facturacion", EMPRESAS, List.of(
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
                        RetoSeeder.individual(EMPRESAS_CONTRATAR,"Contratar a un jugador", "Contrata a un jugador con /empresas contratar", EMPRESAS, NORMAL, List.of(
                                RetoSeeder.individual(EMPRESAS_CONTRATAR_PAGADOR_SUELDO,"Pagar el sueldo a un jugador", "El sueldo se paga automaticamente", EMPRESAS, FACIL))
                        ),
                        RetoSeeder.individual(EMPRESAS_DEPOSITAR,"Depositar pixelcoins empresa", "Deposita tus pixelcoins en tu empresa con /empresas depositar", EMPRESAS, FACIL),
                        RetoSeeder.individual(EMPRESAS_BOLSA_IPO,"Sacar a la bolsa tu empresa", "Saca a la bolsa tu empresa con /empresas ipo", EMPRESAS, NORMAL, List.of(
                                RetoSeeder.individual(EMPRESAS_BOLSA_EMITIR,"Emitir acciones", "Emite acciones de tu empresa en el mercado con /empresas emitir", EMPRESAS, NORMAL, List.of(
                                        RetoSeeder.individual(EMPRESAS_BOLSA_RECAUDAR_EMISION,"Recaudar emision", "Una vez que hayas emitido acciones de tu empresa, un jugador tendra que comprarlas en el emrcado", EMPRESAS, NORMAL)
                                )),
                                RetoSeeder.individual(EMPRESAS_BOLSA_REPARTIR_DIVIDENDOS, "Repartir dividendos", "Reparte pixelcoins de tu empresa con los accionistas /empresas repartirdividendos", EMPRESAS, NORMAL),
                                RetoSeeder.individual(EMPRESAS_BOLSA_RECAUDAR_IPO,"Recaudar IPO", "Una vez que hayas sacado a bolsa tu empresa, un jugador tendra que comprarlas en el mercado para que la empresa recaude el dinero", EMPRESAS, NORMAL)
                        ))
                )),
                RetoSeeder.individual(EMPRESAS_ACCIONISTAS_COMPRAR, "Compra acciones servidor", "Compra acciones de empresas del servidor en /bolsa mercado", EMPRESAS, FACIL, List.of(
                        RetoSeeder.individual(EMPRESAS_ACCIONISTAS_INICIAR_VOTACION, "Inicia votacion", "Una vez seas accionista podras iniciar votaciones sobre la empresa donde el resto de accionistas podran votar", EMPRESAS, NORMAL),
                        RetoSeeder.individual(EMPRESAS_ACCIONISTAS_VOTAR,"Vota", "Vota en una votacion propuesta por otro accionista /empresas votaciones", EMPRESAS, NORMAL),
                        RetoSeeder.individual(EMPRESAS_ACCIONISTAS_VENTA, "Vende acciones", "Vende las acciones que tengas de una empresa al mercado", EMPRESAS, FACIL),
                        RetoSeeder.individual(EMPRESAS_ACCIONISTAS_RECIBIR_DIVIDENDO, "Recibir dividendo", "Recibe un dividendo de una empresa en la que seas accionista", EMPRESAS, FACIL)
                )),
                RetoSeeder.individual(EMPRESAS_CONTRATADO,"Ser contratado", "Se contratado por otra empresa del servidor", EMPRESAS, FACIL, List.of(
                        RetoSeeder.individual(EMPRESAS_CONTRATADO_PAGADO_SUELDO,"Ser pagado un sueldo", "El sueldo se paga automaticamente en la empresa donde estes contratado", EMPRESAS, FACIL)
                )),

                RetoSeeder.progresivoInicio(DEUDAS_PRESTAR_ACREDOR,"Prestar pixelcoins", "Haz que los jugadores te deban todas estas pixelcoins a la vez con /deudas prestar", PIXELCOINS, "Prestamos", DEUDAS, List.of(
                        RetoSeeder.progresivo(100, FACIL),
                        RetoSeeder.progresivo(250, FACIL),
                        RetoSeeder.progresivo(500, FACIL),
                        RetoSeeder.progresivo(1000, NORMAL),
                        RetoSeeder.progresivo(5000, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(10000, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(25000, DIFICIL),
                        RetoSeeder.progresivo(50000, DIFICIL_LOOTBOX)
                )),
                RetoSeeder.individual(DEUDAS_PRESTAR_DEUDOR,"Acepta un prestamo", "Acepta un prestamo de pixelcoins", DEUDAS, FACIL),
                RetoSeeder.individual(DEUDAS_COMPRAR,"Compra deuda", "Compra deuda de otro jugador en el mercado /deudas mercado", DEUDAS, NORMAL),
                RetoSeeder.individual(DEUDAS_VENDER,"Vende deuda", "Si un jugador te debe dinero, puedes vender esa deuda por pixelcoins en el mercado. El comprador recibira los intereses", DEUDAS, NORMAL),
                RetoSeeder.individual(DEUDAS_PAGADA_ENTERA_SIN_NINPAGOS,"No impages la deuda", "Paga todas las cuotas de la deuda sin hacer ningun impago", DEUDAS, DIFICIL),
                RetoSeeder.progresivoInicio(DEUDAS_PRESTAR_COBRO_CUOTAS,"Cobra intereses de la deuda", "Ten cobrado en total estas pixelcoins de intereses de deudas que te deban otros jugadores", PIXELCOINS, "Pixelcoins en intereses", DEUDAS, List.of(
                        RetoSeeder.progresivo(100, FACIL),
                        RetoSeeder.progresivo(250, FACIL),
                        RetoSeeder.progresivo(500, NORMAL),
                        RetoSeeder.progresivo(1000, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(5000, DIFICIL),
                        RetoSeeder.progresivo(10000, DIFICIL_LOOTBOX),
                        RetoSeeder.progresivo(25000, MUY_DIFICIL),
                        RetoSeeder.progresivo(50000, MUY_DIFICIL_LOOTBOX)
                )),

                RetoSeeder.individual(BOLSA_ABRIR_LARGO,"Compra acciones en bolsa", "Puedes comprar acciones de la bolsa de la vida real en /bolsa valores", BOLSA, NORMAL),
                RetoSeeder.individual(BOLSA_ABRIR_CORTO,"Vender en corto", "Puedes apostar en contra de acciones de la bolsa de la vida real con /bolsa invertir", BOLSA, DIFICIL),
                RetoSeeder.progresivoInicio(BOLSA_CERRAR_RENTABILIDAD, "Obtener rentabilidad", "Obten la maxima rentabilidad vendiendo acciones", PORCENTAJE_RESULTADO, "Rentabilidad en bolsa", BOLSA, List.of(
                        RetoSeeder.progresivo(1.05d, FACIL),
                        RetoSeeder.progresivo(1.15d, NORMAL),
                        RetoSeeder.progresivo(1.25d, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(1.5d, DIFICIL),
                        RetoSeeder.progresivo(1.75d, DIFICIL_LOOTBOX),
                        RetoSeeder.progresivo(2d, MUY_DIFICIL_LOOTBOX),
                        RetoSeeder.progresivo(2.5d, MUY_DIFICIL_LOOTBOX.withCantidad(3)),
                        RetoSeeder.progresivo(3d, MUY_MUY_DIFICIL)
                )),
                RetoSeeder.progresivoInicio(TIENDA_VENDER_VOLUMEN, "Vende en la tienda (volumen)", "Obten el maximo volumen de ventas de pixelcoins en la tienda con /tienda vender", PIXELCOINS, "Ventas", TIENDA, List.of(
                        RetoSeeder.progresivo(100, MUY_FACIL),
                        RetoSeeder.progresivo(250, FACIL),
                        RetoSeeder.progresivo(500, FACIL),
                        RetoSeeder.progresivo(1000, NORMAL),
                        RetoSeeder.progresivo(5000, NORMAL_LOOTBOX),
                        RetoSeeder.progresivo(10000, DIFICIL),
                        RetoSeeder.progresivo(25000, DIFICIL_LOOTBOX),
                        RetoSeeder.progresivo(50000, MUY_DIFICIL_LOOTBOX.withCantidad(2))
                )),
                RetoSeeder.progresivoInicio(TIENDA_VENDER_CANTIDAD, "Vende en la tienda (cantidad)", "Ten el maximo numero de objetos vendidos en la tienda", NUMERO, "Nº En ventas", TIENDA, List.of(
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

    private void saveRetosToRepository(List<RetoSeeder> retosSeeder) {
        Queue<RetoPendingToBuild> retosPendingToBuild = new LinkedList<>(retoSeederToRetoPendingToBuild(retosSeeder, Funciones.NULL_ID));
        List<Reto> retos = new LinkedList<>();

        while (!retosPendingToBuild.isEmpty()) {
            RetoPendingToBuild pendingToBuild = retosPendingToBuild.poll();
            RetoSeeder retoSeederPendingToBuild = pendingToBuild.retoSeeder;
            boolean esProgresivo = pendingToBuild.retoSeeder.esProgresivo();
            boolean tieneHijos = pendingToBuild.retoSeeder.tieneHijos();
            UUID retoAnteriorId = pendingToBuild.anteriorId;

            if(esProgresivo){
                List<Reto> retosProgresion = buildRetosProgresion(pendingToBuild.retoSeeder, retoAnteriorId);

                retos.addAll(retosProgresion);
            }else {
                Reto retoPadre = Reto.builder()
                        .retoId(UUID.randomUUID())
                        .nombre(retoSeederPendingToBuild.nombre)
                        .descripccion(retoSeederPendingToBuild.descripccion)
                        .mapping(retoSeederPendingToBuild.mapping)
                        .moduloReto(retoSeederPendingToBuild.modulo)
                        .retoPadreId(retoAnteriorId)
                        .retoPadreProgresionId(Funciones.NULL_ID)
                        .cantidadRequerida(0)
                        .tipo(retoSeederPendingToBuild.tipo)
                        .tipoRecompensa(retoSeederPendingToBuild.recompensa.tipoRecompensa)
                        .recompensaPixelcoins(retoSeederPendingToBuild.recompensa.pixelcoins)
                        .lootboxTierRecompensa(retoSeederPendingToBuild.recompensa.lootboxTier)
                        .nLootboxesRecompensa(retoSeederPendingToBuild.recompensa.cantidad)
                        .build();

                retos.add(retoPadre);

                if(tieneHijos){
                    List<RetoPendingToBuild> hijosPendingToBuild = retoSeederToRetoPendingToBuild(pendingToBuild.retoSeeder.getHijos(), retoPadre.getRetoId());

                    retosPendingToBuild.addAll(hijosPendingToBuild);
                }
            }
        }

        retos.forEach(retosRepository::save);
    }

    private List<Reto> buildRetosProgresion(RetoSeeder padreSeeder, UUID retoAnteriorId) {
        List<Reto> retos = new LinkedList<>();
        UUID retoPadreProgresion = UUID.randomUUID();

        for (int i = 0; i < padreSeeder.getProgresion().size(); i++) {
            RetoSeeder actualRetoSeeder = padreSeeder.getProgresion().get(i);
            boolean primeroEnProgresion = i == 0;

            Reto reto = Reto.builder()
                    .retoId(primeroEnProgresion ? retoPadreProgresion : UUID.randomUUID())
                    .nombre(padreSeeder.nombre)
                    .descripccion(padreSeeder.descripccion)
                    .mapping(padreSeeder.mapping)
                    .moduloReto(padreSeeder.modulo)
                    .retoPadreId(retoAnteriorId)
                    .retoPadreProgresionId(retoPadreProgresion)
                    .nombreUnidadCantidadRequerida(padreSeeder.nombreUnidadCantidadRequerida)
                    .formatoCantidadRequerida(padreSeeder.formatoCantidadRequerida)
                    .cantidadRequerida(actualRetoSeeder.cantidadRequerida)
                    .tipo(padreSeeder.tipo)
                    .tipoRecompensa(actualRetoSeeder.recompensa.tipoRecompensa)
                    .recompensaPixelcoins(actualRetoSeeder.recompensa.pixelcoins)
                    .lootboxTierRecompensa(actualRetoSeeder.recompensa.lootboxTier)
                    .nLootboxesRecompensa(actualRetoSeeder.recompensa.cantidad)
                    .build();

            retoAnteriorId = reto.getRetoId();

            retos.add(reto);
        }

        return retos;
    }

    private List<RetoPendingToBuild> retoSeederToRetoPendingToBuild(List<RetoSeeder> retoSeeders, UUID retoAnteriorId) {
        return retoSeeders.stream()
                .map(retoSeeder -> new RetoPendingToBuild(retoSeeder, retoAnteriorId))
                .collect(Collectors.toList());
    }

    private record RetoPendingToBuild(RetoSeeder retoSeeder, UUID anteriorId){}

    @AllArgsConstructor
    public static class RetoSeeder {
        @Getter private RetoMapping mapping;
        @Getter private String nombre;
        @Getter private String descripccion;
        @Getter private ModuloReto modulo;
        @Getter private TipoReto tipo;
        @Getter private double cantidadRequerida;
        @Getter private FormatoCantidadRequerida formatoCantidadRequerida;
        @Getter private String nombreUnidadCantidadRequerida;
        @Getter private List<RetoSeeder> hijos;
        @Getter private List<RetoSeeder> progresion;
        @Getter private RecompensaRetoSeeder recompensa;

        public boolean tieneHijos() {
            return this.hijos != null && this.hijos.size() > 0;
        }

        public boolean esProgresivo() {
            return this.tipo == TipoReto.PROGRESIVO;
        }

        public static RetoSeeder individual(RetoMapping mapping, String nombre, String desc, ModuloReto moduloReto, RecompensaRetoSeeder recompensa, List<RetoSeeder> hijos) {
            return new RetoSeeder(mapping, nombre, desc, moduloReto, TipoReto.INDEPENDIENTE, 0, null, "", hijos, null, recompensa);
        }

        public static RetoSeeder individual(RetoMapping mapping, String nombre, String desc, ModuloReto moduloReto, RecompensaRetoSeeder recompensa) {
            return new RetoSeeder(mapping, nombre, desc, moduloReto, TipoReto.INDEPENDIENTE, 0, null, "", null, null, recompensa);
        }

        public static RetoSeeder progresivoInicio(RetoMapping mapping, String nombre, String desc, FormatoCantidadRequerida formato, String nombreUnidadCantidadRequerida, ModuloReto moduloReto, List<RetoSeeder> progresion) {
            return new RetoSeeder(mapping, nombre, desc, moduloReto, TipoReto.PROGRESIVO, 0,  formato, nombreUnidadCantidadRequerida, null, progresion, null);
        }

        public static RetoSeeder progresivo(double cantidadRequerida, RecompensaRetoSeeder recompensa) {
            return new RetoSeeder(null, null, null, null ,TipoReto.PROGRESIVO, cantidadRequerida, null, "", null, null, recompensa);
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
