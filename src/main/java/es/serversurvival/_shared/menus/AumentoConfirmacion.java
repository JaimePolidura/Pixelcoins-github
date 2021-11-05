package es.serversurvival._shared.menus;

import es.serversurvival._shared.menus.confirmaciones.Confirmacion;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface AumentoConfirmacion extends Confirmacion {
    void onChangeAumento (int var);

    @Override
    default void onOtherClick (InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null || Funciones.noEsDeTipoItem(itemStack,"LIGHT_GRAY_BANNER" )){
            return;
        }

        String name = itemStack.getItemMeta().getDisplayName();
        StringBuilder stringBuild = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if(i == 2){
                if(name.charAt(i) == '-')
                    stringBuild.append(name.charAt(2));
            }else if (i >= 2){
                stringBuild.append(name.charAt(i));
            }
        }

        onChangeAumento(Integer.parseInt(stringBuild.toString()));
    }
}
