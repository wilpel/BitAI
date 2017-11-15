package com.BitAI.neural.layers;

import java.io.Serializable;
import java.util.Arrays;

import com.BitAI.neural.Constants;
import com.BitAI.neural.activation.ActivationFunction;

public class BaseLayer implements Serializable {

	public int inputNeuronCount;
	public int outputNeuronCount;

	public float[] output;
	public float[] input;
	
	public float[][] weights;
	public float[][] weightsDelta;
	

	public float[] gamma;
	public float[] error;
	
	public boolean drop;


	protected ActivationFunction af;

	public BaseLayer(int inputNeuronCount, int outputNeuronCount, ActivationFunction af) {

		this.inputNeuronCount = inputNeuronCount;
		this.outputNeuronCount = outputNeuronCount;

		this.af = af;

		output = new float[outputNeuronCount];
		input = new float[inputNeuronCount];
		
		weights = new float[outputNeuronCount][inputNeuronCount];
		weightsDelta = new float[outputNeuronCount][inputNeuronCount];

		gamma = new float[outputNeuronCount];
		error = new float[outputNeuronCount];
		
	}
	
	public float square(float value) {
		return 1 - (value * value);
	}

	public float[] feedForward(float[] input) {
		
		this.input = input;

		for (int i = 0; i < outputNeuronCount; i++) {

			output[i] = 0;

			for (int j = 0; j < inputNeuronCount; j++) {

				if(drop) {
					int[] todrop = dropOut(outputNeuronCount);
					int start_index = 0;
					int curr_todrop = todrop[start_index];
					if (i == curr_todrop) {
						output[i] = 0;
						start_index++;
					} else {
						output[i] += input[j] * weights[i][j];
					}

				} else {
					output[i] += input[j] * weights[i][j];
				}
				
		
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
	
	public int[] dropOut(int outputNeuronCount) {
	    int[] dropouts = new int[outputNeuronCount/10]; //10% dropout       
	    for(int i = 0; i < dropouts.length; i++) {
	      dropouts[i] = (int)(Math.random()*outputNeuronCount);
	    }//end for loop
	    System.out.println("Numbers Generated: " + Arrays.toString(dropouts));
	    return dropouts;
	}

	public ActivationFunction getActivationFunction() {
		return af;
	}
}
