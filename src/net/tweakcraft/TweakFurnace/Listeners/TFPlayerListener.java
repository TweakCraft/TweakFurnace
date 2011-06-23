package net.tweakcraft.TweakFurnace.Listeners;

import net.tweakcraft.TweakFurnace.Packages.Items;
import net.tweakcraft.TweakFurnace.Packages.TFurnace;
import net.tweakcraft.TweakFurnace.TweakFurnace;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

/**
 * @author GuntherDW, Edoxile
 */
public class TFPlayerListener extends PlayerListener {

    private static final Logger log = Logger.getLogger("Minecraft");

    private TweakFurnace plugin;

    public TFPlayerListener(TweakFurnace instance) {
        this.plugin = instance;
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled() || event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;
        Block block = event.getClickedBlock();
        if (TFurnace.isFurnace(block)) {
            if(!plugin.canUseTweakFurnace(event.getPlayer(), event.getClickedBlock()))
                return;
            TFurnace furnace = new TFurnace((Furnace) block.getState());
            ItemStack hand = event.getItem();
            if (hand != null && hand.getAmount() > 0 && hand.getTypeId() != Material.AIR.getId()) {
                ItemStack leftover;
                int total, diff;
                if (Items.isFuel(hand.getTypeId())) {
                    leftover = furnace.putFuel(hand.clone());
                    total = furnace.getFuel().getAmount();
                } else if (Items.isSmeltable(hand.getTypeId())) {
                    leftover = furnace.putSmelt(hand.clone());
                    total = furnace.getSmelt().getAmount();
                } else {
                    return;
                }

                if (leftover == null) {
                    diff = hand.getAmount();
                } else {
                    diff = hand.getAmount() - leftover.getAmount();
                }

                event.getPlayer().setItemInHand(leftover);

                if (!plugin.hasMuteEnabled(event.getPlayer())) {
                    if (diff == 0) {
                        event.getPlayer().sendMessage(ChatColor.GOLD + "Couldn't put fuel/smelt in furnace. Probably some other fuel/smelt in there or it's already full!");
                    } else {
                        event.getPlayer().sendMessage(ChatColor.GOLD + "You put " + diff + " " + hand.getType().name().toLowerCase() + " in a furnace!");
                        event.getPlayer().sendMessage(ChatColor.GOLD + "This furnace is now containing " + total + " " + hand.getType().name().toLowerCase() + ".");
                    }
                }
            }
        }
    }

}
