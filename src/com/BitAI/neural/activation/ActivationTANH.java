package com.BitAI.neural.activation;

public class ActivationTANH extends ActivationFunction{

	public float process(float in) {
		return (float) Math.tanh(in);
	}

	
	
}
