package net.tweakcraft.TweakFurnace.Listeners;

import net.tweakcraft.TweakFurnace.Packages.Items;
import net.tweakcraft.TweakFurnace.Packages.TFurnace;
import net.tweakcraft.TweakFurnace.TweakFurnace;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.InventoryListener;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Edoxile
 */
public class TFInventoryListener extends InventoryListener {

    private static final Logger log = Logger.getLogger("Minecraft");

    private TweakFurnace plugin;

    public TFInventoryListener(TweakFurnace instance) {
        this.plugin = instance;
    }

    /**
     * TODO: fix the offset (?), chest
     *
     * @param event
     */
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        if (event.isCancelled())
            return;
        TFurnace furnace = new TFurnace((Furnace) event.getFurnace().getState());
        if (furnace.getFuel().getAmount() > 1
                || furnace.getLeftBlock().getTypeId() != Material.CHEST.getId()
                || furnace.getRightBlock().getTypeId() != Material.CHEST.getId()
                || furnace.getBackBlock().getTypeId() != Material.CHEST.getId())
            return;
        Chest fuel = (Chest) furnace.getBackBlock().getState();
        /*Chest smelt = (Chest) furnace.getLeftBlock().getState();
        Chest result = (Chest) furnace.getRightBlock().getState();*/
        ItemStack[] inv = fuel.getInventory().getContents();
        ItemStack fuelStack = furnace.getFuel();
        int maxStackSize = (fuelStack.getTypeId() == Material.LAVA_BUCKET.getId()) ? 1 : 64;
        for (int index = 0; index < inv.length; index++) {
            ItemStack i = inv[index];
            if (i == null || !Items.isFuel(i.getTypeId()))
                continue;
            if (fuelStack == null) {
                fuelStack = i;
                inv[index] = null;
            } else {
                if (fuelStack.getTypeId() != i.getTypeId() || fuelStack.getDurability() != i.getDurability())
                    continue;
                if (fuelStack.getAmount() + i.getAmount() > maxStackSize) {
                    fuelStack.setAmount(maxStackSize);
                    if ((i.getAmount() + fuelStack.getAmount() - maxStackSize) == 0) {
                        inv[index] = null;
                    } else {
                        inv[index].setAmount(i.getAmount() + fuelStack.getAmount() - maxStackSize);
                    }
                } else {
                    fuelStack.setAmount(fuelStack.getAmount() + i.getAmount());
                    inv[index] = null;
                }
            }
            if (fuelStack.getAmount() == maxStackSize) {
                fuelStack.setAmount(maxStackSize);
                break;
            }
        }
        log.info("Removing: " + fuelStack.toString());
        fuel.getInventory().setContents(inv);
        if (fuelStack.getAmount() != maxStackSize) {
            furnace.setFuel(fuelStack);
        } else {
            furnace.setFuel(fuelStack);
            new FurnaceFillThread(furnace, TFurnace.invSpot.FUEL, fuelStack);
        }
    }
}

class FurnaceFillThread implements Runnable {
    Thread thread;
    TFurnace furnace;
    TFurnace.invSpot spot;
    ItemStack item;

    public FurnaceFillThread(TFurnace f, TFurnace.invSpot s, ItemStack i) {
        furnace = f;
        spot = s;
        item = i;
        thread = new Thread(this, "FurnaceFillThread");
        thread.start();
    }

    public void run() {
        try {
            System.out.println("Starting thread");
            Thread.sleep(2);
            ItemStack fuel = furnace.getFuel();
            if (fuel == null) {
                System.out.println("Setting fuel to 1");
                item.setAmount(1);
            } else {
                item.setAmount(fuel.getAmount() + 1);
                System.out.println("Setting fuel to " + (fuel.getAmount() + 1));
            }
            furnace.setItem(spot, item);
        } catch (InterruptedException e) {
            System.out.println("Exception in FurnaceFillThread: " + e.getMessage());
        }
    }
}