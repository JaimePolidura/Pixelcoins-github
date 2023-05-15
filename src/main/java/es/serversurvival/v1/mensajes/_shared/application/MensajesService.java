package es.serversurvival.v1.mensajes._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v1.mensajes._shared.domain.Mensaje;
import es.serversurvival.v1.mensajes._shared.domain.MensajesRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class MensajesService {
    private final MensajesRepository mensajesRepository;

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
