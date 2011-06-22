package net.tweakcraft.TweakFurnace;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GuntherDW
 */
public class Items {

    private Integer[] fuels = {Material.LOG.getId(), Material.STICK.getId(), Material.SAPLING.getId(), Material.WORKBENCH.getId(), Material.CHEST.getId(), Material.COAL.getId(), Material.BOOKSHELF.getId(), Material.WOOD.getId()};
    private Integer[] smeltables = {Material.IRON_ORE.getId(), Material.GOLD_ORE.getId(), Material.CACTUS.getId(), Material.COBBLESTONE.getId(), Material.SAND.getId(), Material.CLAY_BALL.getId(), Material.RAW_FISH.getId(), Material.PORK.getId()};
    
    public List<Integer> getFuels() {
        List<Integer> result = new ArrayList<Integer>();
        for(Integer i : fuels)
            result.add(i);
        return result;
    }

    public List<Integer> getSmeltables() {
        List<Integer> result = new ArrayList<Integer>();
        for(Integer i : smeltables)
            result.add(i);
        return result;
    }

    public boolean isFuel(Integer blockId) {
        return blockId!=null?getFuels().contains(blockId):false;
    }

    public boolean isSmeltable(Integer blockId) {
        return blockId!=null?getSmeltables().contains(blockId):false;
    }
}
