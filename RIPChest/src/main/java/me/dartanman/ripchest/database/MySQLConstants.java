package me.dartanman.ripchest.database;

public class MySQLConstants 
{
	
	public static final String CREATE_CHEST_TABLE = "CREATE TABLE IF NOT EXISTS RIP_CHEST ("
			+ "CHEST_UUID VARCHAR(37) NOT NULL,"
			+ "PLAYER_UUID VARCHAR(37) NOT NULL,"
			+ "CREATE_TIME BIGINT NOT NULL,"
			+ "WORLD BIGINT NOT NULL,"
			+ "BLOCK_X INT NOT NULL,"
			+ "BLOCK_Y INT NOT NULL,"
			+ "BLOCK_Z INT NOT NULL,"
			+ "PRIMARY KEY ( CHEST_UUID )"
			+ ");";
	
	public static final String INSERT_NEW_CHEST = "INSERT INTO RIP_CHEST (PLAYER_UUID, CREATE_TIME, BLOCK_X, BLOCK_Y, BLOCK_Z) VALUES " +
	"(?, ?, ?, ?, ?, ?);";
	
	public static final String GET_CHESTS_FROM_DATABASE = "SELECT * FROM RIP_CHEST";
	
	public static final String DELETE_BY_CHEST_UUID = "DELETE FROM RIP_CHEST WHERE CHEST_UUID = ?";

}
