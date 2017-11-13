package com.BitAI.neural;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;

import com.BitAI.neural.layers.BasicLayer;
import com.BitAI.neural.layers.Layer;
import com.google.gson.Gson;

public class NeuralNetwork {

	BasicLayer[] layers_array;
	Layer[] layers;

	boolean loaded_weights = false;
	String dump_name = "/dumps/last_dump.txt";

	public NeuralNetwork(BasicLayer[] layers_array) {

		if (loaded_weights) {
			
		} else {

			this.layers_array = layers_array;

			layers = new Layer[layers_array.length - 1];

			for (int i = 0; i < layers.length; i++) {
				layers[i] = new Layer(layers_array[i].getNeuronCount(), layers_array[i + 1].getNeuronCount(),
						layers_array[i].getActivationFunction());
			}
		}

	}

	public void dump(Layer[] ls) throws FileNotFoundException, IOException {
		final String dir = System.getProperty("user.dir");
		File dumps_file = new File(dir + dump_name);
		PrintWriter writer = new PrintWriter(dump_file, "UTF-8");
		String json_dumps = new Gson().toJson(ls);
		// System.out.println(dir);
		writer.print(json_dumps);
		writer.close();
	}

	/*public Layer[] load() throws FileNotFoundException {
		final String dir = System.getProperty("user.dir");
		String dumps_file = dir + dump_name;
		FileReader fr = new FileReader(dump_name);
		String json_string = fr.read(cbuf, offset, fr.read())
	}*/

	public float[] compute(float[] input) {

		layers[0].feedForward(input);

		for (int i = 1; i < layers.length; i++) {

			// for(int j = 0; j < layers[i - 1].output.length; j++){
			// if(Float.isNaN(layers[i - 1].output[j])) {
			// System.out.println(Arrays.toString(input)+" | "+Arrays.toString(layers[i -
			// 1].output));
			// }
			// }

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
		for (int i = 0; i < layers.length; i++) {

			for (int j = 0; j < layers[i].error.length; j++) {
				error += layers[i].error[j];
				times++;
			}

		}

		BigDecimal bd = new BigDecimal(Float.toString(error / times));
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
