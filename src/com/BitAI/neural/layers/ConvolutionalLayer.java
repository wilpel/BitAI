package com.BitAI.neural.layers;

import com.BitAI.neural.activation.ActivationFunction;

public class ConvolutionalLayer extends BaseLayer {
	
	int[][] kernel = {
			{0, -1, 0},
			{-1, 5,-1},
			{0, -1, 0}
			};
	int[][] dimensions;

	public ConvolutionalLayer(int inputNeuronCount, int outputNeuronCount, ActivationFunction af) {
		super(inputNeuronCount, outputNeuronCount, af);
		
		/*for (int i = 0; i < outputNeuronCount; i++) {
			for (int j = 0; j < inputNeuronCount; j++) {

				weights[i][j] = 1;
			}
		}*/
	}
	
	public float[] ConvertDimS(float[][] input) {
		float[] out = new float[input.length + input[0].length];
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[0].length; j++) {
				out[i+j] = input[i][j];
			}
		}
		return out;
		
	}
	
	public float[][] Convlove(float[][] input) {
		float[][] output = input;
		//Loop array
		for(int i = 0; i < input.length; i++) {
			for(int j = 0; j < input[0].length; j++) {
				//Loop kernel
				for (int k = -1; k < 2; k++) {
					for (int l = -1; l < 2; l++) {
						try {
							output[i][j] += input[k][l]*(float)kernel[k+1][l+1];
						} catch (Exception e) {
							output[i][j] += 0;
							//Or just pass
						}
					}
					
				}
			}
			
		}
		return output;
	}

}
