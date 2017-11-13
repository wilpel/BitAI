package com.BitAI.neural.activation;

public class ActivationLeakyReLu extends ActivationFunction{

	float max, min;
	
	
	@Override
	public float process(float in) {
		
		if(in < (min+max)/2)
			return in/2;
		
		if(in>(min+max)/2)
			return in;
		
		return 0;
	}
	
	public void setMax(float max) {
		this.max = max;
	}
	
	public void setMin(float min) {
		this.min = min;
	}

}
