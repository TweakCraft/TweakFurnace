package net.tweakcraft.TweakFurnace.Listeners;

import net.tweakcraft.TweakFurnace.TweakFurnace;
import org.bukkit.Location;
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

    private TweakFurnace plugin;

    public TFBlockListener(TweakFurnace instance) {
        this.plugin = instance;
    }

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
                f.getWorld().dropItem(getFurnaceDropLocation(f), stack);
            }
        }
    }

    private Location getFurnaceDropLocation(Furnace furnace) {
        Location l = furnace.getBlock().getLocation();
        switch (furnace.getBlock().getData()) {
            case 0x2:
                l.setZ(l.getZ() - 1);
                break;
            case 0x3:
                l.setZ(l.getZ() + 1);
                break;
            case 0x4:
                l.setX(l.getX() - 1);
                break;
            case 0x5:
                l.setX(l.getX() + 1);
                break;
            default:
                break;
        }
        return l;
    }
}
