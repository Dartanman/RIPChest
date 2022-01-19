package me.dartanman.ripchest.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DeathListener implements Listener
{
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event)
	{
		final List<ItemStack> drops = event.getDrops();
		
		event.getDrops().clear();
		
		Player player = event.getEntity();
		
		UUID playerUUID = player.getUniqueId();
		
		Location deathLocation = player.getLocation();
		
		deathLocation.getBlock().setType(Material.CHEST);
		
		Chest chest = (Chest) deathLocation.getBlock();
		
		for(ItemStack item : drops)
		{
			chest.getInventory().addItem(item);
		}
	}

}
