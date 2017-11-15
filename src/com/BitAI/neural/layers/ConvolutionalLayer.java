package com.BitAI.neural.layers;

import com.BitAI.neural.activation.ActivationFunction;

public class ConvolutionalLayer extends BaseLayer {
	
	int[][] kernel = {{0, -1, 0,-1 ,5 ,-1,0, -1,0}};
	int[][] dimensions;

	public ConvolutionalLayer(int inputNeuronCount, int outputNeuronCount, ActivationFunction af, int[][] dimensions) {
		super(inputNeuronCount, outputNeuronCount, af);
		this.dimensions = dimensions;
		
		/*for (int i = 0; i < outputNeuronCount; i++) {
			for (int j = 0; j < inputNeuronCount; j++) {

				weights[i][j] = 1;
			}
		}*/
	}
	
	public float[][] Convlove(float[][] input) {
		float[][] output = input;
		for(int i = 1; i < input[0].length-1; i++) {
			for(int j = 1; j < input[1].length-1; j++) {
				for (int k = -1; k < 2; k++) {
					for (int l = -1; l < 2; l++) {
						output[i][j] += input[k][l]*(float)kernel[k+1][l+1];
					}
					
				}
			}
			
		}
		return output;
	}
	
	/*public float[][] KernelProcess(float[][] input, int i, int j) {
		//Loop trough all pixels around the input pixel
		float[][] output = input;
		for (int k = -1; k < 2; k++) {
			for (int l = -1; l < 2; l++) {
				output[i][j] += input[k][l]*(float)kernel[k+1][l+1];
			}
			
		}
		return output;
	}
	
	public float[][] Convlove(float[][] input) {
		float[][] output = input;
		for(int i = 1; i < input[0].length-1; i++) {
			for(int j = 1; j < input[1].length-1; j++) {
				output[i][j] = KernelProcess(input, i, j);
			}
			
		}
		return output;
	}*/


}
