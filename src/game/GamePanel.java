package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import player.Car;
import player.Wall;
import sprite.SpriteLib;


public class GamePanel extends JPanel implements Runnable, KeyListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	boolean game_running = true;
	boolean started = false;
	boolean once = false;
	
	long delta = 0;
	long last = 0;
	long fps = 0;
	long gameover = 0;
	long highscore =0;
	long highscore_end =0;
		
	boolean up = false;
	boolean ups = false;
	boolean down = false;
	boolean left = false;
	boolean right = false;
	int escape = 0;
	int speed = 300;
	int speed_plus;
	int anz = 1;
	int leben = 0;
	int leben_show = 0;
	boolean einmalig = false;
	boolean einmalig_2 = false;
	Car car;
	SpriteLib lib;
	Timer timer;
	Timer timer_leben;
	Wall wall;

	BufferedImage backgrounds;
	BufferedImage backgrounds_end;

	public GamePanel (int w, int h)
	{
		this.setPreferredSize(new Dimension(w,h));
		this.setBackground(Color.BLUE);
		JFrame frame = new JFrame("Autorennen");
		frame.setLocation(100,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(this);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
		doInitializations();
	}
	
	public static void main(String[] args){
		new GamePanel(800,600);
	}
	
	protected void doInitializations()
	{
		gameover=0;
		escape=0;
		highscore_end=0;
		leben = 156;
		leben_show = 3;
		einmalig = false;
		einmalig_2 = false;
		highscore=System.currentTimeMillis();
		anz = 1;
		up=false;
		ups=false;
		left=false;
		right=false;
		down=false;
		speed_plus = 0;
		lib = SpriteLib.getInstance();
		backgrounds           = loadPics("pics/Straße_normal.gif",1)[0];
		
		wall = new Wall(lib.getSprite("pics/wall.gif", 1, 1),300,0,100,this);
		wall.setVerticalSpeed(speed);
		car = new Car(lib.getSprite("pics/car.gif", 1, 1),400,300,100,this);
		timer = new Timer(2500,this);
		timer.start();
		if(!once)
		{
			once = true;
			Thread t = new Thread(this);
			t.start();
		}
	}
	//Delta Zeit zwischen nächsten durchlauf
	private void computeDelta()
	{
		delta = System.nanoTime() -last;
		last = System.nanoTime();
		
		fps = ((long) 1e9)/delta;	
	}
	
	public void run()
	{
		while(game_running)
		{
			computeDelta();
			
			if(isStarted())
			{
				checkKeys();
				doLogic();
				moveObjects();
			}
			
			repaint();
			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException e) {}
		}
	
	}
	
	private void doLogic()
	{
		car.collidedWith(wall);
		
		if(car.getCenterX() > 610 )
		{
				leben -= 1;	
		}
		
		if(car.getCenterX() < 205 )
		{
			leben -= 1;
		}
		
		if(leben<156)
		{
			leben_show = 3;
		}
		
		if(leben<104)
		{
			leben_show = 2;
		}
		
		if(leben<52)
		{
			leben_show = 1;
		}
		
		if(leben<1)
		{
			leben_show = 0;
		}
		if((car.remove && gameover==0) || (gameover==0 && escape==1)){
			gameover = System.currentTimeMillis();
		}
		
		if(leben<= 0 && gameover ==0)
		{
			gameover = System.currentTimeMillis();
		}
		if(escape==0 && gameover>0){
			if(System.currentTimeMillis()-gameover>10){
				stopGame();
			}
		}
	}
	
	protected void stopGame()
	{
		highscore_end=gameover-highscore;
		timer.stop();
		JOptionPane.showMessageDialog(null,"Game Over - Highscore: " + highscore_end + "","Information", JOptionPane.OK_CANCEL_OPTION);
		setStarted(false);
	}
	
	protected void moveObjects()
	{
		car.move(delta);
		wall.move_wall(delta);
	}
	
	@Override
	public void paintAll(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(backgrounds, 0, 0, this);
		g.setColor(Color.red);
		g.drawString("Leben: " + leben_show + "", 700, 10);
		wall.drawObjects(g);
		car.drawObjects(g);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.setColor(Color.red);
		g.drawString("FPS: " + Long.toString(fps), 20, 10);
		g.drawString("Zu Starten Enter Drücken", 300, 300);
		if(!isStarted())
		{
			return;
		}
		
		paintAll(g);
	}
	
	protected BufferedImage[] loadPics(String path, int pics)
	{
		BufferedImage[] anim = new BufferedImage[pics];
		BufferedImage source = null;
		
		URL pic_url = getClass().getClassLoader().getResource(path);
		
		try
		{
			source = ImageIO.read(pic_url);
		} catch (IOException e) {}
		
		for(int x=0;x<pics;x++)
		{
			anim[x] =source.getSubimage(x*source.getWidth()/pics,0, source.getWidth()/pics, source.getHeight());
		}
		
		return anim;
	}
	
	protected void checkKeys() {
		// TODO Auto-generated method stub

		if(up){
			car.setVerticalSpeed(-speed);
		}
		
		if(down){
			car.setVerticalSpeed(speed);
		}
		
		if(right){
			car.setHorizontalSpeed(speed);
		}
		
		if(left){
			car.setHorizontalSpeed(-speed);
		}
		
		if(!up&&!down){
			car.setVerticalSpeed(0);
		}
		
		if(ups)
		{
			leben = 3000;
			leben_show+=300;
		}
		
		if(!left&&!right){
			car.setHorizontalSpeed(0);

		}
		
	}
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode()==KeyEvent.VK_UP)
		{
			up = true;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_A)
		{
			ups = true;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_DOWN)
		{
			down = true;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_LEFT)
		{
			left = true;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			right = true;
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode()==KeyEvent.VK_UP)
		{
			up = false;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_A)
		{
			ups = false;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_DOWN)
		{
			down = false;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_LEFT)
		{
			left = false;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			right = false;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_ENTER)
		{
			if(!isStarted())
			{
				doInitializations();
				setStarted(true);
			}
		}
		
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
		{
			if(isStarted())
			{
				escape=1;
				stopGame();
			}
			else
			{
				setStarted(false);
				System.exit(0);
			}
		}
	
	}
	
	public void keyTyped(KeyEvent e)
	{
	
	}
	
	public boolean isStarted()
	{
		return started;
	}
	
	public void setStarted(boolean started)
	{
		this.started = started;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(isStarted() && e.getSource().equals(timer)){
				double c = Math.ceil(Math.random()*(400-200))+200;
				wall = new Wall(lib.getSprite("pics/wall.gif", 1, 1),c,0,100,this);
				wall.setVerticalSpeed(speed+speed_plus);
				speed_plus +=50;
				if(speed_plus>249)
				{
					timer = new Timer(2000,this);
					timer.start();
				}
				
				if(speed_plus>449)
				{
					timer = new Timer(1000,this);
					timer.start();
				}
		}
	}
}