package net.tweakcraft.TweakFurnace.Packages;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

/**
 * @author GuntherDW, Edoxile
 */
public class TFurnace {

    private static final Logger log = Logger.getLogger("Minecraft");

    private Furnace furnace;

    private int maxstack = 64;

    public TFurnace(Furnace furnace) {
        this.furnace = furnace;
    }

    public ItemStack getSmelt() {
        return this.furnace.getInventory().getItem(0);
    }

    public ItemStack getFuel() {
        return this.furnace.getInventory().getItem(1);
    }

    public ItemStack getResult() {
        return this.furnace.getInventory().getItem(2);
    }

    public void setFuel(ItemStack stack) {
        this.furnace.getInventory().setItem(1, stack);
    }

    public void setSmelt(ItemStack stack) {
        this.furnace.getInventory().setItem(0, stack);
    }

    public void setResult(ItemStack stack) {
        this.furnace.getInventory().setItem(2, stack);
    }

    /**
     * Put a fuel into this furnace
     *
     * @param fuel
     * @return The amount of fuel that didn't fit in the furnace
     */
    public ItemStack putFuel(ItemStack fuel) {
        if (getFuel() != null && (fuel.getTypeId() != getFuel().getTypeId()
                || getFuel().getDurability() != fuel.getDurability())
                && getFuel().getTypeId() != Material.AIR.getId())
            return fuel;

        if (getFuel() == null || getFuel().getTypeId() == Material.AIR.getId()) {
            int amountinfurnace = 0;
            int amount = fuel.getAmount();
            while (amount > 0 && amountinfurnace < maxstack) {
                amountinfurnace++;
                amount--;
            }
            ItemStack furnaceFuel = new ItemStack(fuel.getTypeId(), amountinfurnace);
            setFuel(furnaceFuel);
            if (amount == 0) {
                return null;
            } else {
                fuel.setAmount(amount);
                return fuel;
            }
        } else {
            int amountinfurnace = this.getFuel().getAmount();
            int amount = fuel.getAmount();
            while (amount > 0 && amountinfurnace < maxstack) {
                amountinfurnace++;
                amount--;
            }
            ItemStack furnaceFuel = getFuel();
            furnaceFuel.setAmount(amountinfurnace);
            setFuel(furnaceFuel);
            if (amount == 0) {
                return null;
            } else {
                fuel.setAmount(amount);
                return fuel;
            }
        }
    }

    public ItemStack putSmelt(ItemStack smelt) {
        if ((getSmelt() != null && smelt.getTypeId() != getSmelt().getTypeId()
                && getSmelt().getDurability() != smelt.getDurability())
                && getSmelt().getTypeId() != Material.AIR.getId())
            return smelt;

        if (getSmelt() == null || getSmelt().getTypeId() == Material.AIR.getId()) {
            int amountinfurnace = 0;
            int amount = smelt.getAmount();
            while (amount > 0 && amountinfurnace < maxstack) {
                amountinfurnace++;
                amount--;
            }
            ItemStack furnaceSmelt = new ItemStack(smelt.getTypeId(), amountinfurnace);
            setSmelt(furnaceSmelt);
            if (amount == 0) {
                return null;
            } else {
                smelt.setAmount(amount);
                return smelt;
            }
        } else {
            int amountinfurnace = getSmelt().getAmount();
            int amount = smelt.getAmount();
            while (amount > 0 && amountinfurnace < maxstack) {
                amountinfurnace++;
                amount--;
            }
            ItemStack furnaceSmelt = getSmelt();
            setSmelt(furnaceSmelt);
            if (amount == 0) {
                return null;
            } else {
                smelt.setAmount(amount);
                return smelt;
            }
        }
    }

    public Furnace getFurnace() {
        return furnace;
    }

    public static boolean isFurnace(Block block) {
        return block.getTypeId() == Material.BURNING_FURNACE.getId() || block.getTypeId() == Material.FURNACE.getId();
    }

    public void dropResult() {
        ItemStack result = getResult();
        if (result != null && result.getAmount() > 0 && result.getTypeId() != Material.AIR.getId()) {
            /**
             * TODO: log loot drop; finding player near block and log them (anti-grief)
             */
            furnace.getWorld().dropItemNaturally(getFurnaceDropLocation(), result);
            setResult(null);
        }
    }

    private Location getFurnaceDropLocation() {
        Location l = furnace.getBlock().getLocation();
        l.setX(l.getBlockX());
        l.setY(l.getBlockY());
        l.setZ(l.getBlockZ());
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
