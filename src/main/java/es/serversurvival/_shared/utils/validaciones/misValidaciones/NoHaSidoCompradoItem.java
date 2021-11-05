package es.serversurvival._shared.utils.validaciones.misValidaciones;

import main.ValidationResult;
import main.validators.Validator;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class NoHaSidoCompradoItem implements Validator {
    private final String messageOnFailed;

    public NoHaSidoCompradoItem(String messageOnFailed) {
        this.messageOnFailed = messageOnFailed;
    }

    @Override
    public String getMessageOnFailed() {
        return messageOnFailed;
    }

    @Override
    public ValidationResult check(Object o) {
        try{
            ItemStack item = (ItemStack) o;

            return haSidoComprado(item) ?
                    ValidationResult.failed(messageOnFailed) :
                    ValidationResult.success();

        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }

    private boolean haSidoComprado (ItemStack item) {
        try{
            List<String> lore = item.getItemMeta().getLore();

            if(lore == null || lore.size() == 0)
                return false;

            return lore.get(0).equalsIgnoreCase("Comprado en la tienda");

        }catch (Exception e){
            return false;
        }
    }
}
