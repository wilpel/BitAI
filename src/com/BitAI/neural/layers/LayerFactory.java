package com.BitAI.neural.layers;

import java.util.ArrayList;
import java.util.List;

import com.BitAI.neural.activation.ActivationFunction;

public class LayerFactory {

	private List<HandleLayer> layers = new ArrayList<HandleLayer>();

	public static int TYPE_SIMPLELAYER = 0;
	public static int TYPE_CONVULUTIONAL = 1;

	public LayerFactory() {
		// TODO Auto-generated constructor stub
	}

	public void addLayer(int neurons, ActivationFunction af, int type) {

		layers.add(new HandleLayer(neurons, af, type));
	}

	
	public void addLayer(int neurons, int type) {

		layers.add(new HandleLayer(neurons,null,  type));
	}

	
	public List<HandleLayer> getLayers() {
		return layers;
	}
	

}
