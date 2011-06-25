package net.tweakcraft.TweakFurnace;

import com.nijiko.permissions.PermissionHandler;
import com.zones.Zones;
import com.zones.model.ZoneBase;
import net.tweakcraft.TweakFurnace.Listeners.*;
import net.tweakcraft.TweakFurnace.Packages.Items;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: GuntherDW, Edoxile
 */
public class TweakFurnace extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");
    private TFPlayerListener playerListener = new TFPlayerListener(this);
    private TFBlockListener blockListener = new TFBlockListener(this);
    private TFInventoryListener inventoryListener = new TFInventoryListener(this);
    private HashSet<Player> muteList = new HashSet<Player>();
    private Zones zonesPlugin;
    private PermissionHandler permissionsPlugin;
    private PluginDescriptionFile pdfile = null;
    private Items items = new Items();

    public void onDisable() {
        log.info("[" + pdfile.getName() + "] Disabled.");
    }

    public void onEnable() {
        pdfile = this.getDescription();
        registerEvents();
        loadPlugins();
        log.info("[" + pdfile.getName() + "] " + pdfile.getName() + " Enabled. Version: " + pdfile.getVersion());
    }

    public void registerEvents() {
        this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Event.Priority.Normal, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.FURNACE_BURN, inventoryListener, Event.Priority.Monitor, this);
    }

    public void loadPlugins() {
        Plugin plugin = getServer().getPluginManager().getPlugin("Zones");
        if (plugin != null) {
            zonesPlugin = (Zones) plugin;
        } else {
            log.info("[TweakFurnace] No zones plugin found. Only checking for permissions!");
        }
        plugin = getServer().getPluginManager().getPlugin("Permissions");
        if (plugin != null) {
            permissionsPlugin = (PermissionHandler) plugin;
        } else {
            log.info("[TweakFurnace] No permissions plugin found, defaulting to OP.");
        }
    }

    public Items getItems() {
        return items;
    }

    public boolean canUseTweakFurnace(Player player, Block furnaceBlock) {
        boolean canUse = true;
        if (zonesPlugin != null) {
            ZoneBase zb = zonesPlugin.getWorldManager(player).getActiveZone(furnaceBlock);
            if (zb != null) {
                canUse = zb.allowBlockCreate(player, furnaceBlock);
            }
        }
        if (canUse == true && permissionsPlugin != null) {
            canUse = permissionsPlugin.permission(player, "tweakfurnace.use");
        }
        return canUse || player.isOp();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("tfmute")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (muteList.contains(p)) {
                    muteList.remove(p);
                    sender.sendMessage(ChatColor.AQUA + "You are now removed from the mute list.");
                } else {
                    muteList.add(p);
                    sender.sendMessage(ChatColor.AQUA + "You are now added to the mute list.");
                }
            } else {
                sender.sendMessage("You have to be a player to use this command!");
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean hasMuteEnabled(Player player) {
        return muteList.contains(player);
    }
}
