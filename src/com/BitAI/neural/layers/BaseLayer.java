package com.BitAI.neural.layers;

import java.io.Serializable;
import java.util.Random;

import com.BitAI.neural.Constants;
import com.BitAI.neural.activation.ActivationFunction;

public abstract class BaseLayer implements Serializable {

	public int inputNeuronCount;
	public int outputNeuronCount;

	public float[] output;
	public float[] input;


	protected ActivationFunction af;

	public BaseLayer(int inputNeuronCount, int outputNeuronCount, ActivationFunction af) {

		this.inputNeuronCount = inputNeuronCount;
		this.outputNeuronCount = outputNeuronCount;

		this.af = af;

		output = new float[outputNeuronCount];
		input = new float[inputNeuronCount];

	}
}
