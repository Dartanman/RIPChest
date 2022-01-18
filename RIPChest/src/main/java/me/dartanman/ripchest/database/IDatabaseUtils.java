package me.dartanman.ripchest.database;

import java.util.UUID;

import org.bukkit.Location;

public interface IDatabaseUtils {
	
	public void addDeathChest(UUID chestUUID, UUID playerUUID, Location location);

}
