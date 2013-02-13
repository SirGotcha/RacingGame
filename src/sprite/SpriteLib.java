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
	
	public BufferedImage getSprite(URL location) {
		
		BufferedImage pic = null;
		
		pic = (BufferedImage) sprites.get(location);
		
		if (pic != null) {
			return pic;
		}
		
		try {
			pic = ImageIO.read(location);
		} catch (IOException e1) {
			System.out.println("Fehler beim Image laden: " + e1);
			return null;
		}
		
		sprites.put(location, pic);
		
		return pic;
	}

	public BufferedImage getSprite(String path) {
		
		BufferedImage pic = null;
		URL location = getURLfromRessource(path);
		
		pic = (BufferedImage) sprites.get(location);
		
		if (pic != null) {
			return pic;
		}
		
		try {
			pic = ImageIO.read(location);
		} catch (IOException e1) {
			System.out.println("Fehler beim Image laden: " + e1);
			return null;
		}
		
		sprites.put(location, pic);
		
		return pic;
	}
	
	public BufferedImage[] getSprite(String path, int column, int row) {
		
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
	
	public BufferedImage[] getSprite(URL location, int column, int row) {
		
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
	
	public URL getURLfromRessource(String path){
		return getClass().getClassLoader().getResource(path);
	}
	
}
	
	
	
	
	
	
	
	