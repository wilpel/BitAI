package com.BitAI.neural.genetic;

import com.BitAI.neural.NeuralNetwork;

public class NetworkScoreResult {

	private float score;
	private NeuralNetwork neuralNetwork;
	
	public NetworkScoreResult(float score, NeuralNetwork neuralNetwork) {
		this.score = score;
		this.neuralNetwork = neuralNetwork;
	}
	
	public NeuralNetwork getNeuralNetwork() {
		return neuralNetwork;
	}
	
	public float getScore() {
		return score;
	}
	
}
