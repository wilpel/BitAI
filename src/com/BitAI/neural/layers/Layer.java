package com.BitAI.neural.layers;

import java.io.Serializable;
import java.util.Random;

import com.BitAI.neural.Constants;
import com.BitAI.neural.activation.ActivationFunction;

public class Layer extends BaseLayer {

	private static Random random;


	public Layer(int inputNeuronCount, int outputNeuronCount, ActivationFunction af) {
		super(inputNeuronCount, outputNeuronCount, af);
		
		this.random = new Random();

		for (int i = 0; i < outputNeuronCount; i++) {
			for (int j = 0; j < inputNeuronCount; j++) {

				weights[i][j] = random.nextFloat() - 0.5f;
			}
		}

	}

}