package net.tweakcraft;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Edoxile
 */
public class TweakFurnace extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");

    public void onDisable() {
        log.info("[TweakFurnace] Disabled.");
    }

    public void onEnable() {
        //Register events etc.

        log.info("[TweakFurnace] Enabled. Version: " + getDescription().getVersion());
    }
}
