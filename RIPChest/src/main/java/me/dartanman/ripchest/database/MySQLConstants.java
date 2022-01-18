package me.dartanman.ripchest.database;

public class MySQLConstants 
{
	
	public static String CREATE_CHEST_TABLE = "CREATE TABLE IF NOT EXISTS RIP_CHEST ("
			+ "CHEST_UUID VARCHAR(37) NOT NULL,"
			+ "PLAYER_UUID VARCHAR(37) NOT NULL,"
			+ "CREATE_TIME BIGINT NOT NULL"
			+ "BLOCK_X INT NOT NULL,"
			+ "BLOCK_Y INT NOT NULL,"
			+ "BLOCK_Z INT NOT NULL,"
			+ "PRIMARY KEY ( CHEST_UUID )"
			+ ");";
	
	public static String INSERT_NEW_CHEST = "INSERT INTO RIP_CHEST (PLAYER_UUID, CREATE_TIME, BLOCK_X, BLOCK_Y, BLOCK_Z) VALUES " +
	"(?, ?, ?, ?, ?, ?);";

}
