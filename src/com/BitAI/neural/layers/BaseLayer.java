package com.BitAI.neural.layers;

import java.io.Serializable;
import java.util.Random;

import com.BitAI.neural.Constants;
import com.BitAI.neural.activation.ActivationFunction;

public abstract class BaseLayer implements Serializable {

	public float[] gamma;
	public float[] error;
	public int inputNeuronCount;
	public int outputNeuronCount;

	public float[] output;
	public float[] input;
	
	public float[][] weights;
	public float[][] weightsDelta;


	protected ActivationFunction af;

	public BaseLayer(int inputNeuronCount, int outputNeuronCount, ActivationFunction af) {

		this.inputNeuronCount = inputNeuronCount;
		this.outputNeuronCount = outputNeuronCount;

		this.af = af;

		output = new float[outputNeuronCount];
		input = new float[inputNeuronCount];
		
		weights = new float[outputNeuronCount][inputNeuronCount];
		weightsDelta = new float[outputNeuronCount][inputNeuronCount];

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
