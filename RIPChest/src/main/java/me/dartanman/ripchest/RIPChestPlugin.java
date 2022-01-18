package me.dartanman.ripchest;

import org.bukkit.plugin.java.JavaPlugin;

public class RIPChestPlugin extends JavaPlugin
{
	
	public void onEnable()
	{
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

}
