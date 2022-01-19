package me.dartanman.ripchest;

import org.bukkit.plugin.java.JavaPlugin;

import me.dartanman.ripchest.chests.RIPChestManager;
import me.dartanman.ripchest.database.IDatabaseUtils;
import me.dartanman.ripchest.database.MySQLUtils;

public class RIPChestPlugin extends JavaPlugin
{
	
	private IDatabaseUtils databaseUtil;
	
	private RIPChestManager chestManager;
	
	public void onEnable()
	{
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		MySQLUtils.init(this);
		databaseUtil = new MySQLUtils();
		
		chestManager = new RIPChestManager(this);
	}
	
	public RIPChestManager getChestManager()
	{
		return chestManager;
	}
	
	public IDatabaseUtils getDatabase()
	{
		return databaseUtil;
	}

}
