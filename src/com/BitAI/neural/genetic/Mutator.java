package com.BitAI.neural.genetic;

import java.util.Random;

import com.BitAI.neural.NeuralNetwork;

public class Mutator {

	public static Random rand = new Random();

	public NeuralNetwork mutate(NeuralNetwork net, float mutationMin, float mutationMax) {

		NeuralNetwork network = net;

		for (int i = 0; i < network.getLayers().length; i++) {
			for (int k = 0; k < network.getLayers()[i].weights.length; k++) {
				for (int l = 0; l < network.getLayers()[i].weights[0].length; l++) {
					if (new Random().nextBoolean()) {
						float modification = (rand.nextFloat() * (mutationMax - mutationMin) + mutationMin);

						if (modification == 0)
							modification = 0.0000001f;

						network.getLayers()[i].weights[k][l] = (float) (network.getLayers()[i].weights[k][l] + modification);
					}
				}
			}

		}

		return network;

	}

}
