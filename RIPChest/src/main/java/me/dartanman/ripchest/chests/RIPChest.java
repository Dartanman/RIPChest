package me.dartanman.ripchest.chests;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.dartanman.ripchest.RIPChestPlugin;
import me.dartanman.ripchest.listeners.utils.DeathKeyUtils;
import net.md_5.bungee.api.ChatColor;

public class RIPChest
{
	
	private RIPChestPlugin plugin;
	private UUID chestUUID;
	private UUID playerUUID;
	private Location chestLocation;
	private long expireTime;
	private int secondsBetweenMessages;
	private long createTime;
	private boolean broadcasted;
	
	public RIPChest(UUID chestUUID, UUID playerUUID, Location chestLocation, long createTime)
	{
		plugin = (RIPChestPlugin) Bukkit.getPluginManager().getPlugin("RIPChest");
		this.chestUUID = chestUUID;
		this.playerUUID = playerUUID;
		this.chestLocation = chestLocation;
		this.createTime = createTime;
		expireTime = createTime + plugin.getConfig().getLong("Settings.Death-Chest-Expire-Time-Seconds") * 1000L;
		secondsBetweenMessages = plugin.getConfig().getInt("Settings.Player-Message-Interval-Seconds");
		broadcasted = false;
		startMessagingPlayer();
	}
	
	public void startMessagingPlayer()
	{
		
		new BukkitRunnable()
		{
			public void run()
			{
				long now = System.currentTimeMillis();
				long millisUntilExpire = expireTime - now;
				int secondsUntilExpire = (int)(millisUntilExpire/1000);
				
				if(!getManager().ripChestExists(chestUUID))
				{
					Player player = Bukkit.getPlayer(playerUUID);
					if(player != null)
					{
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.Death-Chest-Despawned-Or-Stolen")));
					}
					cancel();
					return;
				}
				
				if(secondsUntilExpire % secondsBetweenMessages == 0)
				{
					if(player != null)
					{
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.Death-Chest-Despawning-Soon")
								.replace("<seconds>", String.valueOf(secondsUntilExpire))
								.replace("<x>", String.valueOf(chestLocation.getBlockX()))
								.replace("<y>", String.valueOf(chestLocation.getBlockY()))
								.replace("<z>", String.valueOf(chestLocation.getBlockZ()))));
					}
				}
				
				if(!broadcasted)
				{
					
					long broadcastTime = createTime + (plugin.getConfig().getLong("Settings.Seconds-Until-Broadcast") * 1000L);
					
					if(now >= broadcastTime)
					{
						broadcasted = true;
						// Using a for loop instead of Bukkit.broadcastMessage because Bukkit.broadcastMessage goes to console too
						for(Player online : Bukkit.getOnlinePlayers())
						{
							online.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.Death-Chest-Location-Broadcast")
									.replace("<seconds>", String.valueOf(secondsUntilExpire))
									.replace("<x>", String.valueOf(chestLocation.getBlockX()))
									.replace("<y>", String.valueOf(chestLocation.getBlockY()))
									.replace("<z>", String.valueOf(chestLocation.getBlockZ()))));
						}
					}
				}
				
				
				
			}
		}.runTaskTimerAsynchronously(plugin, 0L, 20L);
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
			
			/*
			 * ItemMeta meta = hand.getItemMeta(); if(meta == null) { return false; }
			 * 
			 * if(!meta.hasDisplayName()) { return false; }
			 */
			
			if(hand.isSimilar(DeathKeyUtils.createDeathKey()))
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
