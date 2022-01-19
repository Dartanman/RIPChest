package me.dartanman.ripchest.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import me.dartanman.ripchest.RIPChestPlugin;
import me.dartanman.ripchest.chests.RIPChest;

public class MySQLUtils implements IDatabaseUtils{
	
	private static RIPChestPlugin plugin;
	
	private static String host;
	private static String port;
	private static String schema;
	private static String username;
	private static String password;
	
	public static void init(RIPChestPlugin plugin)
	{
		MySQLUtils.plugin = plugin;
		host = plugin.getConfig().getString("MySQL.Host");
		port = plugin.getConfig().getString("MySQL.Port");
		schema = plugin.getConfig().getString("MySQL.Database");
		username = plugin.getConfig().getString("MySQL.Username");
		password = plugin.getConfig().getString("MySQL.Password");
		createChestTable();
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
			createTableStatement.close();
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
			addDeathChestStatement.setString(4, location.getWorld().getName());
			addDeathChestStatement.setInt(5, location.getBlockX());
			addDeathChestStatement.setInt(6, location.getBlockY());
			addDeathChestStatement.setInt(7, location.getBlockZ());
			addDeathChestStatement.executeUpdate();
			addDeathChestStatement.close();
		}
		catch (SQLException e)
		{
			Bukkit.getLogger().severe("Failed to insert a death chest into the database!");
			Bukkit.getLogger().severe(e.toString());
		}
		disconnect(connection);
	}
	
	@Override
	public ArrayList<RIPChest> getChestsFromDatabase()
	{
		Connection connection = connectToDatabase();
		
		ArrayList<RIPChest> chestList = new ArrayList<RIPChest>();
		
		try
		{
			Bukkit.getLogger().info("Getting all current death chests from database (this may take some time)...");
			PreparedStatement getChestsStatement = connection.prepareStatement(MySQLConstants.GET_CHESTS_FROM_DATABASE);
			ResultSet getChestsResultSet = getChestsStatement.executeQuery();
			while(getChestsResultSet.next())
			{
				String chestUUIDStr = getChestsResultSet.getString("CHEST_UUID");
				UUID chestUUID = UUID.fromString(chestUUIDStr);
				String playerUUIDStr = getChestsResultSet.getString("PLAYER_UUID");
				UUID playerUUID = UUID.fromString(playerUUIDStr);
				long createTime = getChestsResultSet.getLong("CREATE_TIME");
				String worldName = getChestsResultSet.getString("WORLD");
				World world = Bukkit.getWorld(worldName);
				int blockX = getChestsResultSet.getInt("BLOCK_X");
				int blockY = getChestsResultSet.getInt("BLOCK_Y");
				int blockZ = getChestsResultSet.getInt("BLOCK_Z");
				RIPChest chest = new RIPChest(chestUUID, playerUUID, new Location(world, blockX, blockY, blockZ), createTime);
				chestList.add(chest);
			}
			Bukkit.getLogger().info("All death chests have successfully been gotten from the database!");
			getChestsResultSet.close();
			getChestsStatement.close();
		}
		catch (SQLException e)
		{
			Bukkit.getLogger().severe("Failed to get death chests from the database!");
			Bukkit.getLogger().severe(e.toString());
		}
		finally
		{
			disconnect(connection);
		}
		return chestList;
	}
	
	@Override
	public void deleteDeathChest(UUID chestUUID)
	{
		Connection connection = connectToDatabase();

		try
		{
			PreparedStatement deleteChestStatement = connection.prepareStatement(MySQLConstants.DELETE_BY_CHEST_UUID);
			deleteChestStatement.setString(1, chestUUID.toString());
			deleteChestStatement.executeUpdate();
			deleteChestStatement.close();
		}
		catch (SQLException e)
		{
			Bukkit.getLogger().severe("Failed to delete a death chest from the database!");
			Bukkit.getLogger().severe(e.toString());
		}
		finally
		{
			disconnect(connection);
		}
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
