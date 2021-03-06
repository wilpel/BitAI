package com.BitAI.neural;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;

import com.BitAI.neural.layers.BaseLayer;
import com.BitAI.neural.layers.ConvolutionalLayer;
import com.BitAI.neural.layers.Layer;
import com.BitAI.neural.layers.LayerFactory;

public class NeuralNetwork implements Serializable {

	public BaseLayer[] layers;

	boolean loaded_weights = false;
	String dump_name = "/dumps/last_dump.txt";

	public NeuralNetwork(LayerFactory lf) {

		if (loaded_weights) {

		} else {
			
			layers = new Layer[lf.getLayers().size() - 1];
			
			for (int i = 0; i < layers.length; i++) {

				if(lf.getLayers().get(i).getType() == LayerFactory.TYPE_SIMPLELAYER) {
					layers[i] = new Layer(lf.getLayers().get(i).getNeuronCount(), lf.getLayers().get(i + 1).getNeuronCount(),	lf.getLayers().get(i).getActivationFunction());
				//	System.out.println("added layer with data: "+lf.getLayers().get(i).getNeuronCount()+", "+lf.getLayers().get(i + 1).getNeuronCount()+", "+lf.getLayers().get(i).getActivationFunction().process(1));
				}else if (lf.getLayers().get(i).getType() == LayerFactory.TYPE_CONVULUTIONAL) {
					System.out.println('p');
					layers[i] = new ConvolutionalLayer(lf.getLayers().get(i).getNeuronCount(), lf.getLayers().get(i + 1).getNeuronCount(),lf.getLayers().get(i).getActivationFunction());
					//Add covul layer here
					
				}
			}
		}

	}

	/*public void dump(NeuralNetwork ls) throws FileNotFoundException, IOException {
		final String dir = System.getProperty("user.dir");
		File dumps_file = new File(dir + dump_name);
		new File(dir).mkdirs();
		PrintWriter writer = new PrintWriter(dumps_file, "UTF-8");
		String json_dumps = new Gson().toJson(ls);
		// System.out.println(dir);
		writer.print(json_dumps);
		writer.close();
	}

	public static NeuralNetwork loadDump(String filepath) throws FileNotFoundException, IOException {
		FileReader fr = new FileReader(filepath);
		BufferedReader br = new BufferedReader(fr);
		String json_dumps = null;
		String line = null;
		while ((line = br.readLine()) != null) {
			json_dumps += line;
		}
		br.close();
		Gson gson = new Gson();
		// JsonElement jelement = new JsonParser().parse(json_dumps);
		NeuralNetwork loaded_net = gson.fromJson(json_dumps, NeuralNetwork.class);
		System.out.println(loaded_net);
		return loaded_net;
	}*/

	public static NeuralNetwork reader(String filepath) {
		NeuralNetwork loaded_NN = null;
		try {
			FileInputStream fi = new FileInputStream(new File(filepath));
			ObjectInputStream oi = new ObjectInputStream(fi);
			System.out.println(loaded_NN);
			// Read objects
			loaded_NN = (NeuralNetwork)oi.readObject();

			System.out.println(loaded_NN.getLayers()[1].outputNeuronCount);

			oi.close();
			fi.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return loaded_NN;

	}

	public void writer(NeuralNetwork nn) {
		try {
			final String dir = System.getProperty("user.dir");
			FileOutputStream f = new FileOutputStream(new File(dir +"/dumps/last_dump"));
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(nn);
			o.close();
			f.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
			e.printStackTrace();
		}
	}

	public float[] compute(float[] input) {

		layers[0].feedForward(input);

		for (int i = 1; i < layers.length; i++) {

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
		bd = bd.setScale(8, BigDecimal.ROUND_HALF_UP);

		return bd.floatValue();
	}

	public BaseLayer[] getLayers() {
		return layers;
	}

}