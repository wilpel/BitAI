package com.BitAI.neural.activation;

import java.io.Serializable;

public abstract class ActivationFunction implements Serializable {

	public abstract float process(float in);
	
}