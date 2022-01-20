package me.dartanman.ripchest.chests;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.dartanman.ripchest.RIPChestPlugin;
import net.md_5.bungee.api.ChatColor;

public class RIPChest
{
	
	private RIPChestPlugin plugin;
	private UUID chestUUID;
	private UUID playerUUID;
	private Location chestLocation;
	private long createTime;
	private long nextMessageTime;
	
	public RIPChest(UUID chestUUID, UUID playerUUID, Location chestLocation, long createTime)
	{
		plugin = (RIPChestPlugin) Bukkit.getPluginManager().getPlugin("RIPChest");
		this.chestUUID = chestUUID;
		this.playerUUID = playerUUID;
		this.chestLocation = chestLocation;
		this.createTime = createTime;
		nextMessageTime = createTime + (plugin.getConfig().getLong("Settings.Player-Message-Interval-Seconds") * 1000L);
		startMessagingPlayer();
	}
	
	public void startMessagingPlayer()
	{
		
		new BukkitRunnable()
		{
			public void run()
			{
				if(getManager().ripChestExists(chestUUID))
				{
					Player player = Bukkit.getPlayer(playerUUID);
					if(player != null)
					{
						long now = System.currentTimeMillis();
						if(now >= nextMessageTime)
						{
							long timeUntilDespawn = (createTime + (plugin.getConfig().getLong("Settings.Death-Chest-Expire-Time-Seconds")*1000L)) - now;
							Integer timeUntilDespawnSeconds = Integer.valueOf(String.valueOf(timeUntilDespawn / 1000));
							
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.Death-Chest-Despawning-Soon")
									.replace("<seconds>", timeUntilDespawnSeconds.toString())
									.replace("<x>", String.valueOf(chestLocation.getBlockX()))
									.replace("<y>", String.valueOf(chestLocation.getBlockY()))
									.replace("<z>", String.valueOf(chestLocation.getBlockZ()))));
						}	
					}
				}
				else
				{
					Player player = Bukkit.getPlayer(playerUUID);
					if(player != null)
					{
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.Death-Chest-Despawned-Or-Stolen")));
					}
					cancel();
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0L, 100L);
	}
	
	public RIPChestManager getManager()
	{
		return plugin.getChestManager();
	}
	
	public long getCreateTime()
	{
		return createTime;
	}
	
	public UUID getUniqueId()
	{
		return chestUUID;
	}
	
	public UUID getPlayerUUID()
	{
		return playerUUID;
	}
	
	public Location getLocation()
	{
		return chestLocation;
	}
	
	public boolean canOpen(Player player)
	{
		if(player.getUniqueId().equals(playerUUID))
		{
			return true;
		}
			ItemStack hand = player.getInventory().getItemInMainHand();
			if(hand == null)
			{
				return false;
			}
			
			ItemMeta meta = hand.getItemMeta();
			if(meta == null)
			{
				return false;
			}
			
			if(!meta.hasDisplayName())
			{
				return false;
			}
			
			String displayName = meta.getDisplayName();
			
			if(displayName.equals("" /*TODO: Make Death Key Item*/))
			{
				return true;
			}
			
			return false;
	}
	
	@Override
	public boolean equals(Object otherObject)
	{
		
		if(otherObject == null)
		{
			return false;
		}
		
		if(!(otherObject instanceof RIPChest))
		{
			return false;
		}
		
		RIPChest otherChest = (RIPChest) otherObject;
		if(otherChest.getUniqueId().equals(this.getUniqueId()) && otherChest.getPlayerUUID().equals(this.getPlayerUUID()) &&
				otherChest.getLocation().equals(this.getLocation()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
