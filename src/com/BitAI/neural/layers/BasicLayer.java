package com.BitAI.neural.layers;

import java.io.Serializable;

import com.BitAI.neural.activation.ActivationFunction;

public class BasicLayer implements Serializable{

	int neuronCount = 0;
	ActivationFunction af;
	
	public BasicLayer(int neuronCount, ActivationFunction af) {
		
		this.neuronCount = neuronCount;
		this.af = af;
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
	
}