import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 
 */

/**
 * @author Lukas Runt
 * @version 1.0(2021-07-04)
 */
public class DrawingPanel extends JPanel{
	/** Velikost hraciho pole*/
	private int velikost;
	private int velikostPole;
	private Typ[][] hra;
	private int velikostPera = 5;
	private int pocitadlo;
	private Frame frame = new Frame();

	public DrawingPanel() {
		this.setPreferredSize(new Dimension(600, 600));	
		velikost = 3;
		pocitadlo = 0;
		vytvorHru();
		
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				int x = (int)(e.getX()/velikostPole);
				int y = (int)(e.getY()/velikostPole);
				Typ typ;
				if(hra[x][y] == Typ.FREE) {
					if(pocitadlo % 2 == 0) {
						typ = Typ.KRIZEK;
						hra[x][y] = Typ.KRIZEK; 
					} else {
						typ = Typ.KOLECKO;
						hra[x][y] = Typ.KOLECKO; 
					}
					repaint();
					pocitadlo += 1;
					if(testVyhry(x, y)) {
						//System.out.println("Vyhra");
						if(JOptionPane.showConfirmDialog(frame, "Vyhral hrac: " + typ, "Vyhra", JOptionPane.DEFAULT_OPTION) == JOptionPane.OK_OPTION) {
							vytvorHru();
							repaint();
							pocitadlo = 0;
						}
					}
					if(pocitadlo == velikost * velikost) {
						//System.out.println("Remiza");
						if(JOptionPane.showConfirmDialog(frame, "Nikdo nevyhral", "Remiza", JOptionPane.DEFAULT_OPTION) == JOptionPane.OK_OPTION) {
							vytvorHru();
							repaint();
							pocitadlo = 0;
						}
					}
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {		
			}
		});
	}
	

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(5));
		
		drawHraciPole(g2);
		
	}
	
	private void vytvorHru() {
		hra = new Typ[velikost][velikost];
		for(int i = 0; i < hra.length; i++) {
			for(int j = 0; j < hra[i].length; j++) {
				hra[i][j] = Typ.FREE;
			}
		}
	}
	
	private void drawHraciPole(Graphics2D g2) {
		velikostPole = Math.min(this.getHeight(), this.getWidth())/velikost;
		
		Rectangle2D pozadi = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
		g2.setColor(Color.WHITE);
		g2.fill(pozadi);
		
		for(int i = 0; i < hra.length; i++) {
			for(int j = 0; j < hra.length; j++) {
				if(hra[i][j] == Typ.KOLECKO) {
					drawKolecko(g2, i, j);
				} else if(hra[i][j] == Typ.KRIZEK) {
					drawKrizek(g2, i, j);
				}
			}
		}
		g2.setColor(Color.BLACK);
		for(int i = 0; i <= velikost; i++) {
			g2.drawLine(0 + i * velikostPole, 0, 0 + i * velikostPole, this.getHeight());
		}
		for(int i = 0; i <= velikost; i++) {
			g2.drawLine(0, 0 + i * velikostPole, this.getWidth(), 0 + i * velikostPole);
		}
	}
	
	private void drawKolecko(Graphics2D g2, int x, int y) {
		g2.setColor(Color.BLUE);
		g2.drawOval(x * velikostPole + velikostPera, y * velikostPole + velikostPera, velikostPole - velikostPera * 2, velikostPole - velikostPera * 2);
	}
	
	private void drawKrizek(Graphics2D g2, int x, int y) {
		g2.setColor(Color.RED);
		g2.drawLine(x * velikostPole, y*velikostPole, (x + 1) * velikostPole, (y + 1) * velikostPole);
		g2.drawLine((x + 1) * velikostPole, y * velikostPole, x * velikostPole, (y + 1) * velikostPole);
	}
	
	private boolean testVyhry(int x, int y) {
		Typ typ = hra[x][y];
		int pocet = 1;
		int xt = x;
		xt--;
		while(xt >= 0 && hra[xt][y] == typ) {
			pocet++;
			xt--;
		}
		xt = x;
		++xt;
		while(xt < velikost && hra[xt][y] == typ) {
			pocet++;
			xt++;
		}
		if(pocet == 3) {
			return true;
		}
		pocet = 1;
		int yt = y;
		yt--;
		while(yt >= 0 && hra[x][yt] == typ) {
			pocet++;
			yt--;
		}
		yt = y;
		++yt;
		while(yt < velikost && hra[x][yt] == typ) {
			pocet++;
			yt++;
		}
		if(pocet == 3) {
			return true;
		}
		pocet = 1;
		xt = x;
		yt = y;
		xt--;
		yt--;
		while(yt >= 0 && xt >= 0 && hra[xt][yt] == typ) {
			pocet++;
			yt--;
			xt--;
		}
		yt = y;
		xt = x;
		++xt;
		++yt;
		while(yt < velikost && xt < velikost && hra[xt][yt] == typ) {
			pocet++;
			xt++;
			yt++;
		}
		if(pocet == 3) {
			return true;
		}
		pocet = 1;
		xt = x;
		yt = y;
		xt--;
		yt++;
		while(yt < velikost && xt >= 0 && hra[xt][yt] == typ) {
			pocet++;
			yt++;
			xt--;
		}
		yt = y;
		xt = x;
		++xt;
		--yt;
		while(yt >= 0 && xt < velikost && hra[xt][yt] == typ) {
			pocet++;
			xt++;
			yt--;
		}
		if(pocet == 3) {
			return true;
		}
		return false;
	}
	
}
