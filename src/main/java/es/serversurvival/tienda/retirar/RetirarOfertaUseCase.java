package es.serversurvival.tienda.retirar;

import es.serversurvival.tienda._shared.mySQL.ofertas.Ofertas;
import es.serversurvival.tienda._shared.mySQL.ofertas.Oferta;
import org.bukkit.inventory.ItemStack;


public final class RetirarOfertaUseCase {
    public static final RetirarOfertaUseCase INSTANCE = new RetirarOfertaUseCase();
    private final Ofertas ofertasMySQL;

    private RetirarOfertaUseCase() {
        this.ofertasMySQL = Ofertas.INSTANCE;
    }

    public ItemStack retirarOferta(int idARetirar) {
        Oferta ofertaARetirar = ofertasMySQL.getOferta(idARetirar);

        ItemStack itemARetirar = ofertasMySQL.getItemOferta(ofertaARetirar);

        this.ofertasMySQL.borrarOferta(idARetirar);

        return itemARetirar;
    }
}
