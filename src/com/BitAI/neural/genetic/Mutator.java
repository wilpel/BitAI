package com.BitAI.neural.genetic;

import java.util.Random;

import com.BitAI.neural.NeuralNetwork;

public class Mutator {

	public NeuralNetwork mutate(NeuralNetwork net, float mutationMin, float mutationMax) {

		NeuralNetwork network = net;
		
		for (int i = 0; i < network.getLayers().length; i++) {
			for (int j = 0; j < network.getLayers().length; j++) {
				for (int k = 0; k < network.getLayers()[j].weights.length; k++) {
					for (int l = 0; l < network.getLayers()[j].weights[0].length; l++) {
						if(new Random().nextBoolean()) {
						network.getLayers()[j].weights[k][l] = (float) (network.getLayers()[j].weights[k][l]
								* (mutationMin + Math.random() * (mutationMax - mutationMin)));
						}
					}
				}
			}

		}
		
		
		return network;

	}

}
