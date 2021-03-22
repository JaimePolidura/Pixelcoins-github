package es.serversurvival.objetos.solicitudes;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public abstract class Solicitud {
    public DecimalFormat formatea = new DecimalFormat("###,###.##");
    public static Set<Solicitud> solicitudes = new HashSet<>();

    public abstract void enviarSolicitud();

    public abstract void aceptar();

    public abstract void cancelar();

    public abstract String getDestinatario();

    public abstract String getTitulo();

    public static boolean haSidoSolicitado(String destinatario) {
        for (Solicitud solicitude : solicitudes) {
            if (solicitude.getDestinatario().equalsIgnoreCase(destinatario)) {
                return true;
            }
        }
        return false;
    }

    public static Solicitud getByDestinatario(String destinatario) {
        for (Solicitud solicitude : solicitudes) {
            if (solicitude.getDestinatario().equalsIgnoreCase(destinatario)) {
                return solicitude;
            }
        }
        return null;
    }
}