package es.serversurvival.tienda._shared.application;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.tienda._shared.domain.EncantamientoObjecto;
import es.serversurvival.tienda._shared.domain.TiendaObjeto;
import es.serversurvival.tienda.comprar.ObjetoTiendaComprado;

import java.util.List;
import java.util.UUID;

public final class TiendaService {
    private final TiendaRepositoryService repository;

    public TiendaService(){
        this.repository = new TiendaRepositoryService();
    }

    public void save(TiendaObjeto tiendaObjeto) {
        this.repository.save(tiendaObjeto);
    }

    public TiendaObjeto save(String jugador, String objetoNombre, int cantidad, double precio,
                                     short durabilidad, List<EncantamientoObjecto> encantamientos){
        var objetoTienda = new TiendaObjeto(UUID.randomUUID(), jugador, objetoNombre,
                cantidad, precio, durabilidad, encantamientos);

        this.repository.save(objetoTienda);

        return objetoTienda;
    }

    public TiendaObjeto getById(UUID id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Objeto en la tienda no encontrado"));
    }

    public List<TiendaObjeto> findByJugador(String jugador) {
        return this.repository.findByJugador(jugador);
    }

    public List<TiendaObjeto> findAll() {
        return this.repository.findAll();
    }

    public void deleteById(UUID id) {
        this.repository.deleteById(id);
    }
}
