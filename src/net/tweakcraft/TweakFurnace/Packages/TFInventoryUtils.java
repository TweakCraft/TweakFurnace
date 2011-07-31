package net.tweakcraft.TweakFurnace.Packages;

import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

/**
 * Created by IntelliJ IDEA.
 * User: Edoxile
 */
public class TFInventoryUtils {
    public static int removeMaterials(Chest chest, ItemStack toRemove) {
        if (toRemove == null)
            return 0;
        ItemStack[] stacks = chest.getInventory().getContents();
        for (int index = 0; index < stacks.length; index++) {
            if (stacks[index] == null || stacks[index].getTypeId() != toRemove.getTypeId() || stacks[index].getDurability() != toRemove.getDurability())
                continue;
            if (toRemove.getAmount() > stacks[index].getAmount()) {
                toRemove.setAmount(toRemove.getAmount() - stacks[index].getAmount());
                stacks[index] = null;
            } else {
                stacks[index].setAmount(stacks[index].getAmount() - toRemove.getAmount());
                toRemove = null;
                break;
            }
        }
        chest.getInventory().setContents(stacks);
        return (toRemove == null ? 0 : toRemove.getAmount());
    }

    public static int addMaterials(Chest chest, ItemStack toAdd) {
        if (toAdd == null)
            return 0;
        ItemStack[] stacks = chest.getInventory().getContents();
        for (int index = 0; index < stacks.length; index++) {
            if (stacks[index] == null) {
                stacks[index] = toAdd;
                toAdd = null;
                break;
            } else {
                if (stacks[index].getTypeId() == toAdd.getTypeId() && stacks[index].getDurability() == toAdd.getDurability() && stacks[index].getAmount() < 64) {
                    if (stacks[index].getAmount() + toAdd.getAmount() > 64) {
                        toAdd.setAmount(toAdd.getAmount() + stacks[index].getAmount() - 64);
                        stacks[index].setAmount(64);
                    } else {
                        stacks[index].setAmount(stacks[index].getAmount() + toAdd.getAmount());
                        toAdd = null;
                        break;
                    }
                }
            }
        }
        chest.getInventory().setContents(stacks);
        return (toAdd == null ? 0 : toAdd.getAmount());
    }
}
