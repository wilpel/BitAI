package com.BitAI.neural.activation;

public class ActivationTangens extends ActivationFunction{

	@Override
	public float process(float in) {
		return (float) Math.tan(in);
	}

	
	
}
