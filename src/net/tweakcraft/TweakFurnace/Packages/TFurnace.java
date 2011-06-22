package net.tweakcraft.TweakFurnace.Packages;

import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;

/**
 * @author GuntherDW
 */
public class TFurnace {

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

    /**
     * Put a fuel into this furnace
     * @param fuel
     * @return The amount of fuel that didn't fit in the furnace
     */
    public ItemStack putFuel(ItemStack fuel) {
        if(this.getFuel().getTypeId()!=fuel.getTypeId() &&
           this.getFuel().getDurability()!=fuel.getDurability())
            return fuel;

        int amountinfurnace = this.getFuel().getAmount();
        int amount = fuel.getAmount();
        while(fuel.getAmount()>0 && amountinfurnace<maxstack) {
            amountinfurnace++; amount--;
        }
        this.getFuel().setAmount(amountinfurnace);

        if(amount==0)
            return null;
        else {
            fuel.setAmount(amount);
            return fuel;
        }

    }
}
