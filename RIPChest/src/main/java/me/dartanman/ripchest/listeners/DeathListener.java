package me.dartanman.ripchest.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.dartanman.ripchest.RIPChestPlugin;
import me.dartanman.ripchest.chests.RIPChest;
import net.md_5.bungee.api.ChatColor;

public class DeathListener implements Listener
{
	
	private RIPChestPlugin plugin;
	
	public DeathListener(RIPChestPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event)
	{
		
		List<ItemStack> drops = new ArrayList();
		
		for(ItemStack item : event.getDrops())
		{
			drops.add(item.clone());
		}
		
		event.getDrops().clear();
		
		Player player = event.getEntity();
		
		UUID playerUUID = player.getUniqueId();
		
		Location deathLocation = player.getLocation();
		
		deathLocation.getBlock().setType(Material.CHEST);
		Location signLocation = deathLocation.clone().add(0, 0, -1);
		signLocation.getBlock().setType(Material.OAK_WALL_SIGN);
		
		Sign sign = (Sign) signLocation.getBlock().getState();
		List<String> signLines = plugin.getConfig().getStringList("Settings.Sign-Lines");
		for(int i = 0; i < 4; i++)
		{
			sign.setLine(i, ChatColor.translateAlternateColorCodes('&', signLines.get(i).replace("<player>", player.getName())));
		}
		sign.update();
		
		
		Chest chest = (Chest) deathLocation.getBlock().getState();
		
		RIPChest ripChest = plugin.getChestManager().createRIPChest(playerUUID, deathLocation);
		plugin.getChestManager().addRIPChestWithDatabase(ripChest);
		
		ArrayList<ItemStack> leftOverItems = new ArrayList<ItemStack>();
		
		for(ItemStack item : drops)
		{
			HashMap<Integer, ItemStack> leftOver = chest.getInventory().addItem(item);
			if(leftOver.keySet() != null)
			{
				if(leftOver.keySet().size() != 0)
				{
					for(Integer key : leftOver.keySet())
					{
						leftOverItems.add(leftOver.get(key));
					}
				}
			}
		}
		
		if(!leftOverItems.isEmpty()) 
		{
			Location nextLocation = deathLocation.clone().add(1, 0, 0);
			Location nextSignLocation = nextLocation.clone().add(0, 0, -1);
			nextSignLocation.getBlock().setType(Material.OAK_WALL_SIGN);
			sign = (Sign) nextSignLocation.getBlock().getState();
			for(int i = 0; i < 4; i++)
			{
				sign.setLine(i, ChatColor.translateAlternateColorCodes('&', signLines.get(i).replace("<player>", player.getName())));
			}
			sign.update();
			nextLocation.getBlock().setType(Material.CHEST);
			Chest nextChest = (Chest) nextLocation.getBlock().getState();
			RIPChest otherRIPChest = plugin.getChestManager().createRIPChest(playerUUID, nextLocation);
			plugin.getChestManager().addRIPChestWithDatabase(otherRIPChest);
			for(ItemStack item : leftOverItems)
			{
				nextChest.getInventory().addItem(item);
			}
		}
	}

}
