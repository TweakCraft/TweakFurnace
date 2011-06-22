package net.tweakcraft.TweakFurnace;

import net.tweakcraft.TweakFurnace.Listeners.TFBlockListener;
import net.tweakcraft.TweakFurnace.Listeners.TFPlayerListener;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * @author: Edoxile, GuntherDW
 */
public class TweakFurnace extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");
    private TFPlayerListener playerListener = new TFPlayerListener(this);
    private TFBlockListener blockListener = new TFBlockListener(this);
    private PluginDescriptionFile pdfile = null;
    private Items items = new Items();

    public void onDisable() {
        log.info("["+pdfile.getName()+"] Disabled.");
    }

    public void onEnable() {
        pdfile = this.getDescription();
        log.info("["+pdfile.getName()+"] "+pdfile.getName()+" Enabled. Version: " + pdfile.getVersion());
    }

    public void registerEvents() {
        this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Event.Priority.Normal, this);
    }

    public Items getItems() {
        return items;
    }
}
