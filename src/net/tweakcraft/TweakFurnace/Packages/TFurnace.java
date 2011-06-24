package net.tweakcraft.TweakFurnace.Packages;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
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
        ItemStack smelt = this.furnace.getInventory().getItem(0);
        if (smelt == null || smelt.getTypeId() == 0)
            return null;
        else
            return smelt;
    }

    public ItemStack getFuel() {
        ItemStack fuel = this.furnace.getInventory().getItem(1);
        if (fuel == null || fuel.getTypeId() == 0)
            return null;
        else
            return fuel;
    }

    public ItemStack getResult() {
        ItemStack result = this.furnace.getInventory().getItem(2);
        if (result == null || result.getTypeId() == 0)
            return null;
        else
            return result;
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
        if ((this.getFuel() != null && fuel.getTypeId() != this.getFuel().getTypeId())
                && this.getFuel().getDurability() != fuel.getDurability())
            return fuel;

        int amount = 0;
        int amountinfurnace = 0;
        ItemStack newFuel = null;
        boolean updateCount = false;

        if (this.getFuel() == null) {
            amountinfurnace = 0;
            amount = fuel.getAmount();
            newFuel = new ItemStack(fuel.getTypeId(), 0, fuel.getDurability());
        } else {
            amountinfurnace = this.getFuel().getAmount();
            amount = fuel.getAmount();
            newFuel = this.getFuel();
            updateCount = true;
        }

        while (amount > 0 && amountinfurnace < maxstack) {
            amount--;
            amountinfurnace++;
        }

        if (updateCount)
            this.getFuel().setAmount(amountinfurnace);
        else {
            newFuel.setAmount(amountinfurnace);
            this.setFuel(newFuel);
        }

        if (amount == 0)
            return null;
        else {
            fuel.setAmount(amount);
            return fuel;
        }
    }


    public ItemStack putSmelt(ItemStack smelt) {
        if ((this.getSmelt() != null && smelt.getTypeId() != this.getSmelt().getTypeId())
                && this.getSmelt().getDurability() != smelt.getDurability())
            return smelt;

        int amount = 0;
        int amountinfurnace = 0;
        ItemStack newSmelt = null;
        boolean updateCount = false;

        if (this.getSmelt() == null) {
            amountinfurnace = 0;
            amount = smelt.getAmount();
            newSmelt = new ItemStack(smelt.getTypeId(), 0, smelt.getDurability());
        } else {
            amountinfurnace = this.getSmelt().getAmount();
            amount = smelt.getAmount();
            newSmelt = this.getSmelt();
            updateCount = true;
        }

        while (amount > 0 && amountinfurnace < maxstack) {
            amount--;
            amountinfurnace++;
        }

        if (updateCount)
            this.getFuel().setAmount(amountinfurnace);
        else {
            newSmelt.setAmount(amountinfurnace);
            this.setSmelt(newSmelt);
        }

        if (amount == 0)
            return null;
        else {
            smelt.setAmount(amount);
            return smelt;
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

    private BlockFace getFurnaceOrientation() {
        switch (furnace.getBlock().getData()) {
            case 0x2:
                return BlockFace.EAST;
            case 0x3:
                return BlockFace.WEST;
            case 0x4:
                return BlockFace.NORTH;
            case 0x5:
                return BlockFace.SOUTH;
            default:
                return BlockFace.SELF;
        }
    }

    private Chest getSmeltChest() {
        BlockFace face;
        switch (getFurnaceOrientation()) {
            case EAST:
                face = BlockFace.NORTH;
                break;
            case WEST:
                face = BlockFace.SOUTH;
                break;
            case NORTH:
                face = BlockFace.WEST;
                break;
            case SOUTH:
                face = BlockFace.EAST;
                break;
            default:
                face = BlockFace.SELF;
                break;
        }
        if (furnace.getBlock().getRelative(face).getTypeId() == Material.CHEST.getId()) {
            return (Chest) furnace.getBlock().getRelative(face).getState();
        } else {
            return null;
        }
    }

    private Chest getFuelChest() {
        BlockFace face;
        switch (getFurnaceOrientation()) {
            case EAST:
                face = BlockFace.WEST;
                break;
            case WEST:
                face = BlockFace.EAST;
                break;
            case NORTH:
                face = BlockFace.SOUTH;
                break;
            case SOUTH:
                face = BlockFace.NORTH;
                break;
            default:
                face = BlockFace.SELF;
                break;
        }
        if (furnace.getBlock().getRelative(face).getTypeId() == Material.CHEST.getId()) {
            return (Chest) furnace.getBlock().getRelative(face).getState();
        } else {
            return null;
        }
    }

    private Chest getLootChest() {
        BlockFace face;
        switch (getFurnaceOrientation()) {
            case EAST:
                face = BlockFace.SOUTH;
                break;
            case WEST:
                face = BlockFace.NORTH;
                break;
            case NORTH:
                face = BlockFace.EAST;
                break;
            case SOUTH:
                face = BlockFace.WEST;
                break;
            default:
                face = BlockFace.SELF;
                break;
        }
        if (furnace.getBlock().getRelative(face).getTypeId() == Material.CHEST.getId()) {
            return (Chest) furnace.getBlock().getRelative(face).getState();
        } else {
            return null;
        }
    }
}
