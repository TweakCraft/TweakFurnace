package net.tweakcraft.TweakFurnace.Listeners;

import net.tweakcraft.TweakFurnace.TweakFurnace;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerListener;

/**
 * @author GuntherDW
 */
public class TFPlayerListener extends PlayerListener {

    private TweakFurnace plugin;

    public TFPlayerListener(TweakFurnace instance) {
        this.plugin = instance;
    }

    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        
    }

}
