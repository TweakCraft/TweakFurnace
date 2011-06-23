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
     * @param fuel
     * @return The amount of fuel that didn't fit in the furnace
     */
    public ItemStack putFuel(ItemStack fuel) {
        /* if(this.getFuel().getTypeId()!=fuel.getTypeId() &&
           this.getFuel().getDurability()!=fuel.getDurability())
            return fuel; */
        if(this.getFuel()!=null && fuel.getTypeId() == this.getFuel().getTypeId()
                && this.getFuel().getDurability() == fuel.getDurability())
            return fuel;

        if(this.getFuel()==null) {
            int amountinfurnace = 0;
            int amount = fuel.getAmount();
            while(fuel.getAmount()>0 && amountinfurnace<maxstack) {
                amountinfurnace++; amount--;
            }
            ItemStack furnaceFuel = new ItemStack(fuel.getTypeId(), amountinfurnace);
            this.setFuel(furnaceFuel);
            fuel.setAmount(amount);
            return fuel;
        } else {
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

    public ItemStack putSmelt(ItemStack smelt) {
        if(this.getSmelt()!=null && smelt.getTypeId() == this.getSmelt().getTypeId()
                && this.getSmelt().getDurability() == smelt.getDurability())
            return smelt;

        if(this.getSmelt()==null) {
            int amountinfurnace = 0;
            int amount = smelt.getAmount();
            while(smelt.getAmount()>0 && amountinfurnace<maxstack) {
                amountinfurnace++; amount--;
            }
            ItemStack furnaceSmelt = new ItemStack(smelt.getTypeId(), amountinfurnace);
            this.setSmelt(furnaceSmelt);
            smelt.setAmount(amount);
            return smelt;
        } else {
            int amountinfurnace = this.getSmelt().getAmount();
            int amount = smelt.getAmount();
            while(smelt.getAmount()>0 && amountinfurnace<maxstack) {
                amountinfurnace++; amount--;
            }
            this.getSmelt().setAmount(amountinfurnace);
            if(amount==0)
                return null;
            else {
                smelt.setAmount(amount);
                return smelt;
            }
        }
    }

    public Furnace getFurnace() {
        return furnace;
    }

}
