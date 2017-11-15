package tests;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.BitAI.neural.NeuralNetwork;
import com.BitAI.neural.activation.ActivationTANH;
import com.BitAI.neural.layers.HandleLayer;
import com.BitAI.neural.layers.LayerFactory;

public class test004Flags {

	public static int AMOUNT_OF_FLAGS = 4;
	public static int FLAG_ENGLAND = 0;
	public static int FLAG_JAPAN = 1;
	public static int FLAG_FINLAND = 2;
	public static int FLAG_USA = 3;

	public static int TRAIN_IRETATIONS = 100000;

	public static NeuralNetwork network;

	public static ActivationTANH act = new ActivationTANH();

	public static void main(String[] args) {

		LayerFactory lf = new LayerFactory();
		
		lf.addLayer(20*15, new ActivationTANH(), LayerFactory.TYPE_SIMPLELAYER);
		lf.addLayer(8, new ActivationTANH(), LayerFactory.TYPE_SIMPLELAYER);
		lf.addLayer(AMOUNT_OF_FLAGS, new ActivationTANH(), LayerFactory.TYPE_SIMPLELAYER);
		
		network = new NeuralNetwork(lf);

		//for (int j = 0; j < 100; j++) {
			// TASKS
			for (int i = 0; i < 10; i++) {
				System.out.println("training: " + i + "/" + 10);
				train("https://upload.wikimedia.org/wikipedia/en/thumb/b/be/Flag_of_England.svg/1200px-Flag_of_England.svg.png",
						0);
				train("https://upload.wikimedia.org/wikipedia/en/thumb/9/9e/Flag_of_Japan.svg/1200px-Flag_of_Japan.svg.png",
						1);
				train("https://upload.wikimedia.org/wikipedia/commons/thumb/b/bc/Flag_of_Finland.svg/255px-Flag_of_Finland.svg.png",2);
				train("https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/US_flag_51_stars.svg/2000px-US_flag_51_stars.svg.png",3);
			}
			// TESTS
			performTest("https://upload.wikimedia.org/wikipedia/en/thumb/b/be/Flag_of_England.svg/1200px-Flag_of_England.svg.png");
			performTest("https://upload.wikimedia.org/wikipedia/en/thumb/9/9e/Flag_of_Japan.svg/1200px-Flag_of_Japan.svg.png");
			performTest("https://upload.wikimedia.org/wikipedia/commons/thumb/7/73/Flag_of_Genoa.svg/1024px-Flag_of_Genoa.svg.png");
			performTest("https://images-na.ssl-images-amazon.com/images/I/21fcVEuKyyL.jpg");
		//}
			
			
			while(true) {
				
				System.out.println("Paste a direct link to a image to test");
				
				Scanner scanner = new Scanner(System.in);	
				
				String input = scanner.nextLine();
				
				System.out.println("input:"+input);
				
				performTest(input);
			}
	}

	public static void train(String url, int expected) {

		System.out.println(url);

		try {
			BufferedImage img = toBufferedImage(
					ImageIO.read(new URL(url)).getScaledInstance(20, 15, BufferedImage.TYPE_USHORT_GRAY));

			float[] data = new float[20 * 15];

			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {

					data[x + y] = (img.getRGB(x, y) & 0xFF) / 100f;

				}
			}

			float[] expectedArray = new float[AMOUNT_OF_FLAGS];

			for (int i = 0; i < expectedArray.length; i++)
				expectedArray[i] = 0;

			expectedArray[expected] = 1;

			for (int i = 0; i < TRAIN_IRETATIONS; i++) {

				network.compute(data);
				network.backPropagate(expectedArray);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void performTest(String url) {
		try {
			BufferedImage img = toBufferedImage(
					ImageIO.read(new URL(url)).getScaledInstance(20, 15, BufferedImage.TYPE_USHORT_GRAY));

			float[] data = new float[20 * 15];

			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {

					data[x + y] = (img.getRGB(x, y) & 0xFF) / 100f;

				}
			}

			float[] output = network.compute(data);

			float highest = 0;
			int index = 0;

			for (int i = 0; i < AMOUNT_OF_FLAGS; i++) {
				if (output[i] > highest) {
					highest = output[i];
					index = i;
				}
			}

			if (index == FLAG_ENGLAND) {
				System.out.println("England");
			} else if (index == FLAG_JAPAN) {
				System.out.println("Japan");

			} else if (index == FLAG_FINLAND) {
				System.out.println("Finland");

			} else if (index == FLAG_USA) {
				System.out.println("USA");

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

}
