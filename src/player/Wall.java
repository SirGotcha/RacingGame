package player;

import game.GamePanel;
import java.awt.image.BufferedImage;



public class Wall extends sprite.Sprite {

	private static final long serialVersionUID = 1L;

	public Wall(BufferedImage[] i, double x, double y, long delay, GamePanel gamePanel) {
		super(i, x, y, delay, gamePanel);
		parent = (GamePanel) gamePanel;
		// TODO Auto-generated constructor stub
	}

	//Methoden zur Kollisionsabfrage
	@Override
	public boolean collidedWith(sprite.Sprite s) {

    if(remove){
    	return false;
    }
		
		if(this.intersects(s)){
			
			if(s instanceof Car){
				System.out.println("Treffer");
				remove   = true;
				s.remove = true;
				return true;
			}			
			
		}
		
		
		return false;
	}

	public void doLogic(long delta) {
		super.doLogic(delta);
	}
}
