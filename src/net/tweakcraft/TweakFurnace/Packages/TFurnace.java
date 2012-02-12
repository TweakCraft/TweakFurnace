package net.tweakcraft.TweakFurnace.Packages;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;

/**
 * @author GuntherDW, Edoxile
 */
public class TFurnace {

    private Furnace furnace;

    private Integer getMaxstack(Material mat) {
        if (mat != null)
            return mat.getMaxStackSize();
        return null;
    }

    public enum invSpot {
        SMELT,
        FUEL,
        RESULT;

        public int getSpot() {
            return this.ordinal();
        }
    }

    public TFurnace(Furnace furnace) {
        this.furnace = furnace;
    }

    public ItemStack getSmelt() {
        return this.furnace.getInventory().getItem(invSpot.SMELT.getSpot());
    }

    public ItemStack getFuel() {
        return this.furnace.getInventory().getItem(invSpot.FUEL.getSpot());
    }

    public ItemStack getResult() {
        return this.furnace.getInventory().getItem(invSpot.RESULT.getSpot());
    }

    public ItemStack getItem(invSpot spot) {
        ItemStack result = this.furnace.getInventory().getItem(spot.getSpot());
        if (result != null && result.getType() != Material.AIR) {
            return result;
        }
        return null;
    }

    public void updateCount(invSpot spot, int amount) {
        if (this.furnace.getInventory().getItem(spot.getSpot()) != null &&
                this.furnace.getInventory().getItem(spot.getSpot()).getType() != Material.AIR) {
            this.getFurnace().getInventory().getItem(spot.getSpot()).setAmount(amount);
        }
    }

    public void setItem(invSpot spot, ItemStack stack) {
        this.furnace.getInventory().setItem(spot.getSpot(), stack);
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
        return this.putInFurnace(invSpot.FUEL, stack);
    }

    public ItemStack putSmelt(ItemStack stack) {
        return this.putInFurnace(invSpot.SMELT, stack);
    }

    /**
     * Puts something into this furnace
     *
     * @param stack
     * @return The amount of fuel that didn't fit in the furnace
     */
    public ItemStack putInFurnace(invSpot spot, ItemStack stack) {
        ItemStack oldstack = this.getItem(spot);
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
            this.updateCount(spot, amountinfurnace);
        else {
            newStack.setAmount(amountinfurnace);
            this.setItem(spot, newStack);
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

    public BlockFace getFurnaceOrientation() {
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

    public Block getLeftBlock() {
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
        return furnace.getBlock().getRelative(face);
    }

    public Block getBackBlock() {
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
        return furnace.getBlock().getRelative(face);
    }

    public Block getRightBlock() {
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
        return furnace.getBlock().getRelative(face);
    }
    @Deprecated
    public static int getBurnTime(ItemStack item) {
        if (item == null)
            return 0;
        else
            return getBurnTime(item.getType());
    }

    @Deprecated
    public static int getBurnTime(Material m) {
        switch (m) {
            case BLAZE_ROD:
                return 2400;
            case LAVA_BUCKET:
                return 20000;
            case STICK:
            case SAPLING:
                return 100;
            case WOOD:
            case LOG:
            case BOOKSHELF:
            case CHEST:
            case WORKBENCH:
            case FENCE:
            case WOOD_STAIRS:
            case NOTE_BLOCK:
            case JUKEBOX:
            case LOCKED_CHEST:
            case TRAP_DOOR:
                return 300;
            case COAL:
                return 1600;
            case REDSTONE:
                return 3200;
            case DIAMOND:
                return 12800;
            default:
                return 0;
        }
    }
}
