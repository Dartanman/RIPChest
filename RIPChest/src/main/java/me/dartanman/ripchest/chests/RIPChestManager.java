package me.dartanman.ripchest.chests;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import me.dartanman.ripchest.RIPChestPlugin;

public class RIPChestManager {
	
	private RIPChestPlugin plugin;
	
	private ArrayList<RIPChest> chestList;
	
	public RIPChestManager(RIPChestPlugin plugin)
	{
		chestList = new ArrayList<RIPChest>();
		this.plugin = plugin;
		loadFromDatabase();
		startCheckingForExpiredChests();
	}
	
	public void startCheckingForExpiredChests()
	{
		new BukkitRunnable()
		{
			public void run()
			{
				if(chestList.isEmpty())
				{
					return;
				}
				for(RIPChest chest : chestList)
				{
					long now = System.currentTimeMillis();
					if(now - chest.getCreateTime() >= plugin.getConfig().getLong("Settings.Death-Chest-Expire-Time-Seconds")*1000L)
					{
						new BukkitRunnable()
						{
							public void run()
							{
								Location location = chest.getLocation();
								location.getBlock().setType(Material.AIR);
								removeRIPChest(chest);
							}
						}.runTask(plugin);
						plugin.getDatabase().deleteDeathChest(chest.getUniqueId());
					}
				}
			}
		}.runTaskTimerAsynchronously(plugin, 100L, 100L);
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
	
	public boolean ripChestExists(UUID chestUUID)
	{
		for(RIPChest chest : chestList)
		{
			if(chest.getUniqueId().equals(chestUUID))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isBlockRIPChest(Block block)
	{
		if(block.getType().equals(Material.CHEST))
		{
			for(RIPChest chest : chestList)
			{
				if(block.getLocation().getBlockX() == chest.getLocation().getBlockX() &&
						block.getLocation().getBlockY() == chest.getLocation().getBlockY() &&
						block.getLocation().getBlockZ() == chest.getLocation().getBlockZ())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public RIPChest getChestFromBlock(Block block)
	{
		if(isBlockRIPChest(block))
		{
			for(RIPChest chest : chestList)
			{
				if(block.getLocation().getBlockX() == chest.getLocation().getBlockX() &&
						block.getLocation().getBlockY() == chest.getLocation().getBlockY() &&
						block.getLocation().getBlockZ() == chest.getLocation().getBlockZ())
				{
					return chest;
				}
			}
			return null;
		}
		else
		{
			return null;
		}
	}
	
	public boolean addRIPChestWithDatabase(RIPChest chest)
	{
		new BukkitRunnable()
		{
			public void run()
			{
				plugin.getDatabase().addDeathChest(chest.getUniqueId(), chest.getPlayerUUID(), chest.getLocation());
			}
		}.runTaskAsynchronously(plugin);
		
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
				new BukkitRunnable()
				{
					public void run()
					{
						plugin.getDatabase().deleteDeathChest(chest.getUniqueId());
					}
				}.runTaskAsynchronously(plugin);
			}
			else
			{
				addRIPChest(chest);	
			}
		}
	}

}
