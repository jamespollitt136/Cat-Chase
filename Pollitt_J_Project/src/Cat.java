import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class Cat extends GameObject
{
	private int position = 13;
	private AudioClip sound;
	
	public Cat(int x, int y)
	{
		super(x,y);
		this.image = new Image(Game.class.getResource("res/cat.png").toExternalForm());
		sound = new AudioClip(Game.class.getResource("res/cat.wav").toExternalForm());
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
}