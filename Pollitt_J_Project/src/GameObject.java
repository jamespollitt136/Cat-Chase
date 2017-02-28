import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class GameObject 
{
	public Image image;
	public Rectangle rectangle;
	
	public GameObject(int x, int y)
	{
		image = new Image(Game.class.getResource("res/square.png").toExternalForm());
		rectangle = new Rectangle(x, y, 80, 80);
	}
}