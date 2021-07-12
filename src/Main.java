import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class Main {
	
	public static Timer tm = new Timer();
	public static String titulek = "Snake, ";
	public static int score = 1;
	public static JFrame frame = new JFrame();
	
	public static void main(String[] args) {
		
        frame.setTitle(titulek + "Delka hada: " + score);
        
        Snake snake = new Snake();
        DrawingPanel drawingPanel = new DrawingPanel();
        frame.add(snake);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);    
        frame.setVisible(true);
        frame.setResizable(false);
        
       
		tm.schedule(new TimerTask() {
			@Override
			public void run() {
				if(snake.konec == false) {
					frame.repaint();
				}
			}
		}, 0, 100);
	}

}
