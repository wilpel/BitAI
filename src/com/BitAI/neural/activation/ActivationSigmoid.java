package com.BitAI.neural.activation;

public class ActivationSigmoid extends ActivationFunction{

	@Override
	public float process(float in) {
		return (float) (1/(1+Math.pow(Math.E, -in)));
	}

}
