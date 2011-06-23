package net.tweakcraft.TweakFurnace.Listeners;

import net.tweakcraft.TweakFurnace.Packages.Items;
import net.tweakcraft.TweakFurnace.Packages.TFurnace;
import net.tweakcraft.TweakFurnace.TweakFurnace;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

/**
 * @author GuntherDW
 */
public class TFPlayerListener extends PlayerListener {

    private TweakFurnace plugin;

    public TFPlayerListener(TweakFurnace instance) {
        this.plugin = instance;
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.isCancelled()) return;
        if(event.getAction() != Action.LEFT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();

        if(block instanceof Furnace) {
            TFurnace furnace = new TFurnace((Furnace) block.getState());
            ItemStack hand = event.getItem();
            if(hand!=null) {
                if(Items.isFuel(hand.getTypeId())) {
                    ItemStack leftover = furnace.putFuel(hand);
                    if(!leftover.equals(hand)) // We don't need to change the hand if it wasn't changed
                        event.getPlayer().setItemInHand(hand);
                } else if(Items.isSmeltable(hand.getTypeId())) {
                    ItemStack leftover = furnace.putSmelt(hand);
                    if(!leftover.equals(hand)) // We don't need to change the hand if it wasn't changed
                        event.getPlayer().setItemInHand(leftover);
                }
            }
        }
    }

}
