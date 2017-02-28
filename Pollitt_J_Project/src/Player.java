import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class Player extends GameObject
{
	private int position = 0;
	private String playerName, playerInitials;
	private int playerAge, playerScore;
	private AudioClip sound;
	
	public Player(int x, int y)
	{
		super(x, y);
		image = new Image(Game.class.getResource("res/dog.png").toExternalForm());
		sound = new AudioClip(Game.class.getResource("res/dog.wav").toExternalForm());
	}
	
	/**
	 * Move the player forward after each dice throw and correct answer. 
	 * @param diceThrow
	 */
	public void move(int diceThrow)
	{
		position += diceThrow;
		if(position>=49)
		{
			position = 0;
		}
		rectangle.setX(position%7 * 80);
		rectangle.setY(position/7 * 80);
		sound.play();
	}
	
	/**
	 * Called when a player lands on a Tennis ball in the game, setting player back.
	 * @param position
	 */
	public void moveTo(int position)
	{
		this.position = position;
		rectangle.setX(position%7 * 80);
		rectangle.setY(position/7 * 80);
	}
	
	public int getPosition()
	{
		return position;
	}
	
	public void setPosition(int position)
	{
		this.position = position;
	}
	
	public void setDetails(String name, String initials, int age, int score)
	{
		this.playerName = name;
		playerInitials = initials;
		playerAge = age;
		playerScore = score;
	}

	public String getPlayerName() 
	{
		return playerName;
	}
	
	public String getPlayerInitials() 
	{
		return playerInitials;
	}
	
	public int getPlayerAge()
	{
		return playerAge;
	}
	
	public int getPlayerScore()
	{
		return playerScore;
	}
}