package net.tweakcraft.TweakFurnace.Listeners;

import net.tweakcraft.TweakFurnace.TweakFurnace;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

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
            Furnace furnace = (Furnace) block;
        }
    }

}
