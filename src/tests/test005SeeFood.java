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

public class test005SeeFood {

	public static int AMOUNT_OF_OBJECTS = 2;
	public static int HOTDOG = 0;
	public static int NOT_HOTDOG = 1;

	public static int TRAIN_IRETATIONS = 10000;

	public static NeuralNetwork network;

	public static ActivationTANH act = new ActivationTANH();

	public static void main(String[] args) {

		network = new NeuralNetwork(new HandleLayer[] { new HandleLayer(20 * 15, act), new HandleLayer(40, act),
				new HandleLayer(100, act), new HandleLayer(40, act), new HandleLayer(AMOUNT_OF_OBJECTS, act) });

		//for (int j = 0; j < 100; j++) {
			// TASKS
			for (int i = 0; i < 10; i++) {
				System.out.println("training: " + i + "/" + 10);
				train("http://www.wienerschnitzel.com/wp-content/uploads/2014/10/hotdog_mustard-main.jpg",
						0);
				
				train("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Hot_dog_with_mustard.png/1200px-Hot_dog_with_mustard.png",
						0);
				
				train("https://o.aolcdn.com/images/dims3/GLOB/legacy_thumbnail/630x315/format/jpg/quality/85/http%3A%2F%2Fi.huffpost.com%2Fgen%2F3596402%2Fimages%2Fn-HOT-DOG-EATING-628x314.jpg",
						0);
				
				train("http://www.centives.net/S/wp-content/uploads/2012/04/040912_0103_HotDogStati1.png",
						0);

			
			}
			// TESTS
			performTest("https://regmedia.co.uk/2017/07/07/hotdog.jpg?x=1200&y=794");
			performTest("https://www.runnersworld.com/sites/runnersworld.com/files/styles/listicle_slide_custom_user_phone_1x/public/reebok_harmony_w_400.jpg?itok=U3lsw4qz");
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

			float[] expectedArray = new float[AMOUNT_OF_OBJECTS];

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

			for (int i = 0; i < AMOUNT_OF_OBJECTS; i++) {
				if (output[i] > highest) {
					highest = output[i];
					index = i;
				}
			}

			if (index == HOTDOG) {
				System.out.println("Hotdog");
			} else if (index == NOT_HOTDOG) {
				System.out.println("Not Hotdog");

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
