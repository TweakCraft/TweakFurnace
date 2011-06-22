package net.tweakcraft.TweakFurnace.Listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Edoxile
 */
public class TFBlockListener extends BlockListener {
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
        if (event.getNewCurrent() != event.getOldCurrent()
                && !(event.getOldCurrent() > 0 && event.getOldCurrent() > 0) && event.getNewCurrent() > 0) {
            Block redstoneBlock = event.getBlock();
            Block tempBlock;
            ArrayList<Furnace> furnaceList = new ArrayList<Furnace>();
            for (int dx = -1; dx < 1; dx++) {
                tempBlock = redstoneBlock.getRelative(dx, 0, 0);
                if (tempBlock.getType() == Material.FURNACE) {
                    furnaceList.add((Furnace) tempBlock.getState());
                }
            }
            for (int dz = -1; dz < 1; dz++) {
                tempBlock = redstoneBlock.getRelative(0, 0, dz);
                if (tempBlock.getType() == Material.FURNACE) {
                    furnaceList.add((Furnace) tempBlock.getState());
                }
            }
            tempBlock = redstoneBlock.getRelative(0, 0, 1);
            if (tempBlock.getType() == Material.FURNACE) {
                furnaceList.add((Furnace) tempBlock.getState());
            }

            for (Furnace f : furnaceList) {
                ItemStack stack = f.getInventory().getContents()[2];
                f.getInventory().setItem(2, null);
                f.getWorld().dropItem(f.getBlock().getLocation(), stack);
            }
        }
    }
}
