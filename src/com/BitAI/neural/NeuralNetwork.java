package com.BitAI.neural;

import java.math.BigDecimal;
import java.util.Arrays;

import com.BitAI.neural.layers.BasicLayer;
import com.BitAI.neural.layers.Layer;

public class NeuralNetwork {

	BasicLayer[] layers_array;
	Layer[] layers;

	public NeuralNetwork(BasicLayer[] layers_array) {

		this.layers_array = layers_array;

		layers = new Layer[layers_array.length - 1];

		for (int i = 0; i < layers.length; i++) {
			layers[i] = new Layer(layers_array[i].getNeuronCount(), layers_array[i + 1].getNeuronCount(),layers_array[i].getActivationFunction());
		}

	}

	public float[] compute(float[] input) {
		
		
		
		layers[0].feedForward(input);

		for (int i = 1; i < layers.length; i++) {
			
//			for(int j = 0; j < layers[i - 1].output.length; j++){
//				if(Float.isNaN(layers[i - 1].output[j])) {
//					System.out.println(Arrays.toString(input)+" | "+Arrays.toString(layers[i - 1].output));
//				}
//			}
			
			layers[i].feedForward(layers[i - 1].output);
		}

		return layers[layers.length - 1].output;
	}

	public void backPropagate(float[] expected) {

		for (int i = layers.length - 1; i >= 0; i--) {

			if (i == layers.length - 1) {
				layers[i].backPropagationOutput(expected);
			} else {
				layers[i].backPropagationHidden(layers[i + 1].gamma, layers[i + 1].weights);
			}

		}

		for (int i = 0; i < layers.length; i++) {

			layers[i].updateWeights();

		}

	}

	public float getError() {
		
		float error = 0;
		int times = 0;
		for(int i = 0; i < layers.length; i++) {
			
			for(int j = 0; j < layers[i].error.length; j++) {
				error += layers[i].error[j];
				times++;
			}
			
		}
		
		BigDecimal bd = new BigDecimal(Float.toString(error/times));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		return bd.floatValue();
	}
	
	public Layer[] getLayers() {
		return layers;
	}
	
	public BasicLayer[] getLayers_array() {
		return layers_array;
	}

}
