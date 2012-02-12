package net.tweakcraft.TweakFurnace.Listeners;

import net.tweakcraft.TweakFurnace.Packages.TFInventoryUtils;
import net.tweakcraft.TweakFurnace.Packages.TFurnace;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Edoxile, GuntherDW
 */
public class TFInventoryListener implements Listener {

    private static final Logger log = Logger.getLogger("Minecraft");

    public TFInventoryListener() {
    }
    
    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        if (event.isCancelled())
            return;

        TFurnace furnace = new TFurnace((Furnace) event.getFurnace().getState());

        if (event.isCancelled()
                || (furnace.getSmelt() != null && furnace.getSmelt().getAmount() > 1)
                || (furnace.getResult() != null && furnace.getResult().getAmount() == 32)
                || furnace.getLeftBlock().getTypeId() != Material.CHEST.getId()
                || furnace.getRightBlock().getTypeId() != Material.CHEST.getId()
                || furnace.getBackBlock().getTypeId() != Material.CHEST.getId())
            return;

        if (furnace.getResult().getAmount() == 32) {
            log.info("[TweakFurnace] Wait whut?");
            ItemStack stack = furnace.getResult().clone();
            stack.setAmount(32);
            stack.setAmount(TFInventoryUtils.addMaterials(null, (Chest) furnace.getRightBlock().getState(), stack));
            furnace.setResult(stack);
        }

        if (furnace.getSmelt().getAmount() == 1 && furnace.getSmelt().getTypeId() != Material.AIR.getId()) {
            ItemStack stack = furnace.getSmelt().clone();
            stack.setAmount(32);
            stack.setAmount(32 - TFInventoryUtils.removeMaterials(null, (Chest) furnace.getLeftBlock().getState(), stack) + 1);
            furnace.setSmelt(stack);
        }

    }
    
    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        if (event.isCancelled())
            return;
        
        TFurnace furnace = new TFurnace((Furnace) event.getFurnace().getState());

        if (event.isCancelled()
                || (furnace.getFuel() != null && furnace.getFuel().getAmount() > 1)
                || furnace.getLeftBlock().getTypeId() != Material.CHEST.getId()
                || furnace.getRightBlock().getTypeId() != Material.CHEST.getId()
                || furnace.getBackBlock().getTypeId() != Material.CHEST.getId())
            return;

        if (furnace.getFuel().getAmount() == 1 && furnace.getFuel().getTypeId() != Material.AIR.getId()) {
            ItemStack stack = furnace.getFuel().clone();
            stack.setAmount(32);
            stack.setAmount(32 - TFInventoryUtils.removeMaterials(null, (Chest) furnace.getBackBlock().getState(), stack) + 1);
            furnace.setFuel(stack);
        }
    }
}