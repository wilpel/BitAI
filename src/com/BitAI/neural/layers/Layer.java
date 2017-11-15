package com.BitAI.neural.layers;

import java.io.Serializable;
import java.util.Random;

import com.BitAI.neural.Constants;
import com.BitAI.neural.activation.ActivationFunction;

public class Layer extends BaseLayer {

	private static Random random;

	public float[] gamma;
	public float[] error;


	public Layer(int inputNeuronCount, int outputNeuronCount, ActivationFunction af) {
		super(inputNeuronCount, outputNeuronCount, af);
		
		this.random = new Random();
		gamma = new float[outputNeuronCount];
		error = new float[outputNeuronCount];

		for (int i = 0; i < outputNeuronCount; i++) {
			for (int j = 0; j < inputNeuronCount; j++) {

				weights[i][j] = random.nextFloat() - 0.5f;
			}
		}

	}

	public float square(float value) {
		return 1 - (value * value);
	}

	public float[] feedForward(float[] input) {

		
		
		this.input = input;

		for (int i = 0; i < outputNeuronCount; i++) {

			output[i] = 0;

			for (int j = 0; j < inputNeuronCount; j++) {
				
				output[i] += input[j] * weights[i][j];
				
		
			}

			output[i] = af.process(output[i]);

		}


		return output;
	}

	public void backPropagationOutput(float[] expected) {

		for (int i = 0; i < outputNeuronCount; i++) {
			error[i] = output[i] - expected[i];
		}

		for (int i = 0; i < outputNeuronCount; i++) {
			gamma[i] = error[i] * square(output[i]);
		}

		for (int i = 0; i < outputNeuronCount; i++) {
			for (int j = 0; j < inputNeuronCount; j++) {

				weightsDelta[i][j] = gamma[i] * input[j];

			}
		}

	}

	public void backPropagationHidden(float[] gammaForward, float[][] weightsForward) {

		for (int i = 0; i < outputNeuronCount; i++) {
			gamma[i] = 0;

			for (int j = 0; j < gammaForward.length; j++) {
				gamma[i] += gammaForward[j] * weightsForward[j][i];
			}

			gamma[i] *= square(output[i]);

		}

		for (int i = 0; i < outputNeuronCount; i++) {
			for (int j = 0; j < inputNeuronCount; j++) {

				weightsDelta[i][j] = gamma[i] * input[j];

			}
		}

	}

	public void updateWeights() {

		for (int i = 0; i < outputNeuronCount; i++) {
			for (int j = 0; j < inputNeuronCount; j++) {

				weights[i][j] -= weightsDelta[i][j] * Constants.LEARNING_MULTIPLIER;

			}
		}

	}

	public ActivationFunction getActivationFunction() {
		return af;
	}

}