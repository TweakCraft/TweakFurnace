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

    private Integer getMaxstack(Material mat) {
        if(mat!=null)
            return mat.getMaxStackSize();
        return null;
    }

    private enum invSpot {
        SMELT,
        FUEL,
        RESULT;

        public int getSpot() { return this.ordinal(); }
    }

    public TFurnace(Furnace furnace) {
        this.furnace = furnace;
    }

    public ItemStack getSmelt() {
        ItemStack smelt = this.furnace.getInventory().getItem(invSpot.SMELT.getSpot());
        if (smelt == null || smelt.getTypeId() == 0)
            return null;
        else
            return smelt;
    }

    public ItemStack getFuel() {
        ItemStack fuel = this.furnace.getInventory().getItem(invSpot.FUEL.getSpot());
        if (fuel == null || fuel.getTypeId() == 0)
            return null;
        else
            return fuel;
    }

    public ItemStack getResult() {
        ItemStack result = this.furnace.getInventory().getItem(invSpot.RESULT.getSpot());
        if (result == null || result.getTypeId() == 0)
            return null;
        else
            return result;
    }

    public ItemStack getItem(int id) {

        if(id>=0 && id<3) {
            ItemStack result = this.furnace.getInventory().getItem(id);
            if(result != null && result.getType() != Material.AIR) {
                return result;
            }
        }
        return null;
    }

    public void updateCount(int pos, int amount) {
        if(pos>=0 && pos<3) {
            if(this.furnace.getInventory().getItem(pos)!=null&&
                    this.furnace.getInventory().getItem(pos).getType()!=Material.AIR) {
                this.getFurnace().getInventory().getItem(pos).setAmount(amount);
            }
        }
    }

    public void setItem(int id, ItemStack stack) {
        if(id>=0 && id<3) {
            this.furnace.getInventory().setItem(id, stack);
        }
    }

    public void setFuel(ItemStack stack) {
        this.furnace.getInventory().setItem(invSpot.FUEL.getSpot(), stack);
    }

    public void setSmelt(ItemStack stack) {
        this.furnace.getInventory().setItem(invSpot.SMELT.getSpot(), stack);
    }

    public void setResult(ItemStack stack) {
        this.furnace.getInventory().setItem(invSpot.RESULT.getSpot(), stack);
    }

    public ItemStack putFuel(ItemStack stack) {
        return this.putInFurnace(invSpot.FUEL.getSpot(), stack);
    }

    public ItemStack putSmelt(ItemStack stack) {
        return this.putInFurnace(invSpot.SMELT.getSpot(), stack);
    }

    /**
     * Puts something into this furnace
     *
     * @param stack
     * @return The amount of fuel that didn't fit in the furnace
     */
    public ItemStack putInFurnace(int invId, ItemStack stack) {
        ItemStack oldstack = this.getItem(invId);
        if ((oldstack != null && stack.getTypeId() != oldstack.getTypeId())
                && oldstack.getDurability() != stack.getDurability())
            return stack;

        int amount = 0;
        int amountinfurnace = 0;
        ItemStack newStack = null;
        boolean updateCount = false;

        if (oldstack == null) {
            amountinfurnace = 0;
            amount = stack.getAmount();
            newStack = new ItemStack(stack.getTypeId(), 0, stack.getDurability());
        } else {
            amountinfurnace = oldstack.getAmount();
            amount = stack.getAmount();
            newStack = oldstack.clone();
            updateCount = true;
        }
        int maxstack = getMaxstack(newStack.getType());

        while (amount > 0 && amountinfurnace < maxstack) {
            amount--;
            amountinfurnace++;
        }

        if (updateCount)
            this.updateCount(invId, amountinfurnace);
        else {
            newStack.setAmount(amountinfurnace);
            this.setItem(invId, newStack);
        }

        if (amount == 0)
            return null;
        else {
            stack.setAmount(amount);
            return stack;
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
