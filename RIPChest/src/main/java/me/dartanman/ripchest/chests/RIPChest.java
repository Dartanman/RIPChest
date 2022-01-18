package me.dartanman.ripchest.chests;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RIPChest
{
	
	private UUID chestUUID;
	private UUID playerUUID;
	private Location chestLocation;
	
	public RIPChest(UUID chestUUID, UUID playerUUID, Location chestLocation)
	{
		this.playerUUID = playerUUID;
		this.chestLocation = chestLocation;
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

}
