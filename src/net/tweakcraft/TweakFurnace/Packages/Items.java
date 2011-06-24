package net.tweakcraft.TweakFurnace.Packages;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author GuntherDW
 */
public class Items {

    private final static List<Integer> fuels = Arrays.asList(new Integer[]{Material.LOG.getId(), Material.STICK.getId(), Material.SAPLING.getId(), Material.WORKBENCH.getId(), Material.CHEST.getId(), Material.COAL.getId(), Material.BOOKSHELF.getId(), Material.WOOD.getId(), Material.LAVA_BUCKET.getId()});
    private final static List<Integer> smeltables = Arrays.asList(new Integer[]{Material.IRON_ORE.getId(), Material.GOLD_ORE.getId(), Material.CACTUS.getId(), Material.COBBLESTONE.getId(), Material.SAND.getId(), Material.CLAY_BALL.getId(), Material.RAW_FISH.getId(), Material.PORK.getId()});

    public static List<Integer> getSmeltables() {
        return smeltables;
    }

    public static List<Integer> getFuels() {
        return fuels;
    }

    public static boolean isFuel(Integer blockId) {
        return blockId != null ? fuels.contains(blockId) : false;
    }

    public static boolean isSmeltable(Integer blockId) {
        return blockId != null ? smeltables.contains(blockId) : false;
    }

    public static int getOptimumSmeltAmount(Integer blockId) {
        Material m = Material.getMaterial(blockId);
        switch (m) {
            case COAL:
                return 8;
            case LAVA_BUCKET:
                return 1;
            case LOG:
            case BOOKSHELF:
            case WORKBENCH:
            case CHEST:
            case WOOD:
                return 43;
            case STICK:
            case SAPLING:
                return 128;
            default:
                return 0;
        }
    }
}
