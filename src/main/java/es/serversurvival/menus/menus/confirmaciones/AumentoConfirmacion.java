package es.serversurvival.menus.menus.confirmaciones;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static es.serversurvival.util.Funciones.noEsDeTipoItem;

public interface AumentoConfirmacion extends Confirmacion{
    void onChangeAumento (int var);

    @Override
    default void onOtherClick (InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null || noEsDeTipoItem(itemStack,"LIGHT_GRAY_BANNER" )){
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
