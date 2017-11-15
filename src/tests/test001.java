package tests;

import java.math.BigDecimal;

import com.BitAI.neural.NeuralNetwork;
import com.BitAI.neural.activation.ActivationTANH;
import com.BitAI.neural.genetic.GeneticAlgorithm;
import com.BitAI.neural.genetic.NetworkScore;
import com.BitAI.neural.layers.HandleLayer;

public class test001 {

	public static void main(String[] args) {

		NeuralNetwork network = new NeuralNetwork(new HandleLayer[] { new HandleLayer(1, new ActivationTANH()), new HandleLayer(8, new ActivationTANH()), new HandleLayer(1, new ActivationTANH()) });
		GeneticAlgorithm ga = new GeneticAlgorithm(network, 10, 1000);

		System.out.println(network.compute(new float[] { 0.5f })[0] * 1000);

		System.out.println(network.compute(new float[] { 0.5f })[0] * 1000);

		System.out.println(network.compute(new float[] { 0.5f })[0] * 1000);

		System.out.println(network.compute(new float[] { 0.5f })[0] * 1000);

		ga.train(new NetworkScore() {

			@Override
			public float calculateNetworkScore(NeuralNetwork net) {
				float score = net.compute(new float[] { 0.5f })[0];

				if (score > 0.9)
					score = 999999999;

//				BigDecimal bd = new BigDecimal(Float.toString(score));
//				bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
//				System.out.println("actual score: " + bd.floatValue());
				return (int) (score * 100000);
			}
		});

	}

}
