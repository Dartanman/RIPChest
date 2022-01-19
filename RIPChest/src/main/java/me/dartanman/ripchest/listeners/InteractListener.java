package me.dartanman.ripchest.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.dartanman.ripchest.RIPChestPlugin;
import me.dartanman.ripchest.chests.RIPChest;
import me.dartanman.ripchest.chests.RIPChestManager;
import net.md_5.bungee.api.ChatColor;

public class InteractListener implements Listener
{
	
	private RIPChestPlugin plugin;
	
	public InteractListener(RIPChestPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	private void debug(String message)
	{
		Bukkit.broadcastMessage(ChatColor.AQUA + "[Debug] " + ChatColor.YELLOW + message);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		
		debug("Interact");
		
		if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			return;
		}
		
		debug("Right click block");
		
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		RIPChestManager chestManager = plugin.getChestManager();
		
		if(!chestManager.isBlockRIPChest(block))
		{
			return;
		}
		
		debug ("is rip chest");
		
		RIPChest chest = chestManager.getChestFromBlock(block);
		
		if(!chest.canOpen(player))
		{
			event.setCancelled(true);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.Need-Key-To-Open")));
			return;
		}
		
		debug("Can open");
		
	}

}
