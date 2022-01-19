package me.dartanman.ripchest.chests;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;

import me.dartanman.ripchest.RIPChestPlugin;

public class RIPChestManager {
	
	private RIPChestPlugin plugin;
	
	private ArrayList<RIPChest> chestList;
	
	public RIPChestManager(RIPChestPlugin plugin)
	{
		chestList = new ArrayList<RIPChest>();
		this.plugin = plugin;
		loadFromDatabase();
	}
	
	public RIPChest createRIPChest(UUID chestUUID, UUID playerUUID, Location location, long createTime)
	{
		return new RIPChest(chestUUID, playerUUID, location, createTime);
	}
	
	public RIPChest createRIPChest(UUID playerUUID, Location location)
	{
		return createRIPChest(UUID.randomUUID(), playerUUID, location, System.currentTimeMillis());
	}
	
	public boolean addRIPChest(RIPChest chest)
	{
		return chestList.add(chest);
	}
	
	public boolean removeRIPChest(RIPChest chest)
	{
		return chestList.remove(chest);
	}
	
	private void loadFromDatabase()
	{
		for(RIPChest chest : plugin.getDatabase().getChestsFromDatabase())
		{
			long now = System.currentTimeMillis();
			if(now - chest.getCreateTime() >= plugin.getConfig().getLong("Settings.Death-Chest-Expire-Time-Seconds")*1000L)
			{
				Location location = chest.getLocation();
				location.getBlock().setType(Material.AIR);
				plugin.getDatabase().deleteDeathChest(chest.getUniqueId());
			}
			else
			{
				addRIPChest(chest);	
			}
		}
	}

}
