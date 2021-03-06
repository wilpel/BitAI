package com.BitAI.neural.layers;

import java.io.Serializable;

import com.BitAI.neural.activation.ActivationFunction;

public class HandleLayer implements Serializable{

	int type = -1;
	
	int neuronCount = 0;
	ActivationFunction af;
	
	public HandleLayer(int neuronCount, ActivationFunction af, int type) {
		
		this.neuronCount = neuronCount;
		this.af = af;
		
		this.type = type;
		
	}
	
	public int getNeuronCount() {
		return neuronCount;
	}
	
	public void setNeuronCount(int neuronCount) {
		this.neuronCount = neuronCount;
	}
	
	public ActivationFunction getActivationFunction() {
		return af;
	}
	
	public int getType() {
		return type;
	}
	
}