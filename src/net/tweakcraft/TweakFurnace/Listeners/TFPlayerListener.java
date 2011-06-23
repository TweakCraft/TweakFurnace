package net.tweakcraft.TweakFurnace.Listeners;

import net.tweakcraft.TweakFurnace.Packages.Items;
import net.tweakcraft.TweakFurnace.Packages.TFurnace;
import net.tweakcraft.TweakFurnace.TweakFurnace;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

/**
 * @author GuntherDW
 */
public class TFPlayerListener extends PlayerListener {

    private static final Logger log = Logger.getLogger("Minecraft");

    private TweakFurnace plugin;

    public TFPlayerListener(TweakFurnace instance) {
        this.plugin = instance;
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();

        if (TFurnace.isFurnace(block)) {
            TFurnace furnace = new TFurnace((Furnace) block.getState());
            ItemStack hand = event.getItem();
            if (hand != null && hand.getAmount() > 0 && hand.getTypeId() != Material.AIR.getId()) {
                if (Items.isFuel(hand.getTypeId())) {
                    ItemStack leftover = furnace.putFuel(hand);
                    event.getPlayer().setItemInHand(leftover);
                } else if (Items.isSmeltable(hand.getTypeId())) {
                    ItemStack leftover = furnace.putSmelt(hand);
                    event.getPlayer().setItemInHand(leftover);
                }
            }
        }
    }

}
