package me.dartanman.ripchest;

import org.bukkit.plugin.java.JavaPlugin;

import me.dartanman.ripchest.chests.RIPChestManager;
import me.dartanman.ripchest.database.IDatabaseUtils;
import me.dartanman.ripchest.database.MySQLUtils;
import me.dartanman.ripchest.listeners.CloseInventoryListener;
import me.dartanman.ripchest.listeners.DeathListener;
import me.dartanman.ripchest.listeners.InteractListener;

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
		
		getServer().getPluginManager().registerEvents(new DeathListener(this), this);
		getServer().getPluginManager().registerEvents(new InteractListener(this), this);
		getServer().getPluginManager().registerEvents(new CloseInventoryListener(this), this);
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
