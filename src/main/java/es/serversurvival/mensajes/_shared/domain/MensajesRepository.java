package es.serversurvival.mensajes._shared.domain;

import java.util.List;

public interface MensajesRepository {
    void save(Mensaje mensaje);

    List<Mensaje> findMensajesByDestinatario(String destinatario);

    void deleteByDestinatario(String destinatario);
}
