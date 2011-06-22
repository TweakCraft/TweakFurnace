package net.tweakcraft.TweakFurnace;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author GuntherDW
 */
public class Items {

    private List<Integer> fuels;
    private List<Integer> smeltables;

    public Items() {
        this.fuels = Arrays.asList(new Integer[]{Material.LOG.getId(), Material.STICK.getId(), Material.SAPLING.getId(), Material.WORKBENCH.getId(), Material.CHEST.getId(), Material.COAL.getId(), Material.BOOKSHELF.getId(), Material.WOOD.getId()});
        this.smeltables = Arrays.asList(new Integer[]{Material.IRON_ORE.getId(), Material.GOLD_ORE.getId(), Material.CACTUS.getId(), Material.COBBLESTONE.getId(), Material.SAND.getId(), Material.CLAY_BALL.getId(), Material.RAW_FISH.getId(), Material.PORK.getId()});
    }

    public List<Integer> getFuelsAsList() {
        List<Integer> result = new ArrayList<Integer>();
        for(Integer i : fuels)
            result.add(i);
        return result;
    }

    public List<Integer> getSmeltablesAsList() {
        List<Integer> result = new ArrayList<Integer>();
        for(Integer i : smeltables)
            result.add(i);
        return result;
    }



    public List<Integer> getSmeltables() {
        return smeltables;
    }

    public List<Integer> getFuels() {
        return fuels;
    }

    public boolean isFuel(Integer blockId) {
        return blockId!=null?fuels.contains(blockId):false;
    }

    public boolean isSmeltable(Integer blockId) {
        return blockId!=null?smeltables.contains(blockId):false;
    }
}
