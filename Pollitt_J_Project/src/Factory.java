
public class Factory implements FactoryInterface 
{
	public GameObject createProduct(String discrim, int x, int y, int moveToPosition) 
	{
		if(discrim.equals("tennis")) 
		{
			return(new Tennisball(x, y ,moveToPosition));		
		} 
		else if(discrim.equals("cat")) 
		{
			return(new Cat(x, y));
		} 
		else if(discrim.equals("player")) 
		{
			return(new Player(x, y));
		}
		else 
		{
			return(null);
		}
	}
}
