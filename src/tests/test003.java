package tests;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
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
import com.BitAI.neural.activation.ActivationTANH;
import com.BitAI.neural.genetic.GeneticAlgorithm;
import com.BitAI.neural.genetic.NetworkScore;
import com.BitAI.neural.layers.HandleLayer;
import com.BitAI.neural.layers.Layer;

public class test003 extends JFrame {

	public static float playerX = 0, playerY = 3;
	public static Rectangle playerRect = new Rectangle();

	public static ArrayList<Ball> balls = new ArrayList<Ball>();
	
	public NeuralNetwork new_nn;

	public static int sleepMS = 0;
	public static int scoreAI, scoreWall;
	public boolean dump;

	public static int IMAGE_DIVIDER = 10;
	
	public static BufferedImage sight = new BufferedImage(200/IMAGE_DIVIDER, 100/IMAGE_DIVIDER, BufferedImage.TYPE_BYTE_BINARY);;
	

	public static int NEURAL_SIZE = (200/IMAGE_DIVIDER)*(100/IMAGE_DIVIDER);
	
	public static JPanel panel;
	ActivationTANH act = new ActivationTANH();
	
	//Menu stuff begin
	JEditorPane webWindow = new JEditorPane();
	JMenu file = new JMenu("File");
	JMenuBar menu = new JMenuBar();
	JMenuItem open = new JMenuItem("Open file...");

	public test003() {
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
	    	                int return_val = fc.showOpenDialog(test003.this);
	    	                if(return_val == JFileChooser.APPROVE_OPTION) {
	    	                	String filepath = fc.getSelectedFile().toString();
	    	                	System.out.println(filepath);
	    	                	new_nn = NeuralNetwork.loadDump(filepath);
	    	                	//
	    	                	//LOADED LAYER ANVÄNDS INTE ÄN
	    	                	//
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
					sleepMS = 0;
				}

				if (keycode == KeyEvent.VK_2) {
					sleepMS = 30;
				}

				if (keycode == KeyEvent.VK_3) {
					sleepMS = 50;
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
				
				NeuralNetwork network = new NeuralNetwork(new HandleLayer[] {
						new HandleLayer(NEURAL_SIZE, act), new HandleLayer(600, act), new HandleLayer(600, act), new HandleLayer(1, act) });
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
		new test003().setVisible(true);
	}

	public int loop(NeuralNetwork net) {

		int score = 0;
		boolean running = true;
		
//		if(dump) {
//			try {
//				net.dump(net);
//				System.out.println("dumped");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			dump = false;
//		}

		try {
			balls.clear();

			balls.add(new Ball(new Random().nextInt(10)-1));

			playerX = 0;
			playerY = 1;

			while (running) {

				setTitle(scoreAI+"/"+scoreWall);
				
				float[] imageToFloatArray = new float[NEURAL_SIZE];
				int index = 0;
				
				for(int x = 0; x < sight.getWidth(); x++) {
					for(int y = 0; y < sight.getHeight(); y++) {
						imageToFloatArray[index] = sight.getRGB(x, y)==Color.white.getRGB()?1:0;
						
						index++;
					}	
				}
				
				float[] output = net.compute(imageToFloatArray);

				// if(playerY>0&&playerY<100-50)
				playerY += output[0];
				//System.out.println(output[0]);

				if (Float.isNaN(playerY)) {
					System.out.println("PlayerY: " + playerY+" output: "+output[0]);
				}

				playerRect.setBounds((int) playerX, (int) playerY, 1, 3);

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
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_BYTE_BINARY);

	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}

	public class Ball {

		float x = 20, y, xforce = -1, yforce = -0.1f;

		Rectangle rect = new Rectangle();

		public Ball(float y) {

			this.y = y;

		}

		public void render(Graphics g) {
			g.setColor(Color.WHITE);
			g.fillRect((int)x, (int)y, 1, 1);

			if (x > 20) {
				xforce = -1;

				scoreAI++;
			}

			if (y < 0)
				yforce = 1;

			if (y > 10)
				yforce = -1;

			x += xforce;
			y += yforce;
			rect.setBounds((int) x, (int) y, 1, 1);
		}

	}

	public class GamePane extends JPanel {

		@Override
		public void paint(Graphics g) {
			
			Graphics2D g2d = sight.createGraphics();

			
			
			g2d.setColor(Color.black);
			g2d.fillRect(0, 0, sight.getWidth(), sight.getHeight());

			g2d.setColor(Color.white);

			g2d.fillRect((int) playerX, (int) playerY, 1, 3);

			for (int i = 0; i < balls.size(); i++)
				balls.get(i).render(g2d);
			
			
			g.drawImage(sight, 0, 0, 500, 360, null);

//			g.setFont(new Font("Courier new", Font.PLAIN, 32));
//			g.drawString(scoreAI + "/" + scoreWall, 140, 30);
		
		}

	}

}
