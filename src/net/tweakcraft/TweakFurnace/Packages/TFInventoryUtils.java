package net.tweakcraft.TweakFurnace.Packages;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Edoxile
 */
public class TFInventoryUtils {
    private static HashMap<Player, Integer> playerAmountMap = new HashMap<Player,Integer>();

    public static void setCustomPlayerAmount(Player player, int amount){
        playerAmountMap.put(player, amount);
    }

    public static void removeCustomPlayerAmount(Player player){
        if(playerAmountMap.containsKey(player))
            playerAmountMap.remove(player);
    }
    
    public static int getCustomPlayerAmount(Player player) {
        if(playerAmountMap.containsKey(player))
            return playerAmountMap.get(player);
        else
            return 64;
    }

    public static int removeMaterials(Player player, Chest chest, ItemStack toRemove) {
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

    public static int addMaterials(Player player, Chest chest, ItemStack toAdd) {
        int putAmount = playerAmountMap.get(player);
        if(putAmount < 1){
            putAmount = 64;
        }
        if (toAdd == null)
            return 0;
        ItemStack[] stacks = chest.getInventory().getContents();
        for (int index = 0; index < stacks.length; index++) {
            if (stacks[index] == null) {
                stacks[index] = toAdd;
                toAdd = null;
                break;
            } else {
                if (stacks[index].getTypeId() == toAdd.getTypeId() && stacks[index].getDurability() == toAdd.getDurability() && stacks[index].getAmount() < putAmount) {
                    if (stacks[index].getAmount() + toAdd.getAmount() > putAmount) {
                        toAdd.setAmount(toAdd.getAmount() + stacks[index].getAmount() - putAmount);
                        stacks[index].setAmount(putAmount);
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
