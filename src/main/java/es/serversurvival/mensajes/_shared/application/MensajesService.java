package es.serversurvival.mensajes._shared.application;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.mensajes._shared.domain.Mensaje;
import es.serversurvival.mensajes._shared.domain.MensajesRepository;

import java.util.List;
import java.util.UUID;

public final class MensajesService {
    private final MensajesRepository mensajesRepository;

    public MensajesService() {
        this.mensajesRepository = DependecyContainer.get(MensajesRepository.class);
    }

    public void save(Mensaje mensaje){
        this.mensajesRepository.save(mensaje);
    }

    public void save(String destinatario, String mensaje) {
        this.mensajesRepository.save(new Mensaje(UUID.randomUUID(), "", destinatario, mensaje));
    }

    public List<Mensaje> findMensajesByDestinatario(String destinatario) {
        return this.mensajesRepository.findMensajesByDestinatario(destinatario);
    }

    public void deleteByDestinatario(String destinatario) {
        this.mensajesRepository.deleteByDestinatario(destinatario);
    }
}
