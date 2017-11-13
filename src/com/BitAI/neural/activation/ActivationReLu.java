package com.BitAI.neural.activation;

public class ActivationReLu extends ActivationFunction{

	@Override
	public float process(float in) {

		if(in < 0)
			return 0;
		
		return in;
	}

	
}
