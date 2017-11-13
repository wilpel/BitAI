package com.BitAI.neural.genetic;

import java.util.Random;

import com.BitAI.neural.NeuralNetwork;

public class Mutator {

	public NeuralNetwork mutate(NeuralNetwork net, float mutationMin, float mutationMax) {

		NeuralNetwork network = net;

		for (int i = 0; i < network.getLayers().length; i++) {
			for (int k = 0; k < network.getLayers()[i].weights.length; k++) {
				for (int l = 0; l < network.getLayers()[i].weights[0].length; l++) {
					if (new Random().nextBoolean()) {
						network.getLayers()[i].weights[k][l] = (float) (network.getLayers()[i].weights[k][l] + (Math.random()-0.5f)/10);
						//System.out.println();
					}
				}
			}

		}

		return network;

	}

}
