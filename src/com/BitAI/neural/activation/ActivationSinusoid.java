package com.BitAI.neural.activation;

public class ActivationSinusoid extends ActivationFunction{

	@Override
	public float process(float in) {
		return (float) Math.sin(in);
	}

	
	
}
