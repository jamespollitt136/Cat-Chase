
import javafx.scene.image.Image;

public class Tennisball extends GameObject
{
	int moveToPosition;
	
	public Tennisball(int x, int y, int moveToPosition) 
	{
		super(x, y);
		image = new Image(Game.class.getResource("res/tennisball.png").toExternalForm());
		this.moveToPosition = moveToPosition;
	}
}