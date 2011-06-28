package net.tweakcraft.TweakFurnace.Packages;

import net.tweakcraft.TweakFurnace.OutOfSpaceException;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

/**
 * Created by IntelliJ IDEA.
 * User: Edoxile
 */
public class TFChestUtils {

    public static ItemStack safeAddItems(Chest chest, ItemStack itemStack) {
        int amount = itemStack.getAmount();
        ItemStack[] stacks = chest.getInventory().getContents();
        for (int i = 0; i < stacks.length; i++) {
            if (stacks[i] == null) {
                if (amount > 64) {
                    if (itemStack.getData() == null) {
                        stacks[i] = new ItemStack(itemStack.getType(), 64);
                    } else {
                        stacks[i] = itemStack.getData().toItemStack(64);
                    }
                    amount -= 64;
                } else {
                    if (itemStack.getData() == null) {
                        stacks[i] = new ItemStack(itemStack.getType(), amount);
                    } else {
                        stacks[i] = itemStack.getData().toItemStack(amount);
                    }
                    amount = 0;
                    break;
                }
            } else {
                if (stacks[i].getType() != itemStack.getType() || stacks[i].getAmount() == stacks[i].getMaxStackSize()) {
                    continue;
                } else {
                    if (stacks[i].getAmount() + amount > 64) {
                        if (itemStack.getData() == null && stacks[i].getData() == null) {
                            stacks[i].setAmount(64);
                        } else {
                            if (itemStack.getDurability() != stacks[i].getDurability()) {
                                continue;
                            }
                            stacks[i] = itemStack.getData().toItemStack(64);
                        }
                        amount -= 64;
                    } else {
                        if (itemStack.getData() == null && stacks[i].getData() == null) {
                            stacks[i].setAmount(stacks[i].getAmount() + amount);
                        } else {
                            if (!itemStack.getData().equals(stacks[i].getData())) {
                                continue;
                            }
                            stacks[i].setAmount(stacks[i].getAmount() + amount);
                        }
                        amount = 0;
                        break;
                    }
                }
            }
            if (amount == 0) {
                break;
            }
        }
        if (amount > 0) {
            itemStack.setAmount(amount);
            return itemStack;
        } else {
            return null;
        }
    }
}
