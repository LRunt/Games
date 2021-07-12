import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 
 */

/**
 * @author Lukas Runt
 * @version 1.0 (2021-07-08)
 */
public class Snake extends JPanel{
	
	private long StartTime = System.currentTimeMillis();
	private long ActualTime;
	private int velikostX = 60;
	private int velikostY = 60;
	private int velikostPole = 10;
	private TypSnake[][] hra;
	private Random r = new Random();
	private int hlavaX;
	private int hlavaY;
	private Smer pohyb;
	private Frame frame = new Frame();
	private ArrayList<Position> telo = new ArrayList<>();
	private int pocet = 1;
	public static boolean konec = false;
	
	public Snake() {
		this.setPreferredSize(new Dimension(velikostX * velikostPole, velikostY * velikostPole));
		novaHra();
		this.setFocusable(true);
		
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
					pohyb = Smer.LEFT;
				}	
				if (e.getKeyChar() == 'w' || e.getKeyChar() == 'W') {
					pohyb = Smer.UP;
				}
				if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
					pohyb = Smer.DOWN;
				}
				if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
					pohyb = Smer.RIGHT;
				}
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D)g;
		ActualTime = System.currentTimeMillis();
		if((ActualTime - StartTime) <= 1000) {
			StartTime = System.currentTimeMillis();
			move();
		}
		drawHraciPlocha(g2);
	}
	
	/**
	 * Metoda vygeneruje novou hru
	 */
	private void novaHra() {
		hra = new TypSnake[velikostX][velikostY];
		for(int i = 0; i < hra.length; i++) {
			for(int j = 0; j < hra[i].length; j++) {
				hra[i][j] = TypSnake.FREE;
			}
		}
		for(int i = 0; i < hra.length; i++) {
			hra[i][0] = TypSnake.BORDER;
			hra[i][velikostY - 1] = TypSnake.BORDER;
		}
		for(int i = 0; i < hra[0].length; i++) {
			hra[0][i] = TypSnake.BORDER;
			hra[velikostX - 1][i] = TypSnake.BORDER;
		}
		hlavaX = velikostX/2;
		hlavaY = velikostY/2;
		hra[hlavaX][hlavaY] = TypSnake.SNAKE;
		pohyb = Smer.DOWN;
		generaceOvoce();
	}
	
	/**
	 * Metoda generuje ovoce v mape
	 */
	public void generaceOvoce() {
		int ovoceX = r.nextInt(velikostX);
		int ovoceY = r.nextInt(velikostY);
		while(ovoceX == 0 || ovoceX == velikostX - 1) {
			ovoceX = r.nextInt(velikostX);
		}	
		while(ovoceY == 0 || ovoceY == velikostY - 1) {
			ovoceY = r.nextInt(velikostY);
		}
		hra[ovoceX][ovoceY] = TypSnake.FOOD;
	}
	
	/**
	 * Metoda ktesli hraci plochu
	 * @param g2
	 */
	private void drawHraciPlocha(Graphics2D g2) {
		for(int i = 0; i < hra.length; i++) {
			for(int j = 0; j < hra[i].length; j++) {
				if(hra[i][j] == TypSnake.FREE) {
					g2.setColor(Color.GREEN);
					g2.fillRect(i * velikostPole, j * velikostPole, velikostPole, velikostPole);
				}
				if(hra[i][j] == TypSnake.BORDER){
					g2.setColor(Color.BLACK);
					g2.fillRect(i * velikostPole, j * velikostPole, velikostPole, velikostPole);
				}
				if(hra[i][j] == TypSnake.SNAKE) {
					g2.setColor(Color.RED);
					g2.fillRect(i * velikostPole, j * velikostPole, velikostPole, velikostPole);
				}
				if(hra[i][j] == TypSnake.FOOD) {
					g2.setColor(Color.BLUE);
					g2.fillRect(i * velikostPole, j * velikostPole, velikostPole, velikostPole);
				}
			}
		}
	}
	
	/**
	 * Pohyb hada
	 */
	private void move() {
		telo.add(new Position(hlavaX, hlavaY));
		for(int i = 0; i < pocet; i++) {
			int x = telo.get(i).x;
			int y = telo.get(i).y;
			hra[x][y] = TypSnake.SNAKE;
		}
		hra[telo.get(0).x][telo.get(0).y] = TypSnake.FREE;
		telo.remove(0);
		
		if(pohyb == Smer.DOWN) {
			hlavaY++;
		}
		if(pohyb == Smer.UP) {
			hlavaY--;
		}
		if(pohyb == Smer.LEFT) {
			hlavaX--;
		}
		if(pohyb == Smer.RIGHT) {
			hlavaX++;
		}
		if(hra[hlavaX][hlavaY] == TypSnake.FOOD) {
			pocet++;
			generaceOvoce();
			telo.add(new Position(hlavaX, hlavaY));
			Main.score++;
			Main.frame.setTitle(Main.titulek + "Delka hada: " + Main.score);
		}
		if(hra[hlavaX][hlavaY] == TypSnake.BORDER || hra[hlavaX][hlavaY] == TypSnake.SNAKE) {
			//Main.tm.cancel();
			konec = true;
			if(JOptionPane.showConfirmDialog(frame, "Konec hry", "Prohra", JOptionPane.DEFAULT_OPTION) == JOptionPane.OK_OPTION) {
				konec = false;
				novaHra();
				repaint();
			}
		} else {
			hra[hlavaX][hlavaY] = TypSnake.SNAKE;
		}
		
	}
}
