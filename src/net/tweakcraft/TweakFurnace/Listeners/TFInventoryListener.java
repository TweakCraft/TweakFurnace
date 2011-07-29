package net.tweakcraft.TweakFurnace.Listeners;

import net.tweakcraft.TweakFurnace.Packages.Items;
import net.tweakcraft.TweakFurnace.Packages.TFurnace;
import net.tweakcraft.TweakFurnace.TweakFurnace;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryListener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
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

    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        if (event.isCancelled())
            return;

        TFurnace furnace = new TFurnace((Furnace) event.getFurnace().getState());

        if (event.isCancelled()
                || furnace.getSmelt().getAmount() > 1
                || furnace.getLeftBlock().getTypeId() != Material.CHEST.getId()
                || furnace.getRightBlock().getTypeId() != Material.CHEST.getId()
                || furnace.getBackBlock().getTypeId() != Material.CHEST.getId())
            return;

        ItemStack smeltStack = event.getResult();
        if (smeltStack != null) {
            smeltStack.setAmount(smeltStack.getAmount() + 1);
            if (smeltStack.getAmount() == 64) {
                Chest result = (Chest) furnace.getRightBlock().getState();
                HashMap<Integer, ItemStack> stackHashMap = result.getInventory().addItem(smeltStack);
                if (stackHashMap.isEmpty())
                    event.setResult(null);
                else
                    event.setResult(stackHashMap.get(0));
            }
        } else {
            furnace.setResult(event.getResult());
        }

        smeltStack = null;

        Chest smelt = (Chest) furnace.getLeftBlock().getState();
        ItemStack[] inv = smelt.getInventory().getContents();

        int maxStackSize = 64;
        for (int index = 0; index < inv.length; index++) {
            ItemStack i = inv[index];
            if (i == null || !Items.isSmeltable(i.getTypeId()))
                continue;
            if (smeltStack == null) {
                smeltStack = i;
                inv[index] = null;
            } else {
                if (smeltStack.getTypeId() != i.getTypeId() || smeltStack.getDurability() != i.getDurability())
                    continue;
                if (smeltStack.getAmount() + i.getAmount() > maxStackSize) {
                    smeltStack.setAmount(maxStackSize);
                    if ((i.getAmount() + smeltStack.getAmount() - maxStackSize) == 0) {
                        inv[index] = null;
                    } else {
                        //Something wrong here...
                        inv[index].setAmount(i.getAmount() + smeltStack.getAmount() - maxStackSize);
                    }
                } else {
                    smeltStack.setAmount(smeltStack.getAmount() + i.getAmount());
                    inv[index] = null;
                }
            }
            if (smeltStack.getAmount() == maxStackSize) {
                break;
            }
        }
        smelt.getInventory().setContents(inv);
        furnace.setSmelt(smeltStack);
        event.setCancelled(true);
    }

    /**
     * TODO: fix the infini-bug
     *
     * @param event
     */
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        if (event.isCancelled())
            return;

        TFurnace furnace = new TFurnace((Furnace) event.getFurnace().getState());

        switch (event.getFuel().getType()) {
            case REDSTONE:
                event.setBurnTime(3200);
                return;
            case DIAMOND:
                event.setBurnTime(12800);
                return;
        }

        if (event.isCancelled()
                || furnace.getFuel().getTypeId() != Material.AIR.getId()
                || furnace.getResult().getAmount() == 64
                || furnace.getLeftBlock().getTypeId() != Material.CHEST.getId()
                || furnace.getRightBlock().getTypeId() != Material.CHEST.getId()
                || furnace.getBackBlock().getTypeId() != Material.CHEST.getId())
            return;

        Chest fuel = (Chest) furnace.getBackBlock().getState();
        ItemStack[] inv = fuel.getInventory().getContents();
        ItemStack fuelStack = null;
        int maxStackSize = 0;
        for (int index = 0; index < inv.length; index++) {
            ItemStack i = inv[index];
            if (i == null || !Items.isFuel(i.getTypeId()))
                continue;
            if (fuelStack == null) {
                fuelStack = i;
                maxStackSize = i.getTypeId() == Material.LAVA_BUCKET.getId() ? 1 : 64;
                inv[index] = null;
            } else {
                if (fuelStack.getTypeId() != i.getTypeId() || fuelStack.getDurability() != i.getDurability())
                    continue;
                if (fuelStack.getAmount() + i.getAmount() > maxStackSize) {
                    fuelStack.setAmount(maxStackSize);
                    if ((i.getAmount() + fuelStack.getAmount() - maxStackSize) == 0) {
                        inv[index] = null;
                    } else {
                        //Something wrong here...
                        inv[index].setAmount(i.getAmount() + fuelStack.getAmount() - maxStackSize);
                    }
                } else {
                    fuelStack.setAmount(fuelStack.getAmount() + i.getAmount());
                    inv[index] = null;
                }
            }
            if (fuelStack.getAmount() == maxStackSize) {
                break;
            }
        }
        fuel.getInventory().setContents(inv);
        furnace.setFuel(fuelStack);
        event.setBurnTime(TFurnace.getBurnTime(fuelStack));
    }
}