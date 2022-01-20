package me.dartanman.ripchest.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.dartanman.ripchest.RIPChestPlugin;
import me.dartanman.ripchest.chests.RIPChest;
import net.md_5.bungee.api.ChatColor;

public class CloseInventoryListener implements Listener{
	
	private RIPChestPlugin plugin;
	
	public CloseInventoryListener(RIPChestPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	private void debug(String message)
	{
		Bukkit.broadcastMessage(ChatColor.AQUA + "[Debug] " + ChatColor.YELLOW + message);
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event)
	{
		debug("close");
		// I know this seems redundant, but InventoryCloseEvent.getPlayer() actually returns
		// a HumanEntity, not a Player
		if((event.getPlayer() instanceof Player))
		{
			Player player = (Player) event.getPlayer();
			Inventory inventory = event.getInventory();
			debug("type: " + inventory.getType().toString());
			if(inventory.getType().equals(InventoryType.CHEST))
			{
				debug("chest close");
				InventoryHolder holder = inventory.getHolder();
				if(holder instanceof Chest)
				{
					debug("holder is chest");
					Chest chest = (Chest) holder;
					Block block = chest.getBlock();
					if(plugin.getChestManager().isBlockRIPChest(block))
					{
						debug("chest is rip chest");
						RIPChest ripChest = plugin.getChestManager().getChestFromBlock(block);
						for(ItemStack item : inventory.getContents())
						{
							if(item == null) 
							{
								continue;
							}
							if(item.getType().equals(Material.AIR)) 
							{
								continue; 
							}
							debug("dropping item");
							block.getWorld().dropItemNaturally(block.getLocation().add(0, 0.2, 0), item);
						}
						Location location = block.getLocation();
						location.getBlock().setType(Material.AIR);
						plugin.getChestManager().removeRIPChest(ripChest);
						plugin.getDatabase().deleteDeathChest(ripChest.getUniqueId());
					}
				}
			}
		}
	}

}
