package com.BitAI.neural.activation;

public class ActivationGaussian extends ActivationFunction{

	@Override
	public float process(float in) {
		return (float) (Math.pow(Math.E, -Math.pow(in, 2)));
	}

}
