package game;
//Libary's
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;
//Andere Java Dateien einbinden
import player.Car;
import player.Wall;
import sprite.SpriteLib;

//Klasse Game Panel erstellen, mit KeyListenern (Tastendrücke erkennen), ActionListenern (Actionen bei z.B. Buttondruck) und Runnable (ermöglicht Dauerschleife für Spiel)
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
	//Geschwindigkeit der Barrieren
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
	//Radio Buttons für die Auswahl des Autos
	JRadioButton rbmot = new JRadioButton( "Motorrad" );
	JRadioButton rbaut = new JRadioButton( "Auto" );
	JRadioButton rbfer = new JRadioButton( "Ferrari" );
	JRadioButton rbpan = new JRadioButton( "Panzer" );
	JRadioButton rbjeep = new JRadioButton( "Jeep" );
	JRadioButton rbpol = new JRadioButton( "Polizeiwagen" );
	
	ButtonGroup g = new ButtonGroup();
	JButton Weiter = new JButton( "Start" );
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();

	public GamePanel (int w, int h)
	{
		//Erster Frame mit 2 Paneln für Autoauswahl
		final JFrame frame0 = new JFrame("Highway Runner Auswahl");
		frame0.setLocation(100,100);
		frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame0.setVisible(true);
		
		//Zweiter Frame mir Regeln und Start
		final JFrame frame = new JFrame("Highway Runner");
		frame.setPreferredSize(new Dimension(w,h));
		this.setBackground(Color.BLUE);
		frame.setLocation(100,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		panel1.setLayout( new FlowLayout(FlowLayout.RIGHT, 45, 10) );
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("pics/motorad.gif"));
		JLabel l1 = new JLabel( icon );
		
		ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("pics/auto.png"));
		JLabel l2 = new JLabel( icon2 );
		
		ImageIcon icon3 = new ImageIcon(getClass().getClassLoader().getResource("pics/Ferrari.png" ));
		JLabel l3 = new JLabel( icon3 );
		
		ImageIcon icon4 = new ImageIcon(getClass().getClassLoader().getResource("pics/panzer.png" ));
		JLabel l4 = new JLabel( icon4 );
		
		ImageIcon icon5 = new ImageIcon(getClass().getClassLoader().getResource("pics/Jeep.png"));
		JLabel l5 = new JLabel( icon5 );
		
		ImageIcon icon6 = new ImageIcon(getClass().getClassLoader().getResource("pics/Polizeiauto.gif"));
		JLabel l6 = new JLabel( icon6 );
		
		
		panel1.add(l1);
		panel1.add(l2);
		panel1.add(l3);
		panel1.add(l4);
		panel1.add(l5);
		panel1.add(l6);
		
		
		
		panel2.add(rbmot);
		panel2.add(Weiter);
		panel2.add(rbmot);
		panel2.add(rbaut);
		panel2.add(rbfer);
		panel2.add(rbpan);
		panel2.add(rbjeep);
		panel2.add(rbpol);
		
	    rbaut.setSelected( true );
	    g.add( rbmot );
	    g.add( rbaut );
	    g.add( rbfer );
	    g.add( rbpan );
	    g.add( rbjeep );
	    g.add( rbpol );
	    frame0.add( panel1,BorderLayout.PAGE_START);
	    frame0.add( panel2,BorderLayout.PAGE_END);
	    frame0.add(this);
	    frame0.pack();
	    ActionListener al = new ActionListener() {
	        public void actionPerformed( ActionEvent e ) {
	        	frame0.setVisible(false);
	        	frame.setVisible(true);
	        }
	      };
	      Weiter.addActionListener( al );
	      
	      

		
		frame.addKeyListener(this);
		frame.add(this);
		frame.pack();
		doInitializations();
		
		

	
	}

	public static void main(String[] args){
		new GamePanel(800,600);
	}
	
	protected void doInitializations()
	{
		//Variablen für Start des Spiels vorbereiten
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
		//Mit Hilfe der Sprite Klasse Bilder für auto, barrieren und hintergrund erzeugen etc.
		lib = SpriteLib.getInstance();
		backgrounds           = loadPics("pics/background.gif",1)[0];
		
		wall = new Wall(lib.getSprite("pics/wall.gif", 1, 1),300,0,100,this);
		wall.setVerticalSpeed(speed);
		

		if(rbmot.isSelected()) {
			car = new Car(lib.getSprite("pics/motorad.gif", 1, 1),400,300,100,this);
		}
		else if(rbaut.isSelected()) {
			car = new Car(lib.getSprite("pics/auto.png", 1, 1),400,300,100,this);
		}
		else if(rbfer.isSelected()) {
			car = new Car(lib.getSprite("pics/Ferrari.png", 1, 1),400,300,100,this);
		}
		else if(rbpan.isSelected()) {
			car = new Car(lib.getSprite("pics/panzer.png", 1, 1),400,300,100,this);
		}
		else if(rbjeep.isSelected()) {
			car = new Car(lib.getSprite("pics/Jeep.png", 1, 1),400,300,100,this);
		}
		else if(rbpol.isSelected()) {
			car = new Car(lib.getSprite("pics/Polizeiauto.gif", 1, 1),400,300,100,this);
		}

		
		
		//Timer für Barrieren
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
	//Run schleife - Malt Bilder immer neu
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
		//Abfragen für Collision mit Barriere, mit Rasen, Lebensberechnung und Verloren (Zeitstop für Highscore)
		car.collidedWith(wall);
		
		if(car.getCenterX() > 610 )
		{
				leben -= 1;	
		}
		
		if(car.getCenterX() < 205 )
		{
			leben -= 1;
		}
		// Da bild pro minute circa 50-60 mal neu gemalt wird, ist 50-60 mal pro minute ein kontakt mit dem Gras => so umständlich gemacht
		leben_show = leben/52;
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
	//Highscore berechnen, Timer stop, Game OVer Message 
	protected void stopGame()
	{
		highscore_end=gameover-highscore;
		timer.stop();
		JOptionPane.showMessageDialog(null,"Game Over - Highscore: " + highscore_end + "","Information", JOptionPane.OK_CANCEL_OPTION);
		setStarted(false);
	}
	
	// Aufruf der Methoden zum bewegen der AUtos und Barrieren
	protected void moveObjects()
	{
		car.move(delta);
		wall.move_wall(delta);
	}
	
	//Malen aller Komponenten
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
		g.drawString("Weiche den Hindernissen aus und meide das Gras!", 250, 200);
		g.drawString("Zu Starten Enter Drücken", 300, 300);
		
		if(!isStarted())
		{
			return;
		}
		
		paintAll(g);
	}
	//Methoden zum Erzeugen einer Animation ==> Funktioniert aber noch nicht richtig :D
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
	
	//Was tun bei bestimmten Tastendruck
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
	//Abfragen der gedrückten Tasten
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
	
	//Action für Immer schneller werdende Wälle
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