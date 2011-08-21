package net.tweakcraft.TweakFurnace.Listeners;

import net.tweakcraft.TweakFurnace.Packages.Items;
import net.tweakcraft.TweakFurnace.Packages.TFInventoryUtils;
import net.tweakcraft.TweakFurnace.Packages.TFurnace;
import net.tweakcraft.TweakFurnace.TweakFurnace;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
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
            if (!plugin.canUseTweakFurnace(event.getPlayer(), event.getClickedBlock()))
                return;
            TFurnace furnace = new TFurnace((Furnace) block.getState());
            ItemStack hand = event.getItem();
            if (hand != null && hand.getAmount() > 0 && hand.getTypeId() != Material.AIR.getId()) {
                ItemStack leftover;
                int total, diff;
                int holdback = TFInventoryUtils.getCustomPlayerAmount(event.getPlayer());
                int heldback = 0;
                if(holdback!=64) {
                    if(hand.getAmount()>holdback && hand.getAmount() - holdback>0) {
                        heldback = hand.getAmount()-holdback;
                        hand.setAmount(holdback);
                    }

                }
                if (Items.isFuel(hand.getTypeId())) {
                    ItemStack fuel = furnace.getFuel();
                    if (fuel != null && fuel.getTypeId() != Material.AIR.getId() && fuel.getAmount() > 0 && (fuel.getTypeId() != hand.getTypeId()
                            || fuel.getDurability() != hand.getDurability())) {
                        //Eerst maar eens kijken wie dit allemaal "gebruikt"
                        //event.getPlayer().sendMessage(ChatColor.DARK_RED + "This furnace already contains something else!");
                        log.info("[TweakFurnace] " + event.getPlayer().getName() + " tried to put " + hand.getType().name() + " in a furnace with " + fuel.getType().name());
                        return;
                    }
                    leftover = furnace.putFuel(hand.clone());
                    total = furnace.getFuel().getAmount();
                } else if (Items.isSmeltable(hand.getTypeId())) {
                    ItemStack smelt = furnace.getSmelt();
                    if (smelt != null && smelt.getTypeId() != Material.AIR.getId() && smelt.getAmount() > 0 && (smelt.getTypeId() != hand.getTypeId()
                            || smelt.getDurability() != hand.getDurability())) {
                        //Eerst maar eens kijken wie dit allemaal "gebruikt"
                        //event.getPlayer().sendMessage(ChatColor.DARK_RED + "This furnace already contains something else!");
                        log.info("[TweakFurnace] " + event.getPlayer().getName() + " tried to put " + hand.getType().name() + " in a furnace with " + smelt.getType().name());
                        return;
                    }
                    if(smelt!=null&&smelt.getTypeId()!=Material.AIR.getId()) {
                        int infurnace = smelt.getAmount();
                        if(infurnace+hand.getAmount()>holdback) {
                            
                            // hand.setAmount(); /* Checks op hoeveel er al in de furnace zit */

                            /* Iets als int insteken = infurnace-holdback; if(insteken < 0) insteken = ~insteken+1;
                                en daarmee/of met iets dergerlijks werken ipv een while ofzo? Just rambling out loud here.
                             */
                        }

                        
                    }
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

                if(heldback>0) {
                    if(leftover == null) leftover = new ItemStack(hand.getTypeId(), heldback, hand.getData().getData());
                    else leftover.setAmount(leftover.getAmount()+heldback);
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

    public void onPlayerQuit(PlayerQuitEvent event){
        TFInventoryUtils.removeCustomPlayerAmount(event.getPlayer());
    }
}
