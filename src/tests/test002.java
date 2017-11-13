package tests;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.BitAI.neural.NeuralNetwork;
import com.BitAI.neural.activation.ActivationLeakyReLu;
import com.BitAI.neural.activation.ActivationLinear;
import com.BitAI.neural.activation.ActivationSigmoid;
import com.BitAI.neural.activation.ActivationSinusoid;
import com.BitAI.neural.activation.ActivationTANH;
import com.BitAI.neural.genetic.GeneticAlgorithm;
import com.BitAI.neural.genetic.NetworkScore;
import com.BitAI.neural.layers.BasicLayer;
import com.BitAI.neural.layers.Layer;
import com.google.gson.Gson;

public class test002 extends JFrame {

	public static float playerX = 15, playerY = (360 / 2) - 50 / 2;
	public static Rectangle playerRect = new Rectangle();

	public static ArrayList<Ball> balls = new ArrayList<Ball>();

	public static int sleepMS = 2;
	public static int scoreAI, scoreWall;
	public boolean dump;

	public static JPanel panel;
	ActivationTANH act = new ActivationTANH();
	
	//Menu stuff begin
	JEditorPane webWindow = new JEditorPane();
	JMenu file = new JMenu("File");
	JMenuBar menu = new JMenuBar();
	JMenuItem open = new JMenuItem("Open file...");

	public test002() {
		webWindow.setEditable(false);
		setJMenuBar(menu);
	    menu.add(file);
	    file.add(open);
	    open.addActionListener(
	    	    new ActionListener() {
	    	        public void actionPerformed(ActionEvent enterPress) {
	    	            try {
	    	            	//final String dir = System.getProperty("user.dir");
	    	                //Desktop.getDesktop().open(new File(dir));
	    	            	final JFileChooser fc = new JFileChooser();
	    	                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    	                int return_val = fc.showOpenDialog(test002.this);
	    	                if(return_val == JFileChooser.APPROVE_OPTION) {
	    	                	String filepath = fc.getSelectedFile().toString();
	    	                	System.out.println(filepath);
	    	                } else {
	    	                	System.out.println("An error occurred trying to open the file, please blame Gustav...");
	    	                }
	    	            	
	    	            } catch (Exception e) {
	    	                // TODO Auto-generated catch block
	    	                e.printStackTrace();
	    	            }
	    	        }
	    	    });
	    
	    //Menu stuff END
		
		setTitle("Genetic Pong");
		setSize(500, 360);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new GamePane();

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				System.out.println("key pressed");
				int keycode = e.getKeyCode();

				if (keycode == KeyEvent.VK_1) {
					sleepMS = 2;
				}

				if (keycode == KeyEvent.VK_2) {
					sleepMS = 10;
				}

				if (keycode == KeyEvent.VK_3) {
					sleepMS = 15;
				}
				if (keycode == KeyEvent.VK_0) {
					dump = true;
				}

			}
		});

		panel.setBackground(Color.BLACK);
		getContentPane().add(panel, BorderLayout.CENTER);

		new Thread(new Runnable() {

			@Override
			public void run() {

				NeuralNetwork network = new NeuralNetwork(new BasicLayer[] {
						new BasicLayer(2, act), new BasicLayer(5, act), new BasicLayer(1, act) });
				GeneticAlgorithm ga = new GeneticAlgorithm(network, 10, 1000000);

				ga.train(new NetworkScore() {

					@Override
					public float calculateNetworkScore(NeuralNetwork net) {
						return loop(net);
					}
				});

			}
		}).start();

		this.requestFocus();

	}
	
	/*public String getDump() {
		
	}*/
	
	public static void main(String[] args) {
		new test002().setVisible(true);
	}

	public int loop(NeuralNetwork net) {

		int score = 0;
		boolean running = true;
		
		if(dump) {
			try {
				net.dump(net.getLayers());
				System.out.println("dumped");
			} catch (Exception e) {
				e.printStackTrace();
			}
			dump = false;
		}

		try {
			balls.clear();

			balls.add(new Ball(new Random().nextInt(300) - 30));

			playerX = 15;
			playerY = (360 / 2) - 50 / 2;

			while (running) {

				float[] output = net.compute(new float[] {playerY, balls.get(0).y });

				// if(playerY>0&&playerY<360-50)
				playerY += output[0] * 10;
				// System.out.println(output[0]);

				if (Float.isNaN(playerY)) {
					System.out.println("PlayerY: " + playerY+" output: "+output[0]);
				}

				playerRect.setBounds((int) playerX, (int) playerY, 10, 50);

				panel.repaint();

				for (int i = 0; i < balls.size(); i++) {
					if (balls.get(i).x < 0) {
						running = false;
						scoreWall++;
						break;
					}

					if (balls.get(i).rect.intersects(playerRect)) {
						score++;
						System.out.println("score: " + score);
						balls.get(i).xforce = 1;
						balls.get(i).x += 5;
					}

				}

				try {
					Thread.sleep(sleepMS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("Returning score: " + score);
		return score;
	}

	public class Ball {

		float x = 500, y, xforce = -1, yforce = -1;

		Rectangle rect = new Rectangle();

		public Ball(float y) {

			this.y = y;

		}

		public void render(Graphics g) {
			g.setColor(Color.WHITE);
			g.fillOval((int) x, (int) y, 15, 15);

			if (x > 500) {
				xforce = -1;

				scoreAI++;
			}

			if (y < 0)
				yforce = 1;

			if (y > 360 - 30)
				yforce = -1;

			x += xforce * 5;
			y += yforce * 5;
			rect.setBounds((int) x, (int) y, 15, 15);
		}

	}

	public class GamePane extends JPanel {

		@Override
		public void paint(Graphics g) {
			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setColor(Color.white);

			g.setFont(new Font("Courier new", Font.PLAIN, 32));
			g.drawString(scoreAI + "/" + scoreWall, 140, 30);

			g.fillRect((int) playerX, (int) playerY, 10, 50);

			for (int i = 0; i < balls.size(); i++)
				balls.get(i).render(g);

		}

	}

}
