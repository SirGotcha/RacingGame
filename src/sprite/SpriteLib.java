package sprite;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class SpriteLib {

	private static SpriteLib single;
	private static HashMap<URL, BufferedImage>sprites;
	
	public static SpriteLib getInstance() {
		if(single==null) {
			single = new SpriteLib();
		}
		return single;
	}
	
	private SpriteLib() {
		sprites = new HashMap<URL, BufferedImage>();
	}
	/*
	 * Get Sprite Methoden sind verschiedene Methoden zum Laden des Bildes als Sprite
	 */
	//URl der Dateien holen
	public URL getURLfromRessource(String path){
		return getClass().getClassLoader().getResource(path);
	}
	//Bilder etc laden
	public BufferedImage[] getSprite(String path, int column, int row) {
		//Pfad der Bilddatei Holen
		URL location = getURLfromRessource(path);
		
		BufferedImage source = null;
		
		source = (BufferedImage) sprites.get(location);
		
		if (source == null) {
			try {
				source = ImageIO.read(location);
			} catch (IOException e1) {
				System.out.println(e1);
				return null;
			}
			sprites.put(location, source);
		}
	
		int width = source.getWidth() / column;
		int height = source.getHeight() /row;
		
		BufferedImage[] pics = new BufferedImage[column + row];
		int count = 0;
		
		for (int n = 0; n < row; n++) {
			for (int i = 0; i < column; i++) {
				pics[count] = source.getSubimage(i * width, n * height, width, height);
				count++;
			}
		}
		return pics;
	}
	
}
	
	
	
	
	
	
	
	