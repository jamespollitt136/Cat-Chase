import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class Database 
{
	private Connection connection = null;
	private int columnNumber;
	private String initials;
	private int score;
	
	public Database()
	{
		String DRIVER_CLASS = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://helios.csesalford.com/sta302_sa";
		String username = "sta302";
		String password = ""; // password removed
		columnNumber = 1;
		
		try
		{
			Class.forName(DRIVER_CLASS);
			connection = DriverManager.getConnection(url, username, password); //connect to DB
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
			System.exit(1); //close 
		}
		catch(Exception exception)
		{
			System.err.println("Cannot load " + DRIVER_CLASS);
			Alert driverDialog = new Alert(AlertType.ERROR);
			driverDialog.setTitle("ERROR: Missing Jar File");
			driverDialog.setHeaderText("mysql-connector-java-5.1.38-bin.jar");
			driverDialog.setContentText("Please add the above file to the build path."
										+ System.lineSeparator() + "\n" 
										+ "You will find the file in the folder: pollittj");
			driverDialog.showAndWait();
			System.exit(1); //close
		}
	}
	
	public void addToDatabase(String name, String initials, int age, int score) throws SQLException
	{
		String sqlQuery = "INSERT INTO Scoreboard Values(?, ?, ?, ?)";
		PreparedStatement ps = connection.prepareStatement(sqlQuery);
		ps.setString(1, name);
		ps.setString(2, initials);
		ps.setInt(3, age);
		ps.setInt(4, score);
		ps.executeUpdate();
	}
	
	public void showScoreBoard() throws SQLException
	{
		String sqlQuery = "SELECT * FROM Scoreboard ORDER BY Score DESC LIMIT 5";
		PreparedStatement ps = connection.prepareStatement(sqlQuery);
		ResultSet results = ps.executeQuery();
		while(results.next())
		{
			initials = results.getString("Initials");
			score = results.getInt("Score");
			System.out.println("Initials: " + initials + ". ");
			System.out.println("Score: " + score + ". ");
			System.out.println();
			columnNumber++;
		}
	}
	
	public String getInitials()
	{
		return initials;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public int getColumns()
	{
		return columnNumber;
	}
}
