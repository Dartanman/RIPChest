package me.dartanman.ripchest.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.dartanman.ripchest.RIPChestPlugin;

public class MySQLUtils implements IDatabaseUtils{
	
	private static String host;
	private static String port;
	private static String schema;
	private static String username;
	private static String password;
	
	private static void init(RIPChestPlugin plugin)
	{
		host = plugin.getConfig().getString("MySQL.Host");
		port = plugin.getConfig().getString("MySQL.Port");
		schema = plugin.getConfig().getString("MySQL.Database");
		username = plugin.getConfig().getString("MySQL.Username");
		password = plugin.getConfig().getString("MySQL.Password");
	}
	
	private static Connection connectToDatabase()
	{
		return connectToDatabase(host, port, schema, username, password);
	}
	
	private static Connection connectToDatabase(String host, String port, String schema, String username, String password) 
	{
		final String DATABASE_URL = "jdbc:mysql://" + host + ":" + port + "/" + schema + "?characterEncoding=utf8";
		try
		{
			Connection connection = DriverManager.getConnection(DATABASE_URL, username, password);
			return connection;
		}
		catch (SQLException e)
		{
			Bukkit.getLogger().severe("Failed to connect to MySQL Database!");
			Bukkit.getLogger().severe(e.toString());
			return null;
		}
	}
	
	private static void createChestTable()
	{
		Connection connection = connectToDatabase();
		try
		{
			PreparedStatement createTableStatement = connection.prepareStatement(MySQLConstants.CREATE_CHEST_TABLE);
			createTableStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			Bukkit.getLogger().severe("Failed to create (or locate) RIP_CHEST database table!");
			Bukkit.getLogger().severe(e.toString());
		}
		finally
		{
			disconnect(connection);
		}
	}
	
	@Override
	public void addDeathChest(UUID chestUUID, UUID playerUUID, Location location) {
		Connection connection = connectToDatabase();
		try
		{
			PreparedStatement addDeathChestStatement = connection.prepareStatement(MySQLConstants.INSERT_NEW_CHEST);
			addDeathChestStatement.setString(1, chestUUID.toString());
			addDeathChestStatement.setString(2, playerUUID.toString());
			addDeathChestStatement.setLong(3, System.currentTimeMillis());
			addDeathChestStatement.setInt(4, location.getBlockX());
			addDeathChestStatement.setInt(5, location.getBlockY());
			addDeathChestStatement.setInt(6, location.getBlockZ());
			addDeathChestStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			Bukkit.getLogger().severe("Failed to insert a death chest into the database!");
			Bukkit.getLogger().severe(e.toString());
		}
		disconnect(connection);
	}
	
	private static void disconnect(Connection connection)
	{
		try
		{
			connection.close();
		} 
		catch (SQLException e)
		{
			Bukkit.getLogger().severe("Failed to disconnect from database! (this likely has been preceded by another error)");
			Bukkit.getLogger().severe(e.toString());
		}
	}

}
