package me.dartanman.ripchest.database;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;

import me.dartanman.ripchest.chests.RIPChest;

public interface IDatabaseUtils {
	
	public void addDeathChest(UUID chestUUID, UUID playerUUID, Location location);
	
	public ArrayList<RIPChest> getChestsFromDatabase();
	
	public void deleteDeathChest(UUID chestUUID);

}
