package player;


import game.GamePanel;
import java.awt.image.BufferedImage;


public class Car extends sprite.Sprite {
	
	private static final long serialVersionUID = 1L;
	GamePanel parent;
	
	public Car (BufferedImage[] i, double x, double y, long delay, GamePanel gamePanel) {
		super (i, x, y, delay, gamePanel);
		parent = (GamePanel) gamePanel;
	}
	
	public void doLogic(long delta) {
		super.doLogic(delta);
	}

	//Methoden zur Kollisionsabfrage
	@Override
	public boolean collidedWith(sprite.Sprite s) {

    if(remove){
    	return false;
    }
		
		if(this.intersects(s)){
			
			if(s instanceof Wall){
				remove   = true;
				s.remove = true;
				return true;
			}			
			
		}
		
		
		return false;
	}
}
