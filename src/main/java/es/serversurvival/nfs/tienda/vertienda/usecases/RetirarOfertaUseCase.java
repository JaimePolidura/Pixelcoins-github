package es.serversurvival.nfs.tienda.vertienda.usecases;

import es.serversurvival.nfs.tienda.mySQL.ofertas.Ofertas;
import es.serversurvival.nfs.tienda.mySQL.ofertas.Oferta;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public final class RetirarOfertaUseCase {
    public static final RetirarOfertaUseCase INSTANCE = new RetirarOfertaUseCase();
    private final Ofertas ofertasMySQL;

    private RetirarOfertaUseCase() {
        this.ofertasMySQL = Ofertas.INSTANCE;
    }

    public ItemStack retirarOferta(Player player, int idARetirar) {
        Oferta ofertaARetirar = ofertasMySQL.getOferta(idARetirar);

        ItemStack itemARetirar = ofertasMySQL.getItemOferta(ofertaARetirar);


        this.ofertasMySQL.borrarOferta(idARetirar);

        return itemARetirar;
    }
}
